package com.negocio.negocioapp.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.negocio.negocioapp.clasesentidades.Compra

@Dao
interface ComprasDao {
    // Ver todas las compras
    @Query("SELECT * FROM compra")
    fun getAllCompras(): LiveData<List<Compra>>
    // Obtener compra por id
    @Query("SELECT * FROM compra WHERE idCompra = :id")
    fun getCompra(id: Int): LiveData<Compra>
    // Obtener compras asociadas a un cliente
    @Query("SELECT * FROM compra WHERE idCliente = :idCliente")
    fun getComprasCliente(idCliente: Int): LiveData<List<Compra>>

    @Query("SELECT * FROM compra WHERE idCliente = :idCliente")
    fun getObjectComprasCliente(idCliente: Int): List<Compra>

    @Insert
    fun insert(vararg compras: Compra)

    @Update
    fun update(compra: Compra)

    @Delete
    fun delete(compra: Compra)
}