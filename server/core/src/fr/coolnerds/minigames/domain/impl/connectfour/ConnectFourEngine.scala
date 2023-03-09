package fr.coolnerds.minigames.domain.impl.connectfour

import fr.coolnerds.minigames.components.{Drawable, InputParser}
import fr.coolnerds.minigames.domain.*
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.*
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourEngine.{
  InternalBoard,
  InternalPlayer
}
import fr.coolnerds.minigames.domain.players.Player

import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.util.{Failure, Properties, Success, Try}

class ConnectFourEngine(board: InternalBoard, yellow: InternalPlayer, red: InternalPlayer)
    extends Engine
    with EngineStateOps
    with EngineTurnOps
    with Drawable {

  override def askAndPlayAction(): Unit = ???

  override def state: State = ???

  override def draw: String =
    s"ConnectFour(E: ${Color.emptyCell} Y: ${yellow.getColor} R: ${red.getColor})\n${board.draw}"

  override def save(): Future[State] = ???

  override def turn: Turn = ???
}

object ConnectFourEngine {
  type InternalPlayer = Player[Cell]
  type InternalBoard = ConnectFourBoard

  def apply(yellow: InternalPlayer, red: InternalPlayer): ConnectFourEngine = {
    new ConnectFourEngine(ConnectFourBoard.emptyBoard(), yellow, red)
  }
}
