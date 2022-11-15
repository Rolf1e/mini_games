package fr.coolnerds.minigames.board.connectfour

import fr.coolnerds.minigames.boards.{Board, Player}

case class TestPlayer[Case, Action, State](actions: Seq[Action], color: String = "")
    extends Player[Case, Action, State] {

  override def askAction(board: Board[Case, Action, State]): Seq[Action] = {
      println(s"$color plays $actions")
      actions
  }
}
