package com.vivek.basictemplate.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorNetwork {

    companion object {
        private const val BASE_URL = "tpbookserver.herokuapp.com"
    }

    fun getKtorHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Log: $message")
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BASE_URL
                }
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            HttpResponseValidator {
                validateResponse { response: HttpResponse ->
                    when (val statusCode = response.status.value) {
                        in 400..499 -> throw ClientRequestException(
                            response,
                            "Client Error $statusCode"
                        )

                        in 500..599 -> throw ServerResponseException(
                            response,
                            "Server Error $statusCode"
                        )
                    }
                }
            }
        }
    }
}