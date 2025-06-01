package com.vivek.basictemplate.data

interface BookApiService {
    suspend fun getBooks(): Result<List<BookApiModel>>
}