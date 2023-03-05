package fr.coolnerds.minigames

import fr.coolnerds.minigames.domain.MiniGamesException
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourBoard

package object utils {
  type Result[R] = Either[MiniGamesException, R]

}

