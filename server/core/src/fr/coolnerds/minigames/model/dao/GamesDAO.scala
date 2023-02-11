package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.engines.{InAppException, MiniGamesException}
import fr.coolnerds.minigames.infra.sql._
import fr.coolnerds.minigames.model.entities.GameEntity

import java.sql.ResultSet
import java.time.LocalDateTime
import scala.collection.mutable
import scala.language.implicitConversions

class GamesDAO(implicit pool: ConnectionPool[SimpleConnectionPool.Key]) {
  implicit private val entityParser: JavaEntityParser[GameEntity] = GameEntityParser

  def findByName(name: String): Either[MiniGamesException, GameEntity] = {
    val statement = PreparedStatement("SELECT * FROM games WHERE name = ?")(prepared => {
      prepared.setString(1, name)
      prepared
    })
    pool.acquireConnection
      .flatMap(_.executeQuery(statement))
  }

}

//object GamesDAO extends App {
//  val infos = ConnectionInfos(
//    host = "localhost",
//    port = 5432,
//    database = "default_database",
//    username = "username",
//    password = "password"
//  )
//
//  implicit val pool = SimpleConnectionPool(infos)
//  new GamesDAO()
//    .findByName("connect four")
//    .fold(println(_), println(_))
//}

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
