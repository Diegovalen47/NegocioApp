package com.negocio.negocioapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.negocio.negocioapp.R
import com.negocio.negocioapp.clasesentidades.Cliente
import kotlinx.android.synthetic.main.item_cliente.view.*
import kotlinx.android.synthetic.main.item_producto.view.*

// clase adaptador de lista de clientes para mostrar en el listView
class ClientesAdapter(private val mContext: Context, private val listaClientes: List<Cliente>) : ArrayAdapter<Cliente>(mContext, 0, listaClientes) {
    // Esta funcion devuelve cada uno de los items con la infomacion de cada Cliente
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_cliente, parent, false)

        val cliente = listaClientes[position]

        layout.nombre_cliente_tv.text = cliente.nombre
        layout.telefono_tv.text = cliente.telefono
        layout.total_compras_tv.text = "${cliente.totalCompra}"
        layout.debe_tv.text = "${cliente.obtenerCuantoDebe()}"

        return layout
    }
}