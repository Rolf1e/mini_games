package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.infra.sql.SimpleConnectionPool.Key
import fr.coolnerds.minigames.infra.sql.{ConnectionPool, PreparedStatement}
import fr.coolnerds.minigames.model.dao.GamesInstancesDAO.table
import fr.coolnerds.minigames.model.entities.GamesInstancesEntity
import fr.coolnerds.minigames.utils.Result

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
    executeQuery(statement)
  }

  override def save(
      entity: GamesInstancesEntity
  ): Result[Nothing] = {
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
