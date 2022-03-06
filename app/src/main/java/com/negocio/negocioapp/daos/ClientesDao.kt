package com.negocio.negocioapp.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.negocio.negocioapp.clasesentidades.Cliente

@Dao
interface ClientesDao {
    // Ver todos los clientes
    @Query("SELECT * FROM cliente")
    fun getAllClientes(): LiveData<List<Cliente>>
    // Obtener cliente por id
    @Query("SELECT * FROM cliente WHERE idCliente = :id")
    fun getCliente(id: Int): LiveData<Cliente>

    @Insert
    fun insert(vararg clientes: Cliente)

    @Update
    fun update(cliente: Cliente)

    @Delete
    fun delete(cliente: Cliente)
}