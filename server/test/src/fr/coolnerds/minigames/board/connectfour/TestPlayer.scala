package fr.coolnerds.minigames.board.connectfour

import fr.coolnerds.minigames.boards.Action
import fr.coolnerds.minigames.boards.Board
import fr.coolnerds.minigames.boards.Player

case class TestPlayer[Case](actions: Seq[Action], color: Int = 0)
    extends Player[Case] {

  override def getColor(): Int = color

  override def askAction(board: Board[Case]): Seq[Action] = {
    println(s"$color plays $actions")
    actions
  }
}

case class CheaterPlayer[Case](actions: Seq[Action], color: Int = 0)
    extends Player[Case] {

  override def getColor(): Int = color

  override def askAction(board: Board[Case]): Seq[Action] = {
    println(s"Cheater $color plays $actions at the same time !")
    actions
  }
}
