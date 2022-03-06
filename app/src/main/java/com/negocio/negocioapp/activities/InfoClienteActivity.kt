package com.negocio.negocioapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.R
import com.negocio.negocioapp.adapters.ComprasAdapter
import com.negocio.negocioapp.clasesentidades.Cliente
import com.negocio.negocioapp.clasesentidades.Compra
import kotlinx.android.synthetic.main.activity_info_cliente.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InfoClienteActivity : AppCompatActivity() {

    private lateinit var database: AppDataBase
    private lateinit var cliente: Cliente
    private lateinit var clienteLiveData: LiveData<Cliente>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_cliente)

        // Obtenemos la base de datos
        database = AppDataBase.getDatabase(this)

        // Por medio de un intent traemos el id del cliente que necesitamos
        // de la vista anterior (ClientesActivity)
        val idCliente = intent.getIntExtra("id", 0)

        // Pasamos el liente obtenido de la base de tados con el id
        // para pasarlo a un LiveData que es la info que se mostrará
        clienteLiveData = database.clientes().getCliente(idCliente)

        // Observador para la informacion del cliente
        clienteLiveData.observe(this, Observer {
            cliente = it

            // Seteamos la infomacion que vamos a mostrar en el respectivo layout
            info_nombre_tv.text = cliente.nombre
            info_telefono_tv.text = cliente.telefono
            info_total_compras_tv.text = "${cliente.totalCompra}"
            info_abonado_tv.text = "${cliente.abono}"
            info_debe_tv.text = "${cliente.obtenerCuantoDebe()}"

        })

        // Defininos la lista donde guardaremos las compras del respectivo cliente visualizado
        var listaCompras = emptyList<Compra>()

        // Obtenemos las compras de el cliente respectivo por medio del id
        // ademas agregamos un observador para mostrar estos por pantalla en un listView
        database.compras().getComprasCliente(idCliente).observe(this, Observer {
            listaCompras = it

            for (item in listaCompras) {
                CoroutineScope(Dispatchers.IO).launch {
                    val detalles = database.detalleProductos().getObjectDetallesProductoCompra(item.idCompra)
                    var suma: Double = 0.0
                    for (detalle in detalles) {
                        suma += detalle.precio
                    }
                    item.Total = suma
                    database.compras().update(item)
                }
            }

            // Pasamos esta lista compras adapter
            val adapter = ComprasAdapter(this, listaCompras)

            lista_compras.adapter = adapter
        })

        // Agregamos un listener a cada item de la lista de compras
        lista_compras.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, InfoCompraActivity::class.java)
            intent.putExtra("idCompra", listaCompras[position].idCompra)
            intent.putExtra("idCliente", idCliente)
            startActivity(intent)
        }

        // Al boton para agregar compras agregamos un listener para cuando sea presionado
        agg_compra_btn.setOnClickListener {
            // Creamos un intent para enviar infomracion a la siguiente vista
            val intent = Intent(this, NuevaCompraActivity::class.java)
            // El intent tambien debe llevar el id del cliente para poder saber a cual pertenece
            // en la base de datos (ya que hace la funcion de clave foranea)
            intent.putExtra("idCliente", idCliente)
            // Empezamos la actividad respectiva al intent, donde agregamos los datos
            // del nuevo insumo
            startActivity(intent)
        }

        // Listener para el boton abonar
        abonar_btn.setOnClickListener {
            val cantidad = cantidad_abonar_et.text.toString()

            //check if the EditText have values or not
            if(cantidad.trim().isNotEmpty()) {

                CoroutineScope(Dispatchers.IO).launch {
                    val abonoNuevo = cliente.abono + cantidad.toDouble()
                    cliente.abono = abonoNuevo
                    database.clientes().update(cliente)
                }

                Toast.makeText(applicationContext, "Cantidad abonada: $cantidad", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(applicationContext, "Por favor ingrese algun valor ", Toast.LENGTH_SHORT).show()
            }
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
                val intent = Intent(this, NuevoClienteActivity::class.java)
                intent.putExtra("cliente", cliente)
                startActivity(intent)
            }
            // En caso de borrar
            R.id.delete_item -> {
                clienteLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.clientes().delete(cliente)
                    this@InfoClienteActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}