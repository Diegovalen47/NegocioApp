package com.negocio.negocioapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.negocio.negocioapp.clasesentidades.*
import com.negocio.negocioapp.daos.*


// En esta parte agregar a la lista las nuevas entidades(clases) del proyecto
// Es importante incrementar la version de la base de datos cada que se modifique
@Database(
    entities = [
        Producto::class,
        Insumo::class,
        Cliente::class,
        Compra::class,
        DetalleProducto::class
        ],
    version = 17
)
abstract class AppDataBase : RoomDatabase() {

    // Añadir las funciones abstractas de cada tabla de la base de datos
    abstract fun productos() : ProductosDao
    abstract fun insumos() : InsumosDao
    abstract fun clientes() : ClientesDao
    abstract fun compras() : ComprasDao
    abstract fun detalleProductos(): DetalleProductosDao

    // Esta parte no se bien que hace, pero es un patron diseño para la base de datos
    // Para que sea creada si no existe
    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {

            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance

                return instance

            }
        }
    }
}