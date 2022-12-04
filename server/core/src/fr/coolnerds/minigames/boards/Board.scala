package fr.coolnerds.minigames.boards

trait Board[Case] {
  def isWon: Boolean

  def isFull: Boolean
}

trait BoardOps[Case] {
  def at(coordinates: Coordinates): Option[Case]

  def play(action: Action): Unit
}
