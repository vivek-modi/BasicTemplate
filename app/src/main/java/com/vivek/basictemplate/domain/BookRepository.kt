package com.vivek.basictemplate.domain

interface BookRepository {
    suspend fun getBooks(): Result<List<Book>>
}