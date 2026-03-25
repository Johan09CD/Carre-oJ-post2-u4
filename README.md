Task Manager - Unidad 4: Modelado de Datos y Persistencia Local
Este proyecto consiste en una aplicación de gestión de tareas desarrollada en Kotlin. El objetivo principal es implementar la persistencia de datos mediante Room Database y aplicar la Migration API para evolucionar el esquema de la base de datos de la versión 1 a la versión 2 sin pérdida de información.

Arquitectura del Proyecto
La aplicación implementa el patrón de diseño MVVM (Model-View-ViewModel) y está organizada en los siguientes paquetes:

data/local: Contiene los componentes centrales de Room: TaskEntity, TaskDao, AppDatabase y el archivo de migraciones Migrations.kt.

data: Contiene el TaskRepository, que gestiona las operaciones de datos entre el DAO y la interfaz de usuario.

ui: Contiene el TaskViewModel, encargado de exponer los datos mediante StateFlow, y la MainActivity.

Proceso de Migración (V1 a V2)
Se realizó una migración para añadir un sistema de prioridades a las tareas:

Versión 1: Esquema inicial con campos para ID, título, descripción, estado de completado y fecha de creación.

Versión 2: Se actualizó la entidad para incluir el campo priority (Integer), representando niveles: 1 (baja), 2 (media) y 3 (alta).

Migration API: Se implementó el objeto MIGRATION_1_2 utilizando una sentencia SQL ALTER TABLE para agregar la nueva columna con un valor predeterminado de 1.

Johan Steven Carreño Daza
Ingeniería de Sistemas - Sexto Semestre
Universidad de Santander (UDES)
Cúcuta, Colombia