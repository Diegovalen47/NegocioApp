package com.negocio.negocioapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.R
import com.negocio.negocioapp.clasesentidades.Compra
import kotlinx.android.synthetic.main.activity_nueva_compra.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NuevaCompraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_compra)

        // Declaramos el id de la compra para que pueda se nulo
        var idCompra : Int? = null

        // Obtenemos el id del cliente como clave foranea y referencia de la
        // compra que se va a agregar
        val idCliente = intent.getIntExtra("idCliente", 0)

        // Este condicional es en el caso que el intent se mande una compra a editar
        if (intent.hasExtra("compra")) {
            // obtenemos la compra
            val compra = intent.extras?.getSerializable("compra") as Compra

            // Obtenemos su respectivo id
            idCompra = compra.idCompra
        }

        // Obtenemos la base de datos de la app
        val database = AppDataBase.getDatabase(this)

        // Aca hay dos listener para poder capturar el cambio de fecha y poder guardarlo
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            // seteamos un listener para el click del boton de guardar compra
            save_compra.setOnClickListener {

                // Guardamos en variables los respectivos campos
                val fecha = "${dayOfMonth}/${(month + 1)}/${year}"

                // Con estos datos y el idCliente respetivo creamos la compra
                val compra = Compra(fechaDeCompra = fecha, idCliente = idCliente)

                // En caso de que el idCompra no sea nulo, se trata de una edicion ya que
                // anteriormente existia un idCompra que venia a editarse
                if (idCompra != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        compra.idCompra = idCompra

                        database.compras().update(compra)

                        this@NuevaCompraActivity.finish()
                    }
                }
                // Esto ya en caso de que sea una compra a agregar
                else {
                    CoroutineScope(Dispatchers.IO).launch {
                        database.compras().insert(compra)

                        this@NuevaCompraActivity.finish()
                    }
                }
            }
        }

    }
}