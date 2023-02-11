package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.engines.{InAppException, MiniGamesException}
import fr.coolnerds.minigames.infra.sql._
import fr.coolnerds.minigames.model.entities.GameEntity

import java.sql.ResultSet
import java.time.LocalDateTime
import scala.collection.mutable
import scala.language.implicitConversions

class GamesDAO(pool: ConnectionPool[SimpleConnectionPool.Key]) {
  implicit private val entityParser: JavaEntityParser[GameEntity] = GameEntityParser

  def findByName(name: String): Either[MiniGamesException, GameEntity] = {
    val statement = UnPreparedStatement("SELECT * FROM games")
    pool.acquireConnection.flatMap(_.executeQuery(statement))
  }

}

private object GameEntityParser extends JavaEntityParser[GameEntity] {

  implicit override def toEntity(
      result: ResultSet
  ): Either[MiniGamesException, GameEntity] = {
    val game = mutable.Stack[GameEntity]()
    while (result.next()) {
      game.addOne(
        GameEntity(
          result.getInt("id"),
          result.getString("name"),
          result.getInt("min_players"),
          result.getInt("max_players"),
          LocalDateTime.now(),
          LocalDateTime.now()
        )
      )
    }

    if (game.isEmpty) {
      Left(MiniGamesNotFoundException())
    } else if (1 == game.length) {
      Right(game.head)
    } else {
      Left(InAppException("Ambiguous result found, please make a better request."))
    }
  }

}

object GamesDAO extends App {

  def apply(pool: ConnectionPool[Int]): GamesDAO = new GamesDAO(pool)

  val infos = ConnectionInfos(
    "postgresql",
    "localhost",
    5432,
    "default_database",
    "username",
    "password"
  )

  val pool = SimpleConnectionPool(infos)

  GamesDAO(pool)
    .findByName("") match {
    case Left(e)     => println(s"Left ${e}")
    case Right(game) => println(s"Right ${game}")
  }

}
