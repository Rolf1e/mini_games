import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.*
import fr.coolnerds.minigames.domain.impl.connectfour.*
import fr.coolnerds.minigames.domain.players.ConsolePlayer

object Main extends App {

  private val yellow = ConsolePlayer[ConnectFourCell]("Tigran", Yellow.ponValue)
  private val red = ConsolePlayer[ConnectFourCell]("Cassiopee", Red.ponValue)
  private val game = ConnectFourEngine(yellow, red)

  while (!gameIsOver(game)) {
    game.askAndPlayAction()
  }

  private def gameIsOver(game: ConnectFourEngine): Boolean = game.state match {
    case YellowTurn(_) | RedTurn(_) => false
    case Won(winner) =>
      println(s"Winner is $winner")
      true
    case Draw =>
      println("It is a draw !")
      true
  }

}
