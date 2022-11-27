package fr.coolnerds.minigames.common

import fr.coolnerds.minigames.boards.{Action, Board}
import fr.coolnerds.minigames.engine.MiniGamesException
import fr.coolnerds.minigames.players.Player

case class TestPlayer[Case](actions: Seq[Action], color: Int = 0) extends Player[Case] {

  override def getColor: Int = color

  override def askAction(board: Board[Case]): Either[MiniGamesException, Seq[Action]] = {
    println(s"$color plays $actions")
    Right(actions)
  }
}

case class CheaterPlayer[Case](actions: Seq[Action], color: Int = 0) extends Player[Case] {

  override def getColor: Int = color

  override def askAction(board: Board[Case]): Either[MiniGamesException, Seq[Action]] = {
    println(s"Cheater $color plays $actions at the same time !")
    Right(actions)
  }
}
