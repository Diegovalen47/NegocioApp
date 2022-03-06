package com.negocio.negocioapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.R
import com.negocio.negocioapp.activities.InfoCompraActivity
import com.negocio.negocioapp.clasesentidades.DetalleProducto
import com.negocio.negocioapp.clasesentidades.Producto
import kotlinx.android.synthetic.main.item_detalle_producto.view.*


class DetallesProductoAdapter(private val mContext: Context, private val listaDetalleProductos: List<DetalleProducto>) : ArrayAdapter<DetalleProducto>(mContext, 0, listaDetalleProductos) {
    // Esta funcion devuelve cada uno de los items con la infomacion de cada DetalleProducto
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_detalle_producto, parent, false)

        val detalleProducto = listaDetalleProductos[position]

        layout.nombre_detalle_prod_tv.text = detalleProducto.nombre
        layout.precio_detalle_prod_tv.text = "${detalleProducto.precio}"
        layout.cantidad_detalle_tv.text = "${detalleProducto.cantidad}"

        return layout
    }
}