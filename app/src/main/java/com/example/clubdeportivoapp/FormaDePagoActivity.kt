package com.example.clubdeportivoapp

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.Date
import java.util.Locale

class FormaDePagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forma_de_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombrecliente = intent.getStringExtra("nombre")
        val esSocio = intent.getBooleanExtra("esSocio", false)
        val numeroDocumento = intent.getStringExtra("numeroDocumento") ?: ""
        val fecha = intent.getStringExtra("fecha") ?: obtenerFechaActual()

        // Mostrar por ejemplo:
        findViewById<TextView>(R.id.tvNombre).text = "Cliente: $nombrecliente"
        findViewById<TextView>(R.id.txtTipoCliente).text = if (esSocio) "Socio" else "No Socio"


        val campoValor = findViewById<TextInputEditText>(R.id.txtMonto2)

        if (esSocio) {
            campoValor.hint = "Valor/mes: \$2000"
        } else {
            campoValor.hint = "Valor/día: \$500"
        }


        //Icono para volver hacia atras
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener() {
            val intent = Intent(this,MenuPrincipalActivity::class.java)
            startActivity(intent)
        }


        //Guardar todos los datos para luego llevarlos hacia la pantalla de comprobante
        // Dentro de FormaDePagoActivity

        val btnConfirmarPago = findViewById<Button>(R.id.btnConfirmarPago)
        btnConfirmarPago.setOnClickListener {
            val nombre = intent.getStringExtra("nombre") ?: "Cliente"
            val esSocio = intent.getBooleanExtra("esSocio", false)
            val monto = findViewById<TextInputEditText>(R.id.txtMonto2).text.toString()

            // Obtener forma de pago seleccionada
            val radioGroup = findViewById<RadioGroup>(R.id.rgFormaPago)
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            val formaPago = when (selectedRadioButtonId) {
                R.id.rbEfectivo -> "Efectivo"
                R.id.rbTarjeta -> "Tarjeta de débito/crédito"
                R.id.rbTransferencia -> "Transferencia"
                else -> "No especificado"
            }

            // Validación mínima
            if (monto.isBlank() || formaPago == "No especificado") {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Generar fecha y nro de operación
            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val nroOperacion = "OP${System.currentTimeMillis()}"

            // Insertar en la base de datos
            val dbHelper = UserDBHelper(this)
            val exito = dbHelper.insertarPago(nroOperacion, fecha, nombre, monto, formaPago)

            if (exito) {
                Toast.makeText(this, "Pago guardado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al guardar pago", Toast.LENGTH_SHORT).show()
            }

            // Crear Intent para ir a Comprobante
            val intent = Intent(this, Comprobante::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("esSocio", esSocio)
                putExtra("monto", monto)
                putExtra("formaPago", formaPago)
                putExtra("fecha", fecha)
                putExtra("nroOperacion", nroOperacion)
            }
            startActivity(intent)
        }

    }

    private fun obtenerFechaActual(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
}
