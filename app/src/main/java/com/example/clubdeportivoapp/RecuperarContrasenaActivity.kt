package com.example.clubdeportivoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecuperarContrasenaActivity : AppCompatActivity() {
    private lateinit var dbHelper: UserDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contrasena)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = UserDBHelper(this)

        val usuarioInput = findViewById<EditText>(R.id.usuarioInput)
        val botonInput = findViewById<Button>(R.id.btnRecuperar)

        botonInput.setOnClickListener {
            val input = usuarioInput.text.toString()

            if (input.isNotEmpty()) {
                if (dbHelper.usuarioExiste(input)) {
                    enviarCorreo(input)
                } else {
                    Toast.makeText(this, "Usuario o correo no encontrado", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        //Icono para volver hacia atras
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener() {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

        private fun enviarCorreo(destino: String) {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("soporte@clubdeportivo.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Recuperación de contraseña")
                putExtra(Intent.EXTRA_TEXT, "Hola, olvidé mi contraseña. Mi correo o usuario es: $destino")
            }
            startActivity(Intent.createChooser(intent, "Enviar correo con..."))
        }
}