package com.negocio.negocioapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.R
import com.negocio.negocioapp.adapters.DetallesProductoAdapter
import com.negocio.negocioapp.clasesentidades.Compra
import com.negocio.negocioapp.clasesentidades.DetalleProducto
import kotlinx.android.synthetic.main.activity_info_compra.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class InfoCompraActivity : AppCompatActivity() {

    private lateinit var database: AppDataBase
    private lateinit var compra: Compra
    private lateinit var compraLiveData: LiveData<Compra>
    private var idCliente by Delegates.notNull<Int>()
    private lateinit var listaDetalleProductos: List<DetalleProducto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_compra)

        // Obtenemos la base de datos
        database = AppDataBase.getDatabase(this)

        // Por medio de un intent traemos el id de la compra
        // que necesitamos de la vista anterior (InfoClienteActivity)

        val idCompra = intent.getIntExtra("idCompra", 0)

        idCliente = intent.getIntExtra("idCliente",0)

        // Pasamos la compra obtenida de la base de datos on el id
        // para guardarla en un LiveData que es la info que se
        // mostrará
        compraLiveData = database.compras().getCompra(idCompra)

        // Observador par la información de la compra
        compraLiveData.observe(this, Observer {
            compra = it

            // Seteamos la infomacion que vamos a mostrar en el respectivo layout
            info_fecha_tv.text = compra.fechaDeCompra
            info_total_tv.text = compra.Total.toString()

        })

        // Definimos la lista donde guardaremos los detalle_productos de la  respectiva
        // compra visualizada
        listaDetalleProductos = emptyList<DetalleProducto>()

        // Obtenemos los detalles de productos de la respetiva compra
        // ya gregamos un observador
        database.detalleProductos().getDetallesProductoCompra(idCompra).observe(this, Observer {
            listaDetalleProductos = it

            val adapter = DetallesProductoAdapter(this, listaDetalleProductos)

            lista_detalle_productos.adapter = adapter


        })



        // Agregamos un listener a cada item de la lista de detalles
        lista_detalle_productos.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, InfoDetalleProducto::class.java)
            intent.putExtra("idCompra", idCompra)
            intent.putExtra("idProducto", listaDetalleProductos[position].idProducto)
            startActivity(intent)
        }

        agg_detalle_prod_btn.setOnClickListener {

            val intent = Intent(this, NuevoDetalleProductoActivity::class.java)
            intent.putExtra("idCompra", idCompra)
            startActivity(intent)
        }



    }

    // Esta funcion es para cargar los botones de editar/borrar en el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.producto_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    // Funcion para gestionar los botones seleccionados del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // En caso de editar
            R.id.edit_item -> {
                val intent = Intent(this, NuevaCompraActivity::class.java)
                intent.putExtra("compra", compra)
                intent.putExtra("idCliente", idCliente)
                startActivity(intent)
            }
            // En caso de borrar
            R.id.delete_item -> {
                compraLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.compras().delete(compra)
                    this@InfoCompraActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

}