package com.example.data

import com.example.models.Book

interface BookDao {
    suspend fun getAllBooks(): List<Book>
    suspend fun getBook(id: Int): Book?
    suspend fun addNewBook(title: String, author: String, description: String): Book?
    suspend fun editBook(id: Int, title: String, author: String, description: String): Boolean
    suspend fun deleteBook(id: Int): Boolean

}