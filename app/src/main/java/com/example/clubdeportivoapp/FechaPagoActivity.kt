package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FechaPagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fecha_pago)

        val db = UserDBHelper(this)
        val vencidos = db.obtenerListadoVencimientos()

        if (vencidos.isNotEmpty()) {
            val nombres = vencidos.joinToString(", ") { "${it.nombre} ${it.apellido}" }
            android.widget.Toast.makeText(
                this,
                "Atención: $nombres está vencido",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }

        // Botón volver
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }
    }


}

