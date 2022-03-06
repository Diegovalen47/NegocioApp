package com.negocio.negocioapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.negocio.negocioapp.AppDataBase
import com.negocio.negocioapp.clasesentidades.Producto
import com.negocio.negocioapp.R
import kotlinx.android.synthetic.main.activity_nuevo_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NuevoProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_producto)

        // Declaramos el idProducto para que pueda ser nulo
        var idProducto: Int? = null

        // Este condicional es en el caso que en el intent se mande un producto a editar
        if (intent.hasExtra("producto")) {
            // Obtenemos el producto
            val producto = intent.extras?.getSerializable("producto") as Producto

            // Ocupamos los campos con los datos del producto
            nombre_prod_et.setText(producto.nombre)
            precio_prod_et.setText(producto.precio.toString())
            descripcion_prod_et.setText(producto.descripcion)
            // Obtenemos su respectivo id
            idProducto = producto.idProducto
        }

        // Obtenemos la base de datos de la app
        val database = AppDataBase.getDatabase(this)

        // seteamos un listener para el click del boton de guardar producto
        save_prod_btn.setOnClickListener {
            // Guardamos en variables los respectivos campos
            val nombre = nombre_prod_et.text.toString()
            val precio = precio_prod_et.text.toString().toDouble()
            val descripcion = descripcion_prod_et.text.toString()

            // Con estos datos creamos el producto
            val producto = Producto(nombre, precio, descripcion)

            // en caso de que el idProducto no sea nulo, se trata de una edicion ya que ya esixtia
            // un idProducto que venia a editarse
            if (idProducto != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    producto.idProducto = idProducto

                    database.productos().update(producto)

                    this@NuevoProductoActivity.finish()
                }
            }
            // Esto ya en caso de que sea un Producto nuevo a agregar
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    database.productos().insert(producto)

                    this@NuevoProductoActivity.finish()
                }
            }


        }

    }
}