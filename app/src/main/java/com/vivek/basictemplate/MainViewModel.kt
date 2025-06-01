package com.vivek.basictemplate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.basictemplate.domain.Book
import com.vivek.basictemplate.domain.BookRepository
import kotlinx.coroutines.launch

class MainViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _data = MutableLiveData<List<Book>>()
    val data: LiveData<List<Book>> = _data

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = bookRepository.getBooks()
            if (result.isSuccess) {
                _data.value = result.getOrDefault(emptyList())
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
            _isLoading.value = false
        }
    }
}