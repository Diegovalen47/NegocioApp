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
import com.negocio.negocioapp.clasesentidades.Insumo
import kotlinx.android.synthetic.main.activity_info_insumo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InfoInsumoActivity : AppCompatActivity() {

    private lateinit var database: AppDataBase
    private lateinit var insumo: Insumo
    private lateinit var insumoLiveData: LiveData<Insumo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_insumo)

        // Obtenemos la base de datos
        database = AppDataBase.getDatabase(this)

        // Por medio de un intent traemos el id del insumo que necesitamos
        // de la vista anterior (InfoProductoActivity)
        val idInsumo = intent.getIntExtra("idInsumo", 0)

        // Pasamos el insumo obtenido de la base de tados con el id
        // para pasarlo a un LiveData que es la info que se mostrará
        insumoLiveData = database.insumos().getInsumo(idInsumo)

        // Hacemos un observador para la informacion del insumo
        insumoLiveData.observe(this, Observer {
            insumo = it

            // Seteamos la infomacion que vamos a mostrar en el respectivo layout
            nombre_ins_tv.text = insumo.nombre
            tipo_cant_ins_tv.text = insumo.tipo_cantidad
            cantidad_ins_tv.text = "${insumo.cantidad}"
            precio_ins_tv.text = "$ ${insumo.precio}"
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
                val intent = Intent(this, NuevoInsumoActivity::class.java)
                intent.putExtra("insumo", insumo)
                intent.putExtra("idProducto", insumo.idProducto)
                startActivity(intent)
            }
            // En caso de borrar
            R.id.delete_item -> {
                insumoLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.insumos().delete(insumo)
                    this@InfoInsumoActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}