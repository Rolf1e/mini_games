package fr.coolnerds.minigames.domain

trait State extends StatePersistenceOps {}

trait StateOps {
  def state: State
}

trait StatePersistenceOps {
  implicit def toJson: String
}

trait StateFactory {
  implicit def toState(json: String): State
}
