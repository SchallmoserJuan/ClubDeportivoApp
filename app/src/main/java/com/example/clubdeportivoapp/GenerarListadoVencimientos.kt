package com.example.clubdeportivoapp

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class GenerarListadoVencimientos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generar_listado_vencimientos)

        // Conexión a base de datos
        val db = UserDBHelper(this)
        val listaVencimientos = db.obtenerListadoVencimientos() // ← Este método ya existe en tu DBHelper

        // Convertimos los datos a texto legible para la lista
        val vencimientosString = listaVencimientos.map {
            "${it.id} - ${it.nombre} ${it.apellido} - ${it.fechaVencimiento}"
        }

        // Mostramos los datos en el ListView
        val listView = findViewById<ListView>(R.id.listaVencimientos)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vencimientosString)
        listView.adapter = adapter

        // Botón para volver atrás
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, GenerarListadoUnoActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Boton para descargar listado
        val btnDescargar = findViewById<Button>(R.id.btnDescargarListado)
        btnDescargar.setOnClickListener {
            val pdfDocument = PdfDocument()
            val paint = Paint()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
            val page = pdfDocument.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            var yPosition = 50
            paint.textSize = 14f

            canvas.drawText("Listado de Vencimientos", 200f, yPosition.toFloat(), paint)
            yPosition += 30

            listaVencimientos.forEach {
                val linea = "${it.id} - ${it.nombre} ${it.apellido} - ${it.tipoDocumento}: ${it.numeroDocumento}, ${it.fechaVencimiento}"
                if (yPosition > 800) return@forEach // cortar si se pasa del final de página
                canvas.drawText(linea, 40f, yPosition.toFloat(), paint)
                yPosition += 20
            }

            pdfDocument.finishPage(page)

            val file = File(getExternalFilesDir(null), "listado_vencimientos.pdf")
            try {
                pdfDocument.writeTo(FileOutputStream(file))
                Toast.makeText(this, "PDF guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al guardar PDF", Toast.LENGTH_SHORT).show()
            }

            pdfDocument.close()
        }
    }
}
