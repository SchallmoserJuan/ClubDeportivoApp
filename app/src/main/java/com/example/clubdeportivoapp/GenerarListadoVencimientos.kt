package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class GenerarListadoVencimientos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generar_listado_vencimientos)

        // Conexión a base de datos
        val db = UserDBHelper(this)
        val listaVencimientos = db.obtenerListadoVencimientos() // ← Este método ya existe en tu DBHelper

        // Convertimos los datos a texto legible para la lista
        val vencimientosString = listaVencimientos.map {
            "${it.id} - ${it.nombre} ${it.apellido} - Vence: ${it.fechaVencimiento}"
        }

        // Mostramos los datos en el ListView
        val listView = findViewById<ListView>(R.id.listaVencimientos)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vencimientosString)
        listView.adapter = adapter

        // Botón para volver atrás
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, GenerarListadoUnoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
