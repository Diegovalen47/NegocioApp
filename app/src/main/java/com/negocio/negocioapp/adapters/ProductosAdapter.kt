package com.negocio.negocioapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.negocio.negocioapp.R
import com.negocio.negocioapp.clasesentidades.Producto
import kotlinx.android.synthetic.main.item_producto.view.*

// clase adaptador de lista de Productos para mostrar en el listView
class ProductosAdapter(private val mContext: Context, private val listaProductos: List<Producto>) : ArrayAdapter<Producto>(mContext, 0, listaProductos) {
    // Esta funcion devuelve cada uno de los items con la infomacion de cada Producto
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_producto, parent, false)

        val producto = listaProductos[position]

        layout.nombre_insumo_tv.text = producto.nombre
        layout.precio_insumo_tv.text = "$ ${producto.precio}"

        return layout
    }
}