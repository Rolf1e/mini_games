package fr.coolnerds.minigames.engine

import fr.coolnerds.minigames.boards.connectfour.ConnectFourBoard
import fr.coolnerds.minigames.boards.Action
import fr.coolnerds.minigames.components.{Drawable, InputParser}
import fr.coolnerds.minigames.engine.ConnectFour.{InternalBoard, InternalPlayer}
import fr.coolnerds.minigames.engine.ConnectFourConstants._
import fr.coolnerds.minigames.players.Player

import scala.io.StdIn.readLine
import scala.util.{Failure, Properties, Success, Try}

class ConnectFour(board: InternalBoard, yellow: InternalPlayer, red: InternalPlayer)
    extends Engine
    with EngineStateOps
    with Drawable {

  private var currentPlayer = yellow

  override def askAndPlayAction(): Unit = {
    checkActions() match {
      case Some(action) => {
        board.play(action)
        nextTurn()
      }
      case None =>
        if (currentPlayer.getColor == yellowPon) Seq(YellowTurn(board)) else Seq(RedTurn(board))
    }
  }

  /**
    * At connect four, a player can do only one action.
    */
  private def checkActions(): Option[Action] = {
    currentPlayer.askAction(board) match {
      case Right(actions) if actions.size == 1 => Some(actions.head)
      case _                                   => None
    }
  }

  override def state: ConnectFourState = {
    if (board.isWon) {
      Won(currentPlayer.getColor)
    } else if (board.isFull) {
      Draw()
    } else if (currentPlayer.getColor == yellowPon) {
      RedTurn(board)
    } else {
      YellowTurn(board)
    }
  }

  override def draw: String = {
    s"ConnectFour(E: 0 Y: ${yellowPon} R: ${redPon})$lineSeparator${board.draw}"
  }

  private def nextTurn(): Unit = {
    currentPlayer = if (currentPlayer == yellow) red else yellow
  }

}

object ConnectFour {
  type InternalPlayer = Player[Case]
  type InternalBoard = ConnectFourBoard

  def apply(yellow: InternalPlayer, red: InternalPlayer): ConnectFour = {
    new ConnectFour(ConnectFourBoard.emptyBoard(), yellow, red)
  }
}

object ConnectFourConstants {
  type Case = Int

  val yellowPon = 1
  val redPon = 2

  val rowLength = 7
  val columnLength = 6

  val lineSeparator: String = Properties.lineSeparator
}

sealed trait ConnectFourAction extends Action
case class AddPonYellow(col: Int) extends ConnectFourAction
case class AddPonRed(col: Int) extends ConnectFourAction

sealed trait ConnectFourState extends State
case class YellowTurn(board: InternalBoard) extends ConnectFourState
case class RedTurn(board: InternalBoard) extends ConnectFourState
case class Won(color: Int) extends ConnectFourState
class Draw private () extends ConnectFourState
object Draw {
  def apply(): Draw = new Draw()
}

sealed trait ConnectFourException extends MiniGamesException
case class ReadInput(message: String) extends ConnectFourException
case class NaN(message: String) extends ConnectFourException
case class BadColumnIndex(message: String) extends ConnectFourException

trait ConnectFourParser[T] extends InputParser[T]

object ConnectFourParser {

  def parseFromConsole[T](
      implicit parser: ConnectFourParser[T]
  ): Either[MiniGamesException, T] = {
    Try(readLine()) match {
      case Failure(exception) => Left(ReadInput(exception.getMessage))
      case Success(input)     => parser.parse(input)
    }
  }

  implicit object ColumnParser extends ConnectFourParser[Case] {

    override def parse(input: String): Either[MiniGamesException, Case] = {
      try {
        val column = input.toInt
        if (column >= 0 && column < rowLength) {
          Right(column)
        } else Left(BadColumnIndex(s"Column must be between 0 and ${rowLength - 1}"))
      } catch {
        case _: java.lang.NumberFormatException => Left(NaN(input))
      }
    }

  }

}
