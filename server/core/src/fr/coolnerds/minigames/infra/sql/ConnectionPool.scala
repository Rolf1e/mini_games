package fr.coolnerds.minigames.infra.sql

import fr.coolnerds.minigames.engines.{InAppException, MiniGamesException}
import fr.coolnerds.minigames.infra.sql.SimpleConnectionPool.{Key, maxPoolSize}

import java.sql.DriverManager
import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.jdk.CollectionConverters._

trait ConnectionPool[K] {
  def acquireConnection: Either[MiniGamesException, PooledConnection[K]]
  def freeConnection(key: K): Either[MiniGamesException, Unit]
}

class SimpleConnectionPool private (
    connections: ConcurrentMap[Key, InternalConnection],
    factory: ConnectionFactory
) extends ConnectionPool[Key] {

  override def acquireConnection: Either[MiniGamesException, PooledConnection[Key]] = {
    if (connections.isEmpty) {
      Right(createAndSaveConnection.connection)
    } else if (connections.size() < maxPoolSize) {
      connections.asScala.values
        .find(!_.used.getAndSet(true))
        .map(_.connection)
        .toRight(InAppException("Failed to get a connection in the pool. :("))
    } else {
      Left(InAppException("Connection pool is full ! Please retry in a moment"))
    }
  }

  private def createAndSaveConnection: InternalConnection = {
    val freshConnection = factory.newConnection()
    connections.put(freshConnection.identifier, InternalConnection(freshConnection))
    connections.get(freshConnection.identifier)
  }

  override def freeConnection(key: Key): Either[MiniGamesException, Unit] = ???
}

object SimpleConnectionPool {
  type Key = Int
  private val maxPoolSize = 10

  def apply(connectionInfos: ConnectionInfos): ConnectionPool[Key] = {
    new SimpleConnectionPool(
      new ConcurrentHashMap[Key, InternalConnection](maxPoolSize),
      new ConnectionFactory(connectionInfos)
    )
  }

}

private class ConnectionFactory(connectionInfos: ConnectionInfos) {

  private val connectionsCounter = new AtomicInteger(0)

  def newConnection(): PooledConnection[Key] = {
    Class.forName("org.postgresql.Driver")
    val connection = DriverManager.getConnection(
      s"jdbc:${connectionInfos.engine}://${connectionInfos.host}:${connectionInfos.port}/${connectionInfos.database}",
      connectionInfos.username,
      connectionInfos.password
    )
    JavaPooledConnection(connectionsCounter.incrementAndGet(), connection)
  }
}

private[this] case class InternalConnection(
    connection: PooledConnection[Key],
    used: AtomicBoolean = new AtomicBoolean(false)
)
