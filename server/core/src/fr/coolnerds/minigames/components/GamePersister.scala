package fr.coolnerds.minigames.components

import fr.coolnerds.minigames.components.GamePersister.toEntity
import fr.coolnerds.minigames.domain.State
import fr.coolnerds.minigames.model.dao.GamesInstancesDAO
import fr.coolnerds.minigames.model.entities.GamesInstancesEntity
import fr.coolnerds.minigames.utils.Result

import java.time.LocalDateTime
import scala.concurrent.Future

case class GameInfos(partyId: Int, players: Seq[Int], gameId: Int, instanceId: Int)

class GamePersister(model: GamesInstancesDAO) {

  def save(infos: GameInfos, state: State): Result[Unit] = model.save(toEntity(infos, state))

}

object GamePersister {

  def apply(model: GamesInstancesDAO) = new GamePersister(model)

  private def toEntity(infos: GameInfos, state: State): GamesInstancesEntity = {
    GamesInstancesEntity(
      infos.partyId,
      infos.players,
      infos.gameId,
      state,
      infos.instanceId,
      LocalDateTime.now()
    )
  }

}
