import fr.coolnerds.minigames.engine.{ConnectFour, ConnectFourState, Draw, RedTurn, Won, YellowTurn}
import fr.coolnerds.minigames.engine.ConnectFourConstants._
import fr.coolnerds.minigames.players.ConsolePlayer

object Main extends App {

  private val yellow = ConsolePlayer[Case]("Tigran", yellowPon)
  private val red = ConsolePlayer[Case]("Cassiopee", redPon)
  private val game = ConnectFour(yellow, red)

  while (!gameIsOver(game)) {
    game.askAndPlayAction()
  }

  private def gameIsOver(game: ConnectFour): Boolean = game.state match {
    case YellowTurn(_) | RedTurn(_) => false
    case Won(winner) =>
      println(s"Winner is $winner")
      true
    case _: Draw =>
      println("It is a draw !")
      true
  }

}
