package fr.coolnerds.minigames.infra.sql

import fr.coolnerds.minigames.domain.InAppException
import fr.coolnerds.minigames.infra.sql.SimpleConnectionPool.{Key, maxPoolSize}
import fr.coolnerds.minigames.utils.Result

import java.sql.DriverManager
import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.jdk.CollectionConverters._

trait ConnectionPool[K] {
  def acquireConnection: Result[PooledConnection[K]]
  def freeConnection(key: K): Result[Unit]
}

class SimpleConnectionPool private (
    connections: ConcurrentMap[Key, InternalConnection],
    factory: ConnectionFactory
) extends ConnectionPool[Key] {

  override def acquireConnection: Result[PooledConnection[Key]] = {
    if connections.isEmpty then {
      Right(createAndSaveConnection.connection)
    } else if connections.size() < maxPoolSize then {
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

  override def freeConnection(key: Key): Result[Unit] = {
    connections.asScala.get(key) match {
      case Some(connection) => Right(connection.used.set(false))
      case None             => Right(InAppException(s"Connection does not exist with key $key"))
    }
  }
}

object SimpleConnectionPool {
  type Key = Int
  private val maxPoolSize = 10

  def apply(connectionInfos: ConnectionInfos): ConnectionPool[Key] = {
    Class.forName(connectionInfos.driver)
    new SimpleConnectionPool(
      new ConcurrentHashMap[Key, InternalConnection](maxPoolSize),
      new ConnectionFactory(connectionInfos)
    )
  }

}

private class ConnectionFactory(connectionInfos: ConnectionInfos) {

  private val connectionsCounter = new AtomicInteger(0)

  def newConnection(): PooledConnection[Key] = {
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
