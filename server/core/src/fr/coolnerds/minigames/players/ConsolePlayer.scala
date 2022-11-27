package fr.coolnerds.minigames.players

import fr.coolnerds.minigames.boards.{Action, Board}
import fr.coolnerds.minigames.engine.ConnectFourConstants.{redPon, yellowPon}
import fr.coolnerds.minigames.engine.ConnectFourParser.parseFromConsole
import fr.coolnerds.minigames.engine.{AddPonRed, AddPonYellow, MiniGamesException}

case class BadColor(message: String) extends MiniGamesException

case class ConsolePlayer[Case](name: String, color: Int) extends Player[Case] {

  override def askAction(board: Board[Case]): Either[MiniGamesException, Seq[Action]] = {
    println(board.draw())
    println("column?")

    for (column <- parseFromConsole[Int])
      yield {
        if (yellowPon == color) {
          return Right(Seq(AddPonYellow(column)))
        } else if (redPon == color) {
          return Right(Seq(AddPonRed(column)))
        } else {
          return Left(BadColor(s"$color is not legal !"))
        }
      }

  }

  override def getColor: Int = color

}
