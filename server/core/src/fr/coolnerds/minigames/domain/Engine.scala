package fr.coolnerds.minigames.domain

import fr.coolnerds.minigames.utils.Result

import scala.concurrent.Future

trait Engine {
  def askAndPlayAction(): Result[Unit]
}

trait EngineStateOps extends StateOps {
  def save(): Future[State]
}
