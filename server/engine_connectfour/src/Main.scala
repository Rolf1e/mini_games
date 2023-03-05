import fr.coolnerds.minigames.domain.impl.connectfour.{ConnectFourEngine, ConnectFourTurn, Draw, RedTurn, Won, YellowTurn}
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants._
import fr.coolnerds.minigames.domain.players.ConsolePlayer

object Main extends App {

  private val yellow = ConsolePlayer[Cell]("Tigran", yellowPon)
  private val red = ConsolePlayer[Cell]("Cassiopee", redPon)
  private val game = ConnectFourEngine(yellow, red)

  while (!gameIsOver(game)) {
    game.askAndPlayAction()
  }

  private def gameIsOver(game: ConnectFourEngine): Boolean = game.state match {
    case YellowTurn(_) | RedTurn(_) => false
    case Won(winner) =>
      println(s"Winner is $winner")
      true
    case _: Draw =>
      println("It is a draw !")
      true
  }

}
