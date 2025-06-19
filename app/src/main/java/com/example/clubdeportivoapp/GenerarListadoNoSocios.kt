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
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class GenerarListadoNoSocios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generar_listado_no_socios)

        // Obtener los datos desde la base de datos
        val db = UserDBHelper(this)
        val listaNoSocios = db.obtenerNoSocios()

        // Transformar los objetos a texto legible
        val nombresNoSocios = listaNoSocios.map {
            "${it.id} - ${it.nombre} ${it.apellido}"
        }

        // Cargar la lista en el ListView
        val listView = findViewById<ListView>(R.id.listaNoSocios)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresNoSocios)
        listView.adapter = adapter

        // Botón para volver atrás
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, GenerarListadoUnoActivity::class.java)
            startActivity(intent)
            finish() // Opcional: evita que esta pantalla quede abierta
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

            canvas.drawText("Listado de No Socios", 200f, yPosition.toFloat(), paint)
            yPosition += 30

            listaNoSocios.forEach {
                val linea = "${it.id} - ${it.nombre} ${it.apellido} - ${it.tipoDocumento}: ${it.numeroDocumento}"
                if (yPosition > 800) return@forEach // cortar si se pasa del final de página
                canvas.drawText(linea, 40f, yPosition.toFloat(), paint)
                yPosition += 20
            }

            pdfDocument.finishPage(page)

            val file = File(getExternalFilesDir(null), "listado_no_socios.pdf")
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
