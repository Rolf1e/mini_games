package fr.coolnerds.minigames.boards

trait Board[Case, Action, State] {

  def draw(): String

  def at(coordinates: Coordinates): Option[Case]

  def play(action: Action): State

}
