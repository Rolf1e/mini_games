package fr.coolnerds.minigames.infra.sql

case class ConnectionInfos(
    engine: String = "postgresql",
    driver: String = "org.postgresql.Driver",
    host: String,
    port: Int,
    database: String,
    username: String,
    password: String
)
