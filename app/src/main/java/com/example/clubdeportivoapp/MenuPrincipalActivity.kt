package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnInscribir = findViewById<Button>(R.id.btnInscribir)
        btnInscribir.setOnClickListener{
            val intent = Intent(this, InscribirNuevoClienteActivity::class.java)
            startActivity(intent)
        }

        val btnRegistrarPago = findViewById<Button>(R.id.btnPago)
        btnRegistrarPago.setOnClickListener{
            val intent = Intent(this, RegistroPagoActivity::class.java)
            startActivity(intent)
        }

        val btnGenerarListado = findViewById<Button>(R.id.btnListado)
        btnGenerarListado.setOnClickListener{
            val intent = Intent(this, GenerarListadoUnoActivity::class.java)
            startActivity(intent)
        }

        val btnEmitirCarnet = findViewById<Button>(R.id.btnCarnet)
        btnEmitirCarnet.setOnClickListener{
            val intent = Intent(this, EmitirCarnetDosActivity::class.java)
            startActivity(intent)
        }
    }
}