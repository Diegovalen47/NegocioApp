package com.negocio.negocioapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.negocio.negocioapp.*
import com.negocio.negocioapp.adapters.InsumosAdapter
import com.negocio.negocioapp.clasesentidades.Insumo
import com.negocio.negocioapp.clasesentidades.Producto
import kotlinx.android.synthetic.main.activity_info_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Parte lógica de la vista de infromacion de un producto
class InfoProductoActivity : AppCompatActivity() {


    private lateinit var database: AppDataBase
    private lateinit var producto: Producto
    private lateinit var productoLiveData: LiveData<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_producto)

        // Obtenemos la base de datos
        database = AppDataBase.getDatabase(this)

        // Por medio de un intent traemos el id del producto que necesitamos
        // de la vista anterior (ProductosActivity)
        val idProducto = intent.getIntExtra("id", 0)

        // Pasamos el producto obtenido de la base de tados con el id
        // para pasarlo a un LiveData que es la info que se mostrará
        productoLiveData = database.productos().getProduct(idProducto)

        // Hacemos un observador para la informacion del producto
        productoLiveData.observe(this, Observer {
            producto = it

            // Seteamos la infomacion que vamos a mostrar en el respectivo layout
            nombre_prod.text = producto.nombre
            precio_prod.text = "$ ${producto.precio}"
            descripcion_prod.text = producto.descripcion

        })

        // definimos la lista donde guardaremos los insumos del respectivo producto visulizado
        var listaInsumos = emptyList<Insumo>()

        // Obtenemos los insumos de el producto respectivo por medio del id
        // ademas agregamos un observador para mostrar estos por pantalla en un listView
        database.insumos().getInsumosProducto(idProducto).observe(this, Observer {
            listaInsumos = it

            // Pasamos esta lista Insumos adapter
            val adapter = InsumosAdapter(this, listaInsumos)

            lista_insumos.adapter = adapter
        })

        // Agregamos un listener a cada item de la lista de insumos
        lista_insumos.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, InfoInsumoActivity::class.java)
            intent.putExtra("idInsumo", listaInsumos[position].idInsumo)
            intent.putExtra("idProducto", idProducto)
            startActivity(intent)
        }

        // Al boton para agregar insumos agregamos un listener para cuando sea predionado
        agg_ingrediente_btn.setOnClickListener {
            // Creamos un intent para enviarinfomracion a la siguiente vista
            val intent = Intent(this, NuevoInsumoActivity::class.java)
            // El intent tambien debe llevar el id del producto para poder saber a cual pertenece
            // en la base de datos (ya que hace la funcion de clave foranea)
            intent.putExtra("idProducto", idProducto)
            // Empezamos la actividad respectiva al intent, donde agregamos los datos
            // del nuevo insumo
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
                val intent = Intent(this, NuevoProductoActivity::class.java)
                intent.putExtra("producto", producto)
                startActivity(intent)
            }
            // En caso de borrar
            R.id.delete_item -> {
                productoLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.productos().delete(producto)
                    this@InfoProductoActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

}