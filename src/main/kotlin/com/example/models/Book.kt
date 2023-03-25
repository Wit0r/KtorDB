package com.example.models

import org.jetbrains.exposed.sql.Table

data class Book(val id: Int, val title: String, val author: String, val description: String)

object Books: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 58)
    val author = varchar("author", 58)
    val description = varchar("description", 58)

    override val primaryKey = PrimaryKey(id)

}