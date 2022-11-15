package fr.coolnerds.minigames.boards

trait Player[Case, Action, State] {

  def askAction(board: Board[Case, Action, State]): Seq[Action]

}
