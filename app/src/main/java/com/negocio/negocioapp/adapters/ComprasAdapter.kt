package com.negocio.negocioapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.negocio.negocioapp.R
import com.negocio.negocioapp.clasesentidades.Compra
import kotlinx.android.synthetic.main.item_compra.view.*


class ComprasAdapter(private val mContext: Context, private val listaCompras: List<Compra>) : ArrayAdapter<Compra>(mContext, 0, listaCompras) {
    // Esta funcion devuelve cada uno de los items con la infomacion de cada compra
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_compra, parent, false)

        val compra = listaCompras[position]

        layout.fecha_compra_tv.text = compra.fechaDeCompra
        layout.total_compra_tv.text = compra.Total.toString()

        return layout
    }
}