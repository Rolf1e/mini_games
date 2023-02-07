package fr.coolnerds.minigames.components

import fr.coolnerds.minigames.engines.MiniGamesException

trait InputParser[Output] {

  def parse(input: String): Either[MiniGamesException, Output]

}
