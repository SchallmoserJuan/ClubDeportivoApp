package com.example.clubdeportivoapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class GenerarListadoNoSocios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargar el layout XML correspondiente a esta Activity
        setContentView(R.layout.generar_listado_no_socios)

        val iconoVolver = findViewById<ImageView>(R.id.iconoVolver)

        iconoVolver.setOnClickListener {
            finish()
        }
    }
}
