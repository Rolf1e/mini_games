package fr.coolnerds.minigames.boards

trait Board[Case] {

  def isWon: Boolean

  def draw(): String

  def at(coordinates: Coordinates): Option[Case]

  def play(action: Action): Unit

}
