package fr.coolnerds.minigames.engine

trait InputParser[Output] {

  def parse(input: String): Either[String, Output]

}
