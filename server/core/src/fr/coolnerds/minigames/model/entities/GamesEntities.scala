package fr.coolnerds.minigames.model.entities

import fr.coolnerds.minigames.domain.State
import java.time.temporal.Temporal

case class GameEntity(
    id: Int,
    name: String,
    minPlayers: Int,
    maxPlayers: Int,
    createdAt: Temporal,
    updatedAt: Temporal
)

case class GamesInstancesEntity(
    id: Int,
    players: Seq[Int],
    gameId: Int,
    state: State,
    instanceId: Long,
    createdAt: Temporal
)
