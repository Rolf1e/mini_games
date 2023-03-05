package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.domain.{InAppException, State, StateFactory}
import fr.coolnerds.minigames.infra.sql.SimpleConnectionPool.Key
import fr.coolnerds.minigames.infra.sql.{ConnectionPool, JavaEntityParser, PreparedStatement}
import fr.coolnerds.minigames.model.dao.GamesInstancesDAO.table
import fr.coolnerds.minigames.model.entities.GamesInstancesEntity
import fr.coolnerds.minigames.utils.Result

import java.sql.ResultSet
import scala.collection.mutable
import scala.language.implicitConversions

class GamesInstancesDAO private (_pool: ConnectionPool[Key])
    extends AbstractDAO[Key]
    with PersistenceDAO[GamesInstancesEntity] {

  override def pool: ConnectionPool[Key] = _pool

  def findByGameAndInstance(gameId: Int, instanceId: Int): Result[GamesInstancesEntity] = {
    val statement =
      PreparedStatement(s"SELECT * FROM $table WHERE game_id = ? AND instance_id = ?")(prepared => {
        prepared.setInt(1, gameId)
        prepared.setInt(2, instanceId)
        prepared
      })
    executeQuery(statement)(GamesInstancesEntityParser)
  }

  override def save(
      entity: GamesInstancesEntity
  ): Result[Unit] = {
    val statement = PreparedStatement(
      "INSERT INTO games_instances(players_id, state, instance_id) VALUES (?, ?, ?)"
    )(prepared => {
      prepared.setString(1, entity.players.mkString(","))
      prepared.setString(2, entity.state.toJson)
      prepared.setLong(3, entity.instanceId)
      prepared
    })
    executeQuery(statement)
  }
}

object GamesInstancesDAO {
  private val table = "games_instances"
}

private object GamesInstancesEntityParser
    extends JavaEntityParser[GamesInstancesEntity]
    with StateFactory {

  implicit override def toState(json: String): State = ???

  implicit override def toEntity(
      result: ResultSet
  ): Result[GamesInstancesEntity] = {
    val game = mutable.Stack[GamesInstancesEntity]()
    while (result.next()) {
      game.addOne(
        GamesInstancesEntity(
          result.getInt("id"),
          result.getString("players_id").split(",").map(_.toInt),
          result.getInt("game_id"),
          result.getString("state"),
          result.getLong("instanceId"),
          result.getDate("created_at").toInstant
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
