package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class GenerarListadoNoSocios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generar_listado_no_socios)

        // Obtener los datos desde la base de datos
        val db = UserDBHelper(this)
        val listaNoSocios = db.obtenerNoSocios()

        // Transformar los objetos a texto legible
        val nombresNoSocios = listaNoSocios.map {
            "${it.id} - ${it.nombre} ${it.apellido}"
        }

        // Cargar la lista en el ListView
        val listView = findViewById<ListView>(R.id.listaNoSocios)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresNoSocios)
        listView.adapter = adapter

        // Botón para volver atrás
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, GenerarListadoUnoActivity::class.java)
            startActivity(intent)
            finish() // Opcional: evita que esta pantalla quede abierta
        }
    }
}
