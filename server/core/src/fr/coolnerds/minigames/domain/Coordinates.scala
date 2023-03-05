package fr.coolnerds.minigames.domain

sealed trait Coordinates {}

case class Point2D(x: Int, y: Int) extends Coordinates
