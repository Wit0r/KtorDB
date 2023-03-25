package com.example.plugins

import com.example.data.BookDao
import com.example.data.BookDaoImpl
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.coroutines.runBlocking

fun Application.configureRouting() {

    val dao: BookDao = BookDaoImpl().apply {
        runBlocking {
            if(getAllBooks().isEmpty()) {
                addNewBook(
                    title = "The World as Will and Representation",
                    author = "Arthur Schopenhauer",
                    description = ""
                )
            }
        }
    }

    routing {
        route("/books") {
            get {
                call.respond(FreeMarkerContent("index.ftl", mapOf("books" to dao.getAllBooks())))
            }
            post {
                val formParameters = call.receiveParameters()
                val title = formParameters.getOrFail("title")
                val author = formParameters.getOrFail("author")
                val description = formParameters.getOrFail("description")
                val book = dao.addNewBook(title, author, description)
                call.respondRedirect("/books/${book?.id}")
            }
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respond(FreeMarkerContent("show.ftl", mapOf("book" to dao.getBook(id))))
            }
            get("{id}/edit") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respond(FreeMarkerContent("edit.ftl", mapOf("book" to dao.getBook(id))))
            }
            post("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val formParameters = call.receiveParameters()
                when (formParameters.getOrFail("_action")) {
                    "update" -> {
                        val title = formParameters.getOrFail("title")
                        val author = formParameters.getOrFail("author")
                        val description = formParameters.getOrFail("description")
                        dao.editBook(id, title, author, description)
                        call.respondRedirect("/books/$id")
                    }
                    "delete" -> {
                        dao.deleteBook(id)
                        call.respondRedirect("/books")
                    }
                }
            }
        }
    }
}
