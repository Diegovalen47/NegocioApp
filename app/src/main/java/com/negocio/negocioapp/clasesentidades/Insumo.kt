package com.negocio.negocioapp.clasesentidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
// Declaramos que el objeto como una entidad
@Entity(
    // declaramos el nombre de la tabla en la BD
    tableName = "insumo",
    // Declaramos las clave foranea hacia Producto
    foreignKeys = [ForeignKey(
        entity = Producto::class,
        parentColumns = arrayOf("idProducto"),//clave primaria producto
        childColumns = arrayOf("idProducto"),//clave foranea en insumo
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
class Insumo(
    val nombre: String,
    val tipo_cantidad: String,
    val cantidad: Double,
    val precio: Double,
    var idProducto: Int,
    // Declaramos la clave primaria de la tabla insumo, que se generará automaticamente
    @PrimaryKey(autoGenerate = true)
    var idInsumo: Int = 0
) : Serializable