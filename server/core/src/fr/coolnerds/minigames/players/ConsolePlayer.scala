package fr.coolnerds.minigames.players

import fr.coolnerds.minigames.boards.{Action, Board}
import fr.coolnerds.minigames.engine.ConnectFourParser.parseFromConsole
import fr.coolnerds.minigames.engine.ConnectFourConstants.{redPon, yellowPon}
import fr.coolnerds.minigames.engine.{AddPonRed, AddPonYellow}

case class ConsolePlayer[Case](name: String, color: Int) extends Player[Case] {

  override def askAction(board: Board[Case]): Either[String, Seq[Action]] = {
    println(board.draw())
    println("column?")
    parseFromConsole[Int].flatMap(column => {
      if (yellowPon == color) {
        Right(Seq(AddPonYellow(column)))
      } else if (redPon == color) {
        Right(Seq(AddPonRed(column)))
      } else {
        Left(s"$color is not legal !")
      }
    })

  }

  override def getColor(): Int = color

}
