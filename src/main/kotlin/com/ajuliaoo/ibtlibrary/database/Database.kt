package com.ajuliaoo.ibtlibrary.database

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import java.util.TimeZone

fun Application.configureDatabase() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    Database.connect(
        url = environment.config.property("ktor.database.url").getString(),
        user = environment.config.property("ktor.database.user").getString(),
        password = environment.config.property("ktor.database.password").getString()
    )
}