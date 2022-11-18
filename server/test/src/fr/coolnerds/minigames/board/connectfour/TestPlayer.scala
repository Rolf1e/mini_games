package fr.coolnerds.minigames.board.connectfour

import fr.coolnerds.minigames.boards.{Board, Player}

case class TestPlayer[Case, Action, State](actions: Seq[Action], color: Int = 0)
    extends Player[Case, Action, State] {

  override def getColor(): Int = color

  override def askAction(board: Board[Case, Action, State]): Seq[Action] = {
    println(s"$color plays $actions")
    actions
  }
}

case class CheaterPlayer[Case, Action, State](actions: Seq[Action], color: Int = 0)
    extends Player[Case, Action, State] {

  override def getColor(): Int = color

  override def askAction(board: Board[Case, Action, State]): Seq[Action] = {
    println(s"Cheater $color plays $actions at the same time !")
    actions
  }
}
