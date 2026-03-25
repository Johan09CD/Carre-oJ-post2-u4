package com.universidad.offlinefirst.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universidad.offlinefirst.data.PostRepository
import com.universidad.offlinefirst.data.local.PostEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostViewModel(private val repo: PostRepository) : ViewModel() {

    // Observamos los posts desde Room (Fuente única de verdad)
    val posts = repo.observePosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Al iniciar, verificamos si el caché expiró (TTL)
        viewModelScope.launch {
            repo.refreshIfStale()
        }
    }

    fun toggleFavorite(post: PostEntity) {
        viewModelScope.launch {
            repo.toggleFavorite(post)
        }
    }
}