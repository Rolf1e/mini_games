package fr.coolnerds.minigames

import fr.coolnerds.minigames.engines.MiniGamesException

package object utils {
  type Result[R] = Either[MiniGamesException, R]
}
