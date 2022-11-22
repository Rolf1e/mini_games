package fr.coolnerds.minigames.players

import fr.coolnerds.minigames.boards.Board
import fr.coolnerds.minigames.boards.Action

trait Player[Case] {

  def askAction(board: Board[Case]): Either[String, Seq[Action]]

  def getColor(): Int

}
