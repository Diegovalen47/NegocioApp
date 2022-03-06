package com.negocio.negocioapp.clasesentidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// Declaramos que el objeto como una entidad
// declaramos el nombre de la tabla en la BD
@Entity(tableName = "cliente")
class Cliente(
    val nombre: String,
    val telefono: String,
    var totalCompra: Double = 0.0,
    var debe: Double = 0.0,
    var abono: Double = 0.0,
    // Declaramos la clave primaria de la tabla cliente, que se generará automaticamente
    @PrimaryKey(autoGenerate = true)
    var idCliente: Int = 0
) : Serializable {

    fun obtenerCuantoDebe(): Double {
        val result = totalCompra - abono
        return if (result > 0) {
            result
        } else {
            0.0
        }
    }
}