package fr.coolnerds.minigames.domain.impl.connectfour

import fr.coolnerds.minigames.components.{Drawable, InputParser}
import fr.coolnerds.minigames.domain.*
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.*
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourEngine.{
  InternalBoard,
  InternalPlayer
}
import fr.coolnerds.minigames.domain.players.Player
import fr.coolnerds.minigames.utils.Result

import scala.collection.mutable
import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.util.{Failure, Properties, Success, Try}

class ConnectFourEngine(board: InternalBoard, yellow: InternalPlayer, red: InternalPlayer)
    extends Engine
    with EngineStateOps
    with Drawable {

  private val playersOrder = mutable.Queue[InternalPlayer](yellow, red)

  override def askAndPlayAction(): Result[Unit] = {
    val currentPlayer = playersOrder.dequeue()

    val action = currentPlayer.askAction(this) match {
      case Right(actions) if actions.size == 1 => actions.head
      case Left(ex)                            => return Left(ex)
      case _ =>
        return Left(
          InAppException(
            s"${currentPlayer.getColor} is a cheater ! He tries to plays more than one action !"
          )
        )
    }
    board.play(action)
    playersOrder.enqueue(currentPlayer)
    Right(())
  }

  override def state: State = board.state

  override def draw: String =
    s"ConnectFour(E: ${Color.emptyCell} Y: ${yellow.getColor} R: ${red.getColor})\n${board.draw}"

  override def save(): Future[State] = ???

}

object ConnectFourEngine {
  type InternalPlayer = Player[ConnectFourCell]
  type InternalBoard = ConnectFourBoard

  def apply(yellow: InternalPlayer, red: InternalPlayer): ConnectFourEngine = {
    new ConnectFourEngine(ConnectFourBoard.emptyBoard(), yellow, red)
  }
}
