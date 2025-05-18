package com.example.clubdeportivoapp

import android.os.Bundle
import android.widget.Button
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GenerarListadoUnoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_generar_listado_uno)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ir a la pantalla "Socios"
        val btnSocios = findViewById<Button>(R.id.buttonSocios)
        btnSocios.setOnClickListener {
            val intent = Intent(this, GenerarListadoSocios::class.java)
            startActivity(intent)
        }

        // Ir a la pantalla "No Socios"
        val btnNoSocios = findViewById<Button>(R.id.buttonNoSocios)
        btnNoSocios.setOnClickListener {
            val intent = Intent(this, GenerarListadoNoSocios::class.java)
            startActivity(intent)
        }

        // Ir a la pantalla "Vencimientos"
        val btnVencimientos = findViewById<Button>(R.id.buttonVencimientos)
        btnVencimientos.setOnClickListener {
            val intent = Intent(this, GenerarListadoVencimientos::class.java)
            startActivity(intent)
        }
    }
}
