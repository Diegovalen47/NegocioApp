package com.negocio.negocioapp.clasesentidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

// Declaramos que el objeto como una entidad
@Entity(
    // declaramos el nombre de la tabla en la BD
    tableName = "compra",
    // Declaramos las clave foranea hacia Cliente
    foreignKeys = [ForeignKey(
        entity = Cliente::class,
        parentColumns = arrayOf("idCliente"),//clave primaria cliente
        childColumns = arrayOf("idCliente"),//clave foranea en compra
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
class Compra(
    val fechaDeCompra: String,
    var idCliente: Int,
    var Total: Double = 0.0,
    // Declaramos la clave primaria de la tabla compra, que se generará automaticamente
    @PrimaryKey(autoGenerate = true)
    var idCompra: Int = 0
) : Serializable