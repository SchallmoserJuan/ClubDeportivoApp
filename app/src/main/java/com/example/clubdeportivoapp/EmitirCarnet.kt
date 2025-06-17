package com.example.clubdeportivoapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EmitirCarnet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emitir_carnet)
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

        val nombre = intent.getStringExtra("nombre") ?: return
        val dbHelper = UserDBHelper(this)
        val persona = dbHelper.obtenerPersonaPorNombreCompleto(nombre)

        persona?.let {
            findViewById<TextView>(R.id.txtNombreSocio).text = "Nombre: ${it.nombre}"
            findViewById<TextView>(R.id.txtApellidoSocio).text = "Apellido: ${it.apellido}"
            findViewById<TextView>(R.id.txtTipoDoc).text = "Tipo de documento: ${it.tipoDocumento}"
            findViewById<TextView>(R.id.txtNroDoc).text = "Nro.: ${it.numeroDocumento}"
            findViewById<TextView>(R.id.txtNroSocio).text = if (it.esSocio) "Socio Activo" else "No Socio"
        }


        //Boton de Imprimir Carnet
        val btnImprimirCarnet = findViewById<Button>(R.id.btnImprimirCarnet)
        btnImprimirCarnet.setOnClickListener {
            generarCarnetPDF()
        }


    }

    private fun generarCarnetPDF() {
        val document = PdfDocument()
        val width = 500
        val height = 300
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()

        // Fondo
        paint.color = Color.parseColor("#009688")  // verde club
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // Rectángulo blanco con esquinas redondeadas (simula carnet)
        paint.color = Color.WHITE
        canvas.drawRoundRect(20f, 20f, (width - 20).toFloat(), (height - 20).toFloat(), 20f, 20f, paint)

        // Texto
        paint.color = Color.BLACK
        paint.textSize = 20f
        paint.isFakeBoldText = true
        canvas.drawText("CLUB DEPORTIVO", 40f, 60f, paint)

        paint.textSize = 16f
        val nombre = findViewById<TextView>(R.id.txtNombreSocio).text.toString()
        val apellido = findViewById<TextView>(R.id.txtApellidoSocio).text.toString()
        val tipoDoc = findViewById<TextView>(R.id.txtTipoDoc).text.toString()
        val nroDoc = findViewById<TextView>(R.id.txtNroDoc).text.toString()
        val nroSocio = findViewById<TextView>(R.id.txtNroSocio).text.toString()

        canvas.drawText(nombre, 40f, 100f, paint)
        canvas.drawText(apellido, 40f, 130f, paint)
        canvas.drawText(tipoDoc, 40f, 160f, paint)
        canvas.drawText(nroDoc, 40f, 190f, paint)
        canvas.drawText(nroSocio, 40f, 220f, paint)

        // Agregar imagen (foto del socio o logo del club)
        val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.img)
        val scaledBitmap = Bitmap.createScaledBitmap(logoBitmap, 100, 100, false)
        canvas.drawBitmap(scaledBitmap, (width - 130).toFloat(), 40f, null)

        document.finishPage(page)

        val timestamp = System.currentTimeMillis()
        val safeName = nombre.replace(" ", "_")
        val fileName = "carnet_${safeName}_$timestamp.pdf"
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        try {
            document.writeTo(FileOutputStream(file))
            Toast.makeText(this, "Carnet generado en ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al generar PDF", Toast.LENGTH_SHORT).show()
        }

        document.close()
    }


}