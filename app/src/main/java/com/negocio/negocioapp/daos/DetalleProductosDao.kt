package com.negocio.negocioapp.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.negocio.negocioapp.clasesentidades.DetalleProducto

@Dao
interface DetalleProductosDao {

    // Ver todos los campos
    @Query("SELECT * FROM detalleProducto")
    fun getAllDetalleProductos(): LiveData<List<DetalleProducto>>

    // Obtener detalle producto por id compra e id producto
    @Query("SELECT * FROM detalleProducto WHERE idCompra = :idCompra AND idProducto = :idProducto")
    fun getDetalleProducto(idCompra: Int, idProducto: Int): LiveData<DetalleProducto>

    // Obtener detalles asociados a una compra
    @Query("SELECT * FROM detalleProducto WHERE idCompra = :idCompra")
    fun getDetallesProductoCompra(idCompra: Int): LiveData<List<DetalleProducto>>

    @Query("SELECT * FROM detalleProducto WHERE idCompra = :idCompra")
    fun getObjectDetallesProductoCompra(idCompra: Int): List<DetalleProducto>

    @Insert
    fun insert(vararg detalleProducto: DetalleProducto)

    @Update
    fun update(detalleProducto: DetalleProducto)

    @Delete
    fun delete(detalleProducto: DetalleProducto)
}