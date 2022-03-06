package com.negocio.negocioapp.clasesentidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// Declaramos que el objeto como una entidad
// declaramos el nombre de la tabla en la BD
@Entity(tableName = "producto")
class Producto(
    val nombre: String,
    val precio: Double,
    val descripcion: String = "",
    // Declaramos la clave primaria de la tabla producto, que se generará automaticamente
    @PrimaryKey(autoGenerate = true)
    var idProducto: Int = 0
) : Serializable