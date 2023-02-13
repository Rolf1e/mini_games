package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.infra.sql.{ConnectionPool, JavaEntityParser, Statement}
import fr.coolnerds.minigames.utils.Result

trait AbstractDAO[K] {
  def pool: ConnectionPool[K]

  final def executeQuery[E](
      statement: Statement
  )(implicit entityParser: JavaEntityParser[E]): Result[E] = {
    pool.acquireConnection.flatMap(_.executeQuery(statement))
  }
}
