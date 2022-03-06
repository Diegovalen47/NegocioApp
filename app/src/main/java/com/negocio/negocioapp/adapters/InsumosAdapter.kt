package com.negocio.negocioapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.negocio.negocioapp.R
import com.negocio.negocioapp.clasesentidades.Insumo
import kotlinx.android.synthetic.main.item_insumo.view.*

// clase adaptador de lista de Insumos para mostrar en el listView
class InsumosAdapter(private val mContext: Context, private val listaInsumos: List<Insumo>) : ArrayAdapter<Insumo>(mContext, 0, listaInsumos) {
    // Esta funcion devuelve cada uno de los items con la infomacion de cada insumo
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_insumo, parent, false)

        val insumo = listaInsumos[position]

        layout.nombre_insumo_tv.text = insumo.nombre
        layout.precio_insumo_tv.text = "$ ${insumo.precio}"
        layout.tipo_cantidad_tv.text = insumo.tipo_cantidad
        layout.cantidad_insumo_tv.text = "${insumo.cantidad}"

        return layout
    }
}