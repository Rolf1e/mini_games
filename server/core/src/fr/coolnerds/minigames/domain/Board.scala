package fr.coolnerds.minigames.domain

trait Board[Cell] {
  def isWon: Boolean

  def isFull: Boolean
}

trait BoardStateOps {
  def state: State
}

trait BoardOps[Cell] {
  def at(coordinates: Coordinates): Option[Cell]

  def play(action: Action): Unit
}

trait BoardFactory[Cell] {
  def toBoard: Either[MiniGamesException, Board[Cell]]
}
