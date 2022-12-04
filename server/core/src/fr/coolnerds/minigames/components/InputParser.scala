package fr.coolnerds.minigames.components

import fr.coolnerds.minigames.engine.MiniGamesException

trait InputParser[Output] {

  def parse(input: String): Either[MiniGamesException, Output]

}
