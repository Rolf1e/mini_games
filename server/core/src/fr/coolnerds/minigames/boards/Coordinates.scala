package fr.coolnerds.minigames.boards

sealed trait Coordinates {}

case class Point2D(x: Int, y: Int) extends Coordinates
