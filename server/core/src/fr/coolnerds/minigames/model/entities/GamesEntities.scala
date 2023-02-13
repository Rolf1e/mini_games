package fr.coolnerds.minigames.model.entities

import fr.coolnerds.minigames.engines.State

import java.time.LocalDateTime

case class GameEntity(
    id: Int,
    name: String,
    minPlayers: Int,
    maxPlayers: Int,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime
)

case class GamesInstancesEntity(
    id: Int,
    players: Seq[Int],
    gameId: Int,
    state: State,
    instanceId: Long,
    createdAt: LocalDateTime
)
