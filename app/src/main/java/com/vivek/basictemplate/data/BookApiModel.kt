package com.vivek.basictemplate.data

import kotlinx.serialization.Serializable

@Serializable
data class BookApiModel(
    val id: Int,
    val title: String,
    val isbn: String,
)
