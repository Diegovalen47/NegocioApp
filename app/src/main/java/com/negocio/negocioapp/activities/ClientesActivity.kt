package com.negocio.negocioapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.R
import com.negocio.negocioapp.adapters.ClientesAdapter
import com.negocio.negocioapp.clasesentidades.Cliente
import kotlinx.android.synthetic.main.activity_clientes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Parte logica de la vista de clientes
class ClientesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        // Creamos una lista vacia que almacena clientes
        var listaClientes = emptyList<Cliente>()

        //Declaramos la base de datos
        val database = AppDataBase.getDatabase(this)

        // Obtenemos todos los clientes y por medio de un observador
        // los mostramos en un listView usando un adapter
        database.clientes().getAllClientes().observe(this, Observer {
            listaClientes = it

            for (item in listaClientes) {
                CoroutineScope(Dispatchers.IO).launch {
                    val comprasCliente = database.compras().getObjectComprasCliente(item.idCliente)
                    var suma: Double = 0.0
                    for (compra in comprasCliente) {
                        suma += compra.Total
                    }
                    item.totalCompra = suma
                    database.clientes().update(item)
                }
            }

            val adapter = ClientesAdapter(this, listaClientes)

            lista_clientes.adapter = adapter
        })

        // Para cada item creamos un listener al hacer click, para enviar
        // a una nueva activity con la infomracion de ese respectivo item
        lista_clientes.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, InfoClienteActivity::class.java)
            intent.putExtra("id", listaClientes[position].idCliente)
            startActivity(intent)
        }

        // agregamos un listenear de click al boton de agregar clientes, para
        // enviar a una activity de creacion
        agg_cliente_btn.setOnClickListener {
            val intent = Intent(this, NuevoClienteActivity::class.java)
            startActivity(intent)
        }
    }
}