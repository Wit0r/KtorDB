package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.freemarker.*

fun Application.configureFreeMarker() {
    install(FreeMarker)
}