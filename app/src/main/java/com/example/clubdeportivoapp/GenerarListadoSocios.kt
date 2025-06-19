package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class GenerarListadoSocios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generar_listado_socios)

        // Cargar los datos desde la base de datos
        val db = UserDBHelper(this)
        val listaSocios = db.obtenerSocios()

        // Convertimos la lista de objetos a strings legibles
        val nombresSocios = listaSocios.map {
            "${it.id} - ${it.nombre} ${it.apellido} - ${it.fechaVencimiento} "
        }

        // Asignamos el adaptador al ListView
        val listView = findViewById<ListView>(R.id.listaSocios)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresSocios)
        listView.adapter = adapter

        // Configuramos el botón para volver atrás
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, GenerarListadoUnoActivity::class.java)
            startActivity(intent)
            finish() // Opcional: para cerrar esta activity y evitar que quede en el back stack
        }
    }
}
