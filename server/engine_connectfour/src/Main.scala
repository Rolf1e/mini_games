import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.*
import fr.coolnerds.minigames.domain.impl.connectfour.*
import fr.coolnerds.minigames.domain.players.ConsolePlayer

object Main extends App {

  private val yellow = ConsolePlayer[ConnectFourCell]("Tigran", Yellow.ponValue)
  private val red = ConsolePlayer[ConnectFourCell]("Cassiopee", Red.ponValue)
  private val game = ConnectFourEngine(yellow, red)

  while !game.isOver do {
    game.askAndPlayAction()
    println(s"State: ${game.state.toJson}")
  }
  println(s"Game is over, ${game.state}")

}
