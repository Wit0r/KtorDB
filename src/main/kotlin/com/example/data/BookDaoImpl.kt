package com.example.data

import com.example.models.Book
import com.example.models.Books
import com.example.plugins.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class BookDaoImpl: BookDao {

    private fun resultRowToBook(row: ResultRow) = Book(
        id = row[Books.id],
        title = row[Books.title],
        author = row[Books.author],
        description = row[Books.description]
    )

    override suspend fun getAllBooks(): List<Book> = dbQuery {
        Books.selectAll().map(::resultRowToBook)
    }

    override suspend fun getBook(id: Int): Book? = dbQuery {
        Books
            .select { Books.id eq id }
            .map(::resultRowToBook)
            .singleOrNull()
    }

    override suspend fun addNewBook(title: String, author: String, description: String): Book? = dbQuery {
        val insertStatement = Books.insert {
            it[Books.title] = title
            it[Books.author] = author
            it[Books.description] = description
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToBook)
    }

    override suspend fun editBook(id: Int, title: String, author: String, description: String): Boolean = dbQuery {
        Books.update({ Books.id eq id}) {
            it[Books.title] = title
            it[Books.author] = author
            it[Books.description] = description
        } > 0
    }

    override suspend fun deleteBook(id: Int): Boolean = dbQuery {
        Books.deleteWhere { Books.id eq id } > 0
    }



}