package fr.coolnerds.minigames.players

import fr.coolnerds.minigames.boards.Board
import fr.coolnerds.minigames.boards.Action
import fr.coolnerds.minigames.engine.MiniGamesException

trait Player[Case] {

  def askAction(board: Board[Case]): Either[MiniGamesException, Seq[Action]]

  def getColor: Int

}
