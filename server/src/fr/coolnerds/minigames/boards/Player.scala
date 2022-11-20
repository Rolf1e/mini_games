package fr.coolnerds.minigames.boards

trait Player[Case] {

  def askAction(board: Board[Case]): Seq[Action]

  def getColor(): Int

}
