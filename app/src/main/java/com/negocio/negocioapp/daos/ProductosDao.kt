package com.negocio.negocioapp.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.negocio.negocioapp.clasesentidades.Producto

@Dao
interface ProductosDao {
    // Ver todos los productos
    @Query("SELECT * FROM producto")
    fun getAllProducts(): LiveData<List<Producto>>
    // Obtener producto por id
    @Query("SELECT * FROM producto WHERE idProducto = :id")
    fun getProduct(id: Int): LiveData<Producto>

    @Insert
    fun insert(vararg productos: Producto)

    @Update
    fun update(producto: Producto)

    @Delete
    fun delete(producto: Producto)

    @Query("SELECT * FROM producto")
    fun getAllObjectProducts():  List<Producto>

}