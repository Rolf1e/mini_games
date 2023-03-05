package fr.coolnerds.minigames.domain.impl

import fr.coolnerds.minigames.components.InputParser
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.{Cell, rowLength}
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourEngine.InternalBoard
import fr.coolnerds.minigames.domain.{Action, InGameException, MiniGamesException, Turn}

import scala.io.StdIn.readLine
import scala.util.{Failure, Properties, Success, Try}

package object connectfour {

  enum Color {
    case Red, Yellow
  }

  object ConnectFourConstants {
    type Cell = Int

    val yellowPon = 1
    val redPon = 2

    val rowLength = 7
    val columnLength = 6

    val emptyCell = 0

    val lineSeparator: String = Properties.lineSeparator
  }

  sealed trait ConnectFourAction extends Action
  case class AddPonYellow(col: Int) extends ConnectFourAction
  case class AddPonRed(col: Int) extends ConnectFourAction

  sealed trait ConnectFourTurn extends Turn
  case class YellowTurn(board: InternalBoard) extends ConnectFourTurn {}
  case class RedTurn(board: InternalBoard) extends ConnectFourTurn {}
  case class Won(color: Int) extends ConnectFourTurn {}
  object Draw extends ConnectFourTurn {}

  abstract class ConnectFourException(message: String) extends InGameException(message)
  case class ReadInput(message: String) extends ConnectFourException(message)
  case class NaN(message: String) extends ConnectFourException(message)
  case class BadColumnIndex(message: String) extends ConnectFourException(message)

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

    implicit object ColumnParser extends ConnectFourParser[Cell] {

      override def parse(input: String): Either[MiniGamesException, Cell] = {
        Try(input.toInt) match {
          case Failure(e) =>
            e match {
              case _: NumberFormatException => Left(NaN(input))
            }
          case Success(column) =>
            if (column >= 0 && column < rowLength) Right(column)
            else Left(BadColumnIndex(s"Column must be between 0 and ${rowLength - 1}"))
        }
      }

    }

  }
}
