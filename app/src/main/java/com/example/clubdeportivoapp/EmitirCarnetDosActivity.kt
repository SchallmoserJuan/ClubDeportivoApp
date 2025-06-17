package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class Persona2(val nombre: String, val esSocio: Boolean)

class EmitirCarnetDosActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDBHelper
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var nombreSeleccionado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emitir_carnet_dos)
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

        //Usar base de datos
        dbHelper = UserDBHelper(this)
        autoCompleteTextView = findViewById(R.id.autoCompletePersonas)
        val btnConfirmar = findViewById<Button>(R.id.button)

        btnConfirmar.isEnabled = false // Deshabilitado por defecto

        val nombresPersonas = dbHelper.obtenerTodosLosNombresCompletos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresPersonas)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            nombreSeleccionado = parent.getItemAtPosition(position) as String
            val persona = dbHelper.obtenerPersonaPorNombreCompleto(nombreSeleccionado)

            if (persona != null && persona.esSocio) {
                btnConfirmar.isEnabled = true
                btnConfirmar.setBackgroundColor(getColor(R.color.teal_700)) // o cualquier color de habilitado
            } else {
                btnConfirmar.isEnabled = false
                btnConfirmar.setBackgroundColor(getColor(R.color.darker_gray)) // o cualquier color de deshabilitado
                Toast.makeText(this, "Esta persona no es socia. No se puede emitir el carnet.", Toast.LENGTH_SHORT).show()
            }
        }

        btnConfirmar.setOnClickListener {
            val persona = dbHelper.obtenerPersonaPorNombreCompleto(nombreSeleccionado)
            if (persona != null && persona.esSocio) {
                val intent = Intent(this, EmitirCarnet::class.java)
                intent.putExtra("nombre", "${persona.nombre} ${persona.apellido}")
                intent.putExtra("esSocio", true)
                startActivity(intent)
            }
        }

    }
}