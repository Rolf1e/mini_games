import fr.coolnerds.minigames.players.ConsolePlayer
import fr.coolnerds.minigames.engine.ConnectFour
import fr.coolnerds.minigames.engine.ConnectFourConstants._

object Main extends App {

  val yellow = ConsolePlayer[Case]("Tigran", yellowPon)
  val red = ConsolePlayer[Case]("Cassiopee", redPon)
  val game = ConnectFour(yellow, red)
  val maxTurn = 3

  var turn = 1
  while (turn <= maxTurn && !game.isWon) {
    turn += 1
    game.askAndPlayAction()
  }

}
