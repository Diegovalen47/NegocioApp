package com.negocio.negocioapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.R
import com.negocio.negocioapp.adapters.ProductosAdapter
import com.negocio.negocioapp.clasesentidades.DetalleProducto
import com.negocio.negocioapp.clasesentidades.Producto
import kotlinx.android.synthetic.main.activity_nuevo_detalle_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NuevoDetalleProductoActivity : AppCompatActivity() {

    private lateinit var database : AppDataBase
    private lateinit var producto: Producto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_detalle_producto)

        database = AppDataBase.getDatabase(this)

        var idProducto : Int? = null

        var idCompra = intent.getIntExtra("idCompra", 0)


        if (intent.hasExtra("detalleProducto")) {
            val detalleProductoEditar = intent.extras?.getSerializable("detalleProducto") as DetalleProducto

            lista_det_prod.isVisible = false

            selected_prod_tv.text = detalleProductoEditar.nombre
            cantidad_detalle_et.setText(detalleProductoEditar.cantidad.toString())
            total_prod_tv.text = "${detalleProductoEditar.precio}"

            idProducto = detalleProductoEditar.idProducto
            idCompra = detalleProductoEditar.idCompra

            database.productos().getProduct(idProducto).observe(this, Observer {
                producto = it
            })
        }



        var listaProductos = emptyList<Producto>()

        database.productos().getAllProducts().observe(this, Observer {
            listaProductos = it

            val adapter = ProductosAdapter(this, listaProductos)

            lista_det_prod.adapter = adapter
        })

        lista_det_prod.setOnItemClickListener { parent, view, position, id ->

            producto = listaProductos[position]

            selected_prod_tv.text = producto.nombre
            cantidad_detalle_et.setText("1")
            total_prod_tv.text = "${producto.precio}"


        }

        cantidad_detalle_et.setOnFocusChangeListener { view, b ->
            total_prod_tv.text = "${(cantidad_detalle_et.text.toString().toDouble())*producto.precio}"
        }

        save_detalle_btn.setOnClickListener {

            val cantidad = cantidad_detalle_et.text.toString().toInt()
            val precio = total_prod_tv.text.toString().toDouble()
            val nombre = selected_prod_tv.text.toString()
            val detalleProducto = DetalleProducto(cantidad, precio, nombre)

            if (idProducto != null) {
                CoroutineScope(Dispatchers.IO).launch {

                    detalleProducto.idProducto = idProducto
                    detalleProducto.idCompra = idCompra

                    database.detalleProductos().update(detalleProducto)

                    this@NuevoDetalleProductoActivity.finish()
                }
            }
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    detalleProducto.idProducto = producto.idProducto
                    detalleProducto.idCompra = idCompra

                    database.detalleProductos().insert(detalleProducto)

                    this@NuevoDetalleProductoActivity.finish()
                }
            }
        }



    }
}