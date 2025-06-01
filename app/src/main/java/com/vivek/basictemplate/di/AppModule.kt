package com.vivek.basictemplate.di

import com.vivek.basictemplate.data.BookApiService
import com.vivek.basictemplate.data.BookRemoteDataSource
import com.vivek.basictemplate.data.DefaultBookRepository
import com.vivek.basictemplate.data.KtorNetwork
import com.vivek.basictemplate.MainViewModel
import com.vivek.basictemplate.domain.BookRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single {
        val network = KtorNetwork()
        network.getKtorHttpClient()
    }
    singleOf(::DefaultBookRepository) bind BookRepository::class
    singleOf(::BookRemoteDataSource) bind BookApiService::class
    viewModelOf(::MainViewModel)
}