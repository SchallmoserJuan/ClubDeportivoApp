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

// Clase de ejemplo
data class Persona(val nombre: String, val esSocio: Boolean)

class RegistroPagoActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDBHelper
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var nombreSeleccionado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Usar base de datos
        dbHelper = UserDBHelper(this)

        autoCompleteTextView = findViewById(R.id.autoCompletePersonas)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmarBusqueda)

        var nombreBuscado: String = ""


        // Obtener nombres completos desde la base
        val nombresPersonas = dbHelper.obtenerTodosLosNombresCompletos()

        // Adaptador para mostrar nombres
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresPersonas)
        autoCompleteTextView.setAdapter(adapter)

        // Guardar nombre seleccionado
        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            nombreSeleccionado = parent.getItemAtPosition(position) as String
        }


        // Botón confirmar
        btnConfirmar.setOnClickListener {
            val nombreBuscado = if (nombreSeleccionado.isNotBlank()) nombreSeleccionado
            else autoCompleteTextView.text.toString().trim()

            val persona = dbHelper.obtenerPersonaPorNombreCompleto(nombreBuscado)

            if (persona != null) {
                val intent = Intent(this, FormaDePagoActivity::class.java)
                intent.putExtra("nombre", "${persona.nombre} ${persona.apellido}")
                intent.putExtra("esSocio", persona.esSocio)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Persona no encontrada", Toast.LENGTH_SHORT).show()
            }
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