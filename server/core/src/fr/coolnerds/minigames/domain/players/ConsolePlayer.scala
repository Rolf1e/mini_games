package fr.coolnerds.minigames.domain.players

import fr.coolnerds.minigames.components.Drawable
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourParser.parseFromConsole
import fr.coolnerds.minigames.domain.impl.connectfour.{AddPon, Color}
import fr.coolnerds.minigames.domain.{Action, InAppException, InGameException, MiniGamesException}

case class ConsolePlayer[Case](name: String, color: Int) extends Player[Case] {

  override def askAction(drawable: Drawable): Either[MiniGamesException, Seq[Action]] = {
    println(drawable.draw)
    println("column?")

    for
      column <- parseFromConsole[Int]
      action <- createAction(column, color)
    yield action
  }

  private def createAction(column: Int, color: Int): Either[MiniGamesException, Seq[Action]] = {
    Color.fromCell(color) match
      case Some(c) => Right(Seq(AddPon(c, column)))
      case None    => Left(InAppException(s"$color is not legal !"))
  }

  override def getColor: Int = color

}
