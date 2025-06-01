package com.vivek.basictemplate

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vivek.basictemplate.databinding.MainActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewmodel by viewModel<MainViewModel>()
    private lateinit var bookAdapter: BookDataAdapter
    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()

        viewmodel.fetchData()

        observeViewModel()
    }

    private fun setupRecyclerView() {
        bookAdapter = BookDataAdapter()
        binding.recyclerView.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observeViewModel() {
        viewmodel.data.observe(this) {
            bookAdapter.submitList(it)
        }

        viewmodel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewmodel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                println(">> error data")
            }
        }
    }
}