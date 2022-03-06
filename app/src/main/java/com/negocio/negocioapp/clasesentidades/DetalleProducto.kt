package com.negocio.negocioapp.clasesentidades

import androidx.room.Entity
import androidx.room.ForeignKey
import java.io.Serializable

// Declaramos que el objeto como una entidad
@Entity(
    // declaramos el nombre de la tabla en la BD
    tableName = "detalleProducto",
    // Declaramos la clave primaria compuesta
    primaryKeys = ["idCompra", "idProducto"],
    // Declaramos las claves foraneas hacia compra y hacia producto
    foreignKeys = [
        ForeignKey(
            entity = Compra::class,
            parentColumns = arrayOf("idCompra"),
            childColumns = arrayOf("idCompra"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Producto::class,
            parentColumns = arrayOf("idProducto"),
            childColumns = arrayOf("idProducto"),
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
class DetalleProducto(
    val cantidad: Int,
    val precio: Double,
    val nombre: String,
    var idCompra: Int = 0,
    var idProducto: Int = 0
) : Serializable