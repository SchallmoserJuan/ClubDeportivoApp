package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)

        // Conexion con la base de datos
        val dbHelper = UserDBHelper(this)

        val user = findViewById<EditText>(R.id.etUser)
        val pass = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val userString = user.text.toString().trim()
            val passString = pass.text.toString().trim()

            if(dbHelper.login(userString, passString)){
                // Guardar nombre de usuario en SharedPreferences
                val prefs = getSharedPreferences("session", MODE_PRIVATE)
                prefs.edit().putString("usuario", userString).apply()

                // Ir al menú principal
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
                finish() // Opcional: evita volver al login con el botón de atrás
            } else {
                Toast.makeText(this,"Datos incorrectos", Toast.LENGTH_SHORT).show()
            }
        }






//        // Boton Login
//        val tvBotonLogin = findViewById<TextView>(R.id.btnLogin)
//
//        //Funcion de login
//        tvBotonLogin.setOnClickListener {
//            val intent = Intent(this, MenuPrincipalActivity::class.java)
//            startActivity(intent)
//        }
//
//
        // Boton de olvidar contraseña
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, RecuperarContrasenaActivity::class.java)
            startActivity(intent)
        }

    }

}
