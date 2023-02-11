package fr.coolnerds.minigames.infra.sql

trait Statement {
  def readyQuery: String
}

case class UnPreparedStatement(sql: String) extends Statement {
  override def readyQuery: String = sql
}
