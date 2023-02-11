package fr.coolnerds.minigames.infra.sql

case class ConnectionInfos(
    engine: String,
    host: String,
    port: Int,
    database: String,
    username: String,
    password: String
)
