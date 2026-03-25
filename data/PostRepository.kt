package com.universidad.offlinefirst.data

import com.universidad.offlinefirst.data.local.PostDao
import com.universidad.offlinefirst.data.local.PostEntity
import com.universidad.offlinefirst.data.remote.PostApiService
import com.universidad.offlinefirst.data.remote.toEntity
import kotlinx.coroutines.flow.Flow

class PostRepository(private val dao: PostDao, private val api: PostApiService) {

    companion object {
        private const val TTL_MS = 5 * 60 * 1000L // 5 minutos definidos en la guía
    }

    // La UI siempre observa Room (Fuente única de verdad) [cite: 114, 115]
    fun observePosts(): Flow<List<PostEntity>> = dao.observeAll()

    // Lógica de refresco: solo llama a la red si el cache expiró [cite: 116, 120]
    suspend fun refreshIfStale() {
        val oldest = dao.getOldestCacheTimestamp() ?: 0L
        val isStale = (System.currentTimeMillis() - oldest) > TTL_MS [cite: 118, 122, 124]

        if (isStale) {
            try {
                val remote = api.getPosts() [cite: 123]
                dao.upsertAll(remote.map { it.toEntity() }) [cite: 125]
            } catch (e: Exception) {
                // Sin conexión: Room ya tiene datos, no se hace nada [cite: 127]
            }
        }
    }

    suspend fun toggleFavorite(post: PostEntity) {
        // Actualización local inmediata
        dao.toggleFavorite(post.id, !post.isFavorite) [cite: 139]
        // La sincronización con WorkManager se añadirá en el siguiente paso
    }
}