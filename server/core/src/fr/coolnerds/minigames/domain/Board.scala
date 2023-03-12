package fr.coolnerds.minigames.domain

import fr.coolnerds.minigames.utils.Result

trait Board[Cell] {}

trait BoardStateOps extends StateOps {
  def isWon: Boolean

  def isFull: Boolean
}

trait BoardOps[Cell] {
  def play(action: Action): Result[Unit]
}

trait BoardFactory[Cell] {
  def toBoard: Result[Board[Cell]]
}
