package com.vivek.basictemplate.data

import com.vivek.basictemplate.domain.Book
import com.vivek.basictemplate.domain.BookRepository

class DefaultBookRepository(private val bookApiService: BookApiService) : BookRepository {

    override suspend fun getBooks(): Result<List<Book>> {
        return bookApiService.getBooks().map { bookApiModels->
            bookApiModels.map {
                it.toBook()
            }
        }
    }
}

fun BookApiModel.toBook(): Book {
    return Book(
        id = id,
        title = title,
    )
}