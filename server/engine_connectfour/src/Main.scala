import fr.coolnerds.minigames.players.ConsolePlayer
import fr.coolnerds.minigames.engine.ConnectFour
import fr.coolnerds.minigames.engine.ConnectFourConstants._

class Main extends App {

    val yellow = ConsolePlayer[Case]("Tigran", yellowPon)
    val red = ConsolePlayer[Case]("Cassiopee", redPon)
    val game = ConnectFour(yellow, red)

    game.askAndPlayAction()

    game.askAndPlayAction()

}
