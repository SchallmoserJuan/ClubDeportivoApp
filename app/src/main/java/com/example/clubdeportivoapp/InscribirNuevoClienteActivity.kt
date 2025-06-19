package com.example.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast


class InscribirNuevoClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscribir_nuevo_cliente) // ¡AHORA CERRADO!

        val spinner: Spinner = findViewById(R.id.spinnerDocumento)
        val items = arrayOf("DNI", "LE", "LC", "Pasaporte", "Otro")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //Icono para volver hacia atras
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener() {
            val intent = Intent(this,MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

        // FormularioClienteActivity -> boton que lleva hacia esta activity
        // Siempre con condicional que no esten vacios los campos
        // Validar estos campos
        val btnComprobar = findViewById<Button>(R.id.btnComprobar)
        btnComprobar.setOnClickListener {
            val spinner: Spinner = findViewById(R.id.spinnerDocumento)
            val tipoDoc = spinner.selectedItem.toString()
            val numeroDoc = findViewById<EditText>(R.id.etNumero).text.toString()

            if (numeroDoc.isEmpty()) {
                Toast.makeText(this, "Debe ingresar un número de documento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = UserDBHelper(this)
            val existe = db.personaExiste(tipoDoc, numeroDoc)

            if (existe) {
                Toast.makeText(this, "El cliente ya existe. No se puede registrar de nuevo.", Toast.LENGTH_LONG).show()
                // Opcional: volver al menú principal
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, FormularioClienteActivity::class.java)
                intent.putExtra("tipoDocumento", tipoDoc)
                intent.putExtra("numeroDocumento", numeroDoc)
                startActivity(intent)
            }
        }
    }
}
