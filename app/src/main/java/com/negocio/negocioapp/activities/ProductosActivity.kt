package com.negocio.negocioapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_productos.*
import androidx.lifecycle.Observer
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.clasesentidades.Producto
import com.negocio.negocioapp.adapters.ProductosAdapter
import com.negocio.negocioapp.R

class ProductosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        // Creamos una lista vacia que almacena productos
        var listaProductos = emptyList<Producto>()

        // Declaramos la base de datos
        val database = AppDataBase.getDatabase(this)

        // Obtenemos todos los productos y por medio de un observador
        // los mostramos en un listView usando un adapter
        database.productos().getAllProducts().observe(this, Observer {
            listaProductos = it

            val adapter = ProductosAdapter(this, listaProductos)

            lista_prod.adapter = adapter

        })

        // Para cada item creamos un listener al hacer click, para enviar
        // a una nueva activity con la infomracion de ese respectivo item
        lista_prod.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, InfoProductoActivity::class.java)
            intent.putExtra("id", listaProductos[position].idProducto)
            startActivity(intent)
        }

        // agregamos un listenear de click al boton de agregar productos, para
        // enviar a una activity de creacion
        agg_producto_btn.setOnClickListener {
            val intent = Intent(this, NuevoProductoActivity::class.java)
            startActivity(intent)
        }
    }
}