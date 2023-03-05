package fr.coolnerds.minigames.domain

import scala.concurrent.Future

trait Engine {
  def askAndPlayAction(): Unit
}

trait EngineTurnOps {
  def turn: Turn
}

trait EngineStateOps {
  def state: State
  def save(): Future[State]
}

