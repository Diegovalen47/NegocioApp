package com.negocio.negocioapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.clasesentidades.Insumo
import com.negocio.negocioapp.R
import kotlinx.android.synthetic.main.activity_nuevo_insumo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NuevoInsumoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_insumo)

        // Declaramos el id de insumo para que pueda ser nulo
        var idInsumo : Int? = null

        // Obtenermos el id del producto como clave foranea y referencia del
        // insumo que se va a agregar
        val idProducto = intent.getIntExtra("idProducto", 0)

        // Este condicional es en el caso que en el intent se mande un insumo a editar
        if (intent.hasExtra("insumo")) {
            // obtenemos el insumo
            val insumo = intent.extras?.getSerializable("insumo") as Insumo

            // Ocupamos los campos con los datos del insumo
            nombre_insumo_et.setText(insumo.nombre)
            tipo_cantidad_et.setText(insumo.tipo_cantidad)
            cantidad_ins_et.setText(insumo.cantidad.toString())
            precio_insumo_et.setText(insumo.precio.toString())
            // obtenemos su respectivo id
            idInsumo = insumo.idInsumo
        }

        // Obtenemos la base de datos de la app
        val database = AppDataBase.getDatabase(this)

        // seteamo un listener para el click del boton de guardar insumo
        save_insumo_btn.setOnClickListener {

            // Guardamos en variables los respectivos campos
            val nombre = nombre_insumo_et.text.toString()
            val tipo_cantidad = tipo_cantidad_et.text.toString()
            val cantidad = cantidad_ins_et.text.toString().toDouble()
            val precio = precio_insumo_et.text.toString().toDouble()

            // Con estos datos y el idProducto respectivo creamos el insumo
            val insumo = Insumo(nombre,tipo_cantidad, cantidad, precio, idProducto)

            // en caso de que el idInsumo no sea nulo, se trata de una edicion ya que ya esixtia
            // un idIsumo que venia a editarse
            if (idInsumo != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    insumo.idInsumo = idInsumo

                    database.insumos().update(insumo)

                    this@NuevoInsumoActivity.finish()
                }
            }
            // Esto ya en caso de que sea un Insumo nuevo a agregar
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    database.insumos().insert(insumo)

                    this@NuevoInsumoActivity.finish()
                }
            }
        }
    }
}