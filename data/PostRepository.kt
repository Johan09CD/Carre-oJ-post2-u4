package com.universidad.offlinefirst.data

import android.content.Context
import androidx.work.*
import com.universidad.offlinefirst.data.local.PostDao
import com.universidad.offlinefirst.data.local.PostEntity
import com.universidad.offlinefirst.data.remote.PostApiService
import com.universidad.offlinefirst.data.remote.toEntity
import com.universidad.offlinefirst.worker.SyncFavoritesWorker
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class PostRepository(
    private val dao: PostDao,
    private val api: PostApiService
) {

    companion object {
        // TTL de 5 minutos definido en la guía (5 * 60 * 1000ms)
        private const val TTL_MS = 300000L
    }

    // Fuente única de verdad: la UI siempre observa Room
    fun observePosts(): Flow<List<PostEntity>> = dao.observeAll()

    /**
     * Lógica de refresco: Solo solicita datos a la red si el caché ha expirado
     * o si la base de datos está vacía.
     */
    suspend fun refreshIfStale() {
        val oldest = dao.getOldestCacheTimestamp() ?: 0L
        val isStale = (System.currentTimeMillis() - oldest) > TTL_MS

        if (isStale) {
            try {
                val remote = api.getPosts()
                // Se actualiza Room con los nuevos datos y se marca el tiempo de caché
                dao.upsertAll(remote.map { it.toEntity() })
            } catch (e: Exception) {
                // Si no hay conexión, Room mantiene los datos previos
            }
        }
    }

    /**
     * Cambia el estado de favorito localmente y encola la sincronización
     * para cuando haya conectividad.
     */
    suspend fun toggleFavorite(post: PostEntity, context: Context) {
        // 1. Actualización inmediata en la base de datos local (UI reacciona al instante)
        dao.toggleFavorite(post.id, !post.isFavorite)

        // 2. Encolar la sincronización garantizada con WorkManager
        enqueueFavoriteSync(context)
    }

    private fun enqueueFavoriteSync(context: Context) {
        // Restricción: Solo ejecutar cuando el dispositivo tenga internet
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Configuración de la petición de trabajo único
        val request = OneTimeWorkRequestBuilder<SyncFavoritesWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15,
                TimeUnit.SECONDS
            )
            .build()

        // Encolar con política KEEP para evitar duplicados innecesarios
        WorkManager.getInstance(context).enqueueUniqueWork(
            "sync_favorites",
            ExistingWorkPolicy.KEEP,
            request
        )
    }
}