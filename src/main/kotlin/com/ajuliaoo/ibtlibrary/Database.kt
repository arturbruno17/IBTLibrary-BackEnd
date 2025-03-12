package com.ajuliaoo.ibtlibrary

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    Database.connect(
        url = environment.config.property("ktor.database.url").getString(),
        user = environment.config.property("ktor.database.user").getString(),
        password = environment.config.property("ktor.database.password").getString()
    )
}