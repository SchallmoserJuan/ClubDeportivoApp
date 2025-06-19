package com.example.clubdeportivoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*

class FormularioClienteActivity : AppCompatActivity() {

    private val ELEGIR_IMAGEN = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario_cliente)
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

        val tipoDoc = intent.getStringExtra("tipoDocumento") ?: ""
        val numeroDoc = intent.getStringExtra("numeroDocumento") ?: ""

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etDireccion = findViewById<EditText>(R.id.etDireccion)
        val cbSocio = findViewById<CheckBox>(R.id.cbSocio)
        val cbNoSocio = findViewById<CheckBox>(R.id.cbNoSocio)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)


        // Evitar que ambos check estén marcados al mismo tiempo
        cbSocio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) cbNoSocio.isChecked = false
        }
        cbNoSocio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) cbSocio.isChecked = false
        }


        btnConfirmar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()
            val telefono = etTelefono.text.toString()
            val direccion = etDireccion.text.toString()
            val esSocio = cbSocio.isChecked
            val esNoSocio = cbNoSocio.isChecked

            if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
                Toast.makeText(this, "Todos los campos deben estar completos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!esSocio && !esNoSocio) {
                Toast.makeText(this, "Debe seleccionar si es socio o no", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = UserDBHelper(this)
            val resultado = db.registrarPersona(
                nombre, apellido, tipoDoc, numeroDoc, esSocio,
                fotoPath = imageUri?.toString()
            )

            if (resultado) {
                Toast.makeText(this, "Cliente registrado con éxito", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, FormaDePagoActivity::class.java)
                intent.putExtra("nombre", "$nombre $apellido")
                intent.putExtra("numeroDocumento", numeroDoc)
                intent.putExtra("esSocio", esSocio)  // ahora puede ser true o false
                intent.putExtra("fecha", obtenerFechaActual())
                startActivity(intent)

                finish()
            } else {
                Toast.makeText(this, "Error al registrar cliente", Toast.LENGTH_SHORT).show()
            }
        }

        val btnFoto = findViewById<Button>(R.id.btnFoto)
        btnFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, ELEGIR_IMAGEN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ELEGIR_IMAGEN && resultCode == RESULT_OK) {
            imageUri = data?.data
            Toast.makeText(this, "Imagen seleccionada correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerFechaActual(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

}