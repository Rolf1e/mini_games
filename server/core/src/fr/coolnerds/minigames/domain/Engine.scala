package fr.coolnerds.minigames.domain

import fr.coolnerds.minigames.utils.Result

trait Engine {
  def askAndPlayAction(): Result[Unit]

  def isOver: Boolean
}
