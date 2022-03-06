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
import com.negocio.negocioapp.clasesentidades.DetalleProducto
import kotlinx.android.synthetic.main.activity_info_detalle_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class InfoDetalleProducto : AppCompatActivity() {

    private lateinit var database: AppDataBase
    private lateinit var detalleProducto: DetalleProducto
    private lateinit var detalleProductoLiveData: LiveData<DetalleProducto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_detalle_producto)

        // Obtenemos la base de datos
        database = AppDataBase.getDatabase(this)

        val idCompra = intent.getIntExtra("idCompra", 0)

        val idProducto = intent.getIntExtra("idProducto", 0)

        detalleProductoLiveData = database.detalleProductos().getDetalleProducto(idCompra, idProducto)

        detalleProductoLiveData.observe(this, Observer {
            detalleProducto = it

            info_nombre_prod_tv.text = detalleProducto.nombre
            info_precio_tv.text = "${detalleProducto.precio}"
            info_cantidad_tv.text = "${detalleProducto.cantidad}"


        })

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
                val intent = Intent(this, NuevoDetalleProductoActivity::class.java)
                intent.putExtra("detalleProducto", detalleProducto)
                startActivity(intent)
            }
            // En caso de borrar
            R.id.delete_item -> {
                detalleProductoLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.detalleProductos().delete(detalleProducto)
                    this@InfoDetalleProducto.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}