package com.vivek.basictemplate.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class BookRemoteDataSource(private val httpClient: HttpClient) : BookApiService {

    override suspend fun getBooks(): Result<List<BookApiModel>> {
        return try {
            val data = httpClient.get("/books").body<List<BookApiModel>>()
            Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}