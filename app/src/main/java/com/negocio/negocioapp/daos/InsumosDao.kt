package com.negocio.negocioapp.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.negocio.negocioapp.clasesentidades.Insumo

@Dao
interface InsumosDao {
    // Ver todos los insumos
    @Query("SELECT * FROM insumo")
    fun getAllInsumos(): LiveData<List<Insumo>>
    // Obtener insumo por id
    @Query("SELECT * FROM insumo WHERE idInsumo = :id")
    fun getInsumo(id: Int): LiveData<Insumo>
    // Obtener insumos asociados a un determinado producto
    @Query("SELECT * FROM insumo WHERE idProducto = :idProducto")
    fun getInsumosProducto(idProducto: Int): LiveData<List<Insumo>>

    @Insert
    fun insert(vararg insumos: Insumo)

    @Update
    fun update(insumo: Insumo)

    @Delete
    fun delete(insumo: Insumo)
}