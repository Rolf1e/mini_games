package fr.coolnerds.minigames.domain

import scala.concurrent.Future

trait State {
  implicit def toJson: String
}

trait StateOps {
  def state: State
}

trait StateFactory {
  implicit def toState(json: String): State
}

