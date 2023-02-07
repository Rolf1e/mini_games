package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.model.entities.GamesInstancesEntity

class GamesInstanceDAO() {

  def save(gameInstance: GamesInstancesEntity): Unit = ???

  def findByGameAndInstance(gameId: Int, instanceId: Long): Seq[GamesInstancesEntity] = ???

}

