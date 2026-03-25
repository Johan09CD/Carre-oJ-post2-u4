Task Manager - Unidad 4: Caching con TTL y Arquitectura Offline-First
Este proyecto implementa un sistema de gestión de posts utilizando una estrategia de Caché con TTL (Time To Live) y arquitectura offline-first. La aplicación garantiza que el usuario siempre vea información (fuente local) y sincroniza los cambios con la API de JSONPlaceholder de manera asincrónica.

Arquitectura Implementada:
El proyecto sigue el patrón MVVM y utiliza el Repository como fuente única de verdad (Single Source of Truth):

Capa de Datos Local (Room): Almacena los posts y su estado de sincronización.

Capa de Datos Remota (Retrofit): Se conecta con la API pública para obtener y actualizar datos.

Repository: Gestiona la lógica de decisión: si el caché tiene menos de 5 minutos, sirve datos de Room; de lo contrario, refresca desde la red.

WorkManager: Garantiza que las operaciones de "favoritos" realizadas sin conexión se suban a la API cuando se recupere la conectividad.

Lógica de Sincronización
TTL (Time To Live): Se implementó un tiempo de vida del caché de 5 minutos (300,000 ms). Si el usuario abre la aplicación dentro de este rango, no se realizan peticiones de red innecesarias.

Offline-First: La UI observa un Flow proveniente de Room. Cualquier cambio de la red actualiza la base de datos y la UI reacciona automáticamente.

Sincronización Garantizada: Al marcar un post como favorito, se usa SyncFavoritesWorker con restricciones de red (NetworkType.CONNECTED) y políticas de reintento exponencial.

Johan Steven Carreño Daza Ingeniería de Sistemas - Sexto Semestre

Universidad de Santander (UDES)

Cúcuta, Colombia