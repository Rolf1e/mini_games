package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.domain.InAppException
import fr.coolnerds.minigames.infra.sql.SimpleConnectionPool.Key
import fr.coolnerds.minigames.infra.sql._
import fr.coolnerds.minigames.model.dao.GamesDAO.table
import fr.coolnerds.minigames.model.entities.GameEntity
import fr.coolnerds.minigames.utils.Result

import java.sql.ResultSet
import java.time.LocalDateTime
import scala.collection.mutable
import scala.language.{implicitConversions, postfixOps}

class GamesDAO private (_pool: ConnectionPool[Key]) extends AbstractDAO[Key] {
  implicit private val entityParser: JavaEntityParser[GameEntity] = GameEntityParser

  def findByName(name: String): Result[GameEntity] = {
    val statement = PreparedStatement(s"SELECT * FROM $table WHERE name = ?")(prepared => {
      prepared.setString(1, name)
      prepared
    })
    executeQuery(statement)
  }

  override def pool: ConnectionPool[Key] = _pool
}

object GamesDAO {
  private val table = "games"
  def apply(implicit pool: ConnectionPool[Key]): GamesDAO = {
    new GamesDAO(pool)
  }
}

private object GameEntityParser extends JavaEntityParser[GameEntity] {

  implicit override def toEntity(
      result: ResultSet
  ): Result[GameEntity] = {
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

    if game.isEmpty then {
      Left(MiniGamesNotFoundException())
    } else if 1 == game.length then {
      Right(game.head)
    } else {
      Left(InAppException("Ambiguous result found, please make a better request."))
    }
  }

}
