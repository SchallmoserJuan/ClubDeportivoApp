package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class GenerarListadoVencimientos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Cargar el layout XML correspondiente a esta Activity
        setContentView(R.layout.generar_listado_vencimientos)

        //Icono para volver hacia atras
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener() {
            val intent = Intent(this,GenerarListadoUnoActivity::class.java)
            startActivity(intent)
        }
    }
}