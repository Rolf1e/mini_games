package fr.coolnerds.minigames.domain.players

import fr.coolnerds.minigames.components.Drawable
import fr.coolnerds.minigames.domain.{Action, InGameException, MiniGamesException}
import fr.coolnerds.minigames.domain.impl.connectfour.{AddPonRed, AddPonYellow}
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.{redPon, yellowPon}
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourParser.parseFromConsole

case class BadColor(message: String) extends InGameException(message)

case class ConsolePlayer[Case](name: String, color: Int) extends Player[Case] {

  override def askAction(drawable: Drawable): Either[MiniGamesException, Seq[Action]] = {
    println(drawable.draw)
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
