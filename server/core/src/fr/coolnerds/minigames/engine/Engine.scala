package fr.coolnerds.minigames.engine

trait Engine {
  def askAndPlayAction(): Unit
}

trait EngineStateOps {
  def state: State
}

trait MiniGamesException extends Exception
