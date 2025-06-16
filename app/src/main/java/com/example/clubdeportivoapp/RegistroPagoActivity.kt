package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroPagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Icono para volver hacia atras
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener() {
            val intent = Intent(this,MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

        // Boton de confirmar pago
        // Con condicional para saber que no esta vacio
    }
}