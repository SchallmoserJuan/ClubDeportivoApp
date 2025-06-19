package com.example.clubdeportivoapp

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Comprobante : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_comprobante)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val nombre = intent.getStringExtra("nombre") ?: "Cliente"
        val esSocio = intent.getBooleanExtra("esSocio", false)
        val monto = intent.getStringExtra("monto") ?: "0"
        val formaPago = intent.getStringExtra("formaPago") ?: "No especificado"
        val fecha = intent.getStringExtra("fecha") ?: "No disponible"
        val nroOperacion = intent.getStringExtra("nroOperacion") ?: "No disponible"

        findViewById<TextView>(R.id.txtCliente).text = "Cliente: $nombre (${if (esSocio) "Socio" else "No Socio"})"
        findViewById<TextView>(R.id.txtMonto).text = "Monto: $monto"
        findViewById<TextView>(R.id.txtFormaPagoElegida).text = "Forma de pago: $formaPago"
        findViewById<TextView>(R.id.txtFecha).text = "Fecha: $fecha"
        findViewById<TextView>(R.id.txtNroOperacion).text = "Nro. de operación: $nroOperacion"

        // Configurar botón volver si es que tenés uno, y otros botones (ej. imprimir)
        findViewById<ImageView>(R.id.iconoVolver).setOnClickListener {
            finish() // vuelve a la pantalla anterior
        }


        //Boton de imprimir comprobante
        val btnImprimir = findViewById<Button>(R.id.btnImprimirComprobante)
        btnImprimir.setOnClickListener {
            generarPdf()
        }

        //Boton de emitirCarnet

        val btnEmitirCarnet = findViewById<Button>(R.id.btnEmitirCarnet)

        // Mostrar o esconder el botón según si es socio o no
        if (esSocio) {
            btnEmitirCarnet.isEnabled = true
            btnEmitirCarnet.visibility = android.view.View.VISIBLE
        } else {
            btnEmitirCarnet.isEnabled = false
            btnEmitirCarnet.visibility = android.view.View.GONE
        }

        // Listener para emitir carnet solo si está habilitado (opcional, pero recomendable)
        btnEmitirCarnet.setOnClickListener {
            if (esSocio) {
                val intent = Intent(this, EmitirCarnet::class.java)
                intent.putExtra("nombre", nombre)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Solo socios pueden emitir carnet", Toast.LENGTH_SHORT).show()
            }
        }

        //Icono para volver hacia atras
        val btnVolver = findViewById<ImageView>(R.id.iconoVolver)
        btnVolver.setOnClickListener() {
            val intent = Intent(this,MenuPrincipalActivity::class.java)
            startActivity(intent)
        }


    }

    private fun generarPdf() {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint()
        paint.isAntiAlias = true

        // Título centrado
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Club Deportivo", 150f, 40f, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = false
        canvas.drawText("Comprobante de Pago", 150f, 65f, paint)

        // Línea separadora
        paint.strokeWidth = 1f
        canvas.drawLine(20f, 80f, 280f, 80f, paint)

        // Texto alineado a la izquierda
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 12f
        var y = 100f

        val nombre = findViewById<TextView>(R.id.txtCliente).text.toString()
        val monto = findViewById<TextView>(R.id.txtMonto).text.toString()
        val formaPago = findViewById<TextView>(R.id.txtFormaPagoElegida).text.toString()
        val fecha = findViewById<TextView>(R.id.txtFecha).text.toString()
        val nroOperacion = findViewById<TextView>(R.id.txtNroOperacion).text.toString()

        canvas.drawText(nombre, 20f, y, paint); y += 25
        canvas.drawText(monto, 20f, y, paint); y += 25
        canvas.drawText(formaPago, 20f, y, paint); y += 25
        canvas.drawText(fecha, 20f, y, paint); y += 25
        canvas.drawText(nroOperacion, 20f, y, paint); y += 40

        // Línea separadora
        canvas.drawLine(20f, y, 280f, y, paint); y += 25

        // Pie de página
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 10f
        canvas.drawText("Gracias por su pago", 150f, y, paint)

        document.finishPage(page)

        val timestamp = System.currentTimeMillis()
        val fileName = "comprobante_pago_$timestamp.pdf"
        val folder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(folder, fileName)

        try {
            document.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF generado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al generar PDF", Toast.LENGTH_SHORT).show()
        }

        document.close()
    }

}