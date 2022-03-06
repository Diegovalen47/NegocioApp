package com.negocio.negocioapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.R
import com.negocio.negocioapp.clasesentidades.Cliente
import kotlinx.android.synthetic.main.activity_nuevo_cliente.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NuevoClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_cliente)

        // Declaramos el idCliente para que pueda ser nulo
        var idCliente: Int? = null

        // Este condicional es en el caso que en el intent se mande un cliente a editar
        if (intent.hasExtra("cliente")) {
            // Obtenemos el cliente
            val cliente = intent.extras?.getSerializable("cliente") as Cliente

            // Ocupamos los campos con los datos del cliente
            nombre_cliente_et.setText(cliente.nombre)
            telefono_et.setText(cliente.telefono)
            // Obtenemos su respectivo id
            idCliente = cliente.idCliente
        }

        // Obtenemos la base de datos de la app
        val database = AppDataBase.getDatabase(this)

        // seteamos un listener para el click del boton de guardar cliente
        save_cliente_btn.setOnClickListener {
            // Guardamos en variables los respectivos campos
            val nombre = nombre_cliente_et.text.toString()
            val telefono = telefono_et.text.toString()

            // Con estos datos creamos el cliente
            val cliente = Cliente(nombre, telefono)

            // En caso de que el idCliente sea nulo, se trata de una edicion ya que anteriormente
            // existia un idCliente que venia a editarse
            if (idCliente != null) {
               CoroutineScope(Dispatchers.IO).launch {
                   cliente.idCliente = idCliente

                   database.clientes().update(cliente)

                   this@NuevoClienteActivity.finish()
               }
            }
            // Esto ya es en case de ser un Cliente a agragar
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    database.clientes().insert(cliente)

                    this@NuevoClienteActivity.finish()
                }
            }
        }
    }
}