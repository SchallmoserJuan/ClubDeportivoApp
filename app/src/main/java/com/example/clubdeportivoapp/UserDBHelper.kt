package com.example.clubdeportivoapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDBHelper (context: Context): SQLiteOpenHelper(context, "ClubDB", null, 4) {

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL("""
                CREATE TABLE usuarios (
                    ID Integer PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT UNIQUE,
                    contrasenia TEXT,
                    correo TEXT
                )
            """.trimIndent())
            db.execSQL("INSERT INTO usuarios(nombre,contrasenia) VALUES ('admin','1234')")
            db.execSQL("INSERT INTO usuarios(nombre,contrasenia) VALUES ('admin2','12345')")

            // Nueva tabla pagos
            db.execSQL("""
            CREATE TABLE pagos (
                ID Integer PRIMARY KEY AUTOINCREMENT,
                nroOperacion TEXT,
                fecha TEXT,
                nombre TEXT, -- corregido aquí
                monto TEXT,
                formaPago TEXT
            )
            """.trimIndent())

            db.execSQL("""
                CREATE TABLE personas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT,
                    apellido TEXT,
                    tipoDocumento TEXT,
                    numeroDocumento TEXT,
                    esSocio INTEGER -- 1 para socio, 0 para no socio
                )
            """.trimIndent())

            db.execSQL("INSERT INTO personas (nombre, apellido, tipoDocumento, numeroDocumento, esSocio) VALUES ('Juan', 'Perez', 'DNI', '12345678', 1)")
            db.execSQL("INSERT INTO personas (nombre, apellido, tipoDocumento, numeroDocumento, esSocio) VALUES ('Ana', 'Gomez', 'DNI', '87654321', 0)")
            db.execSQL("INSERT INTO personas (nombre, apellido, tipoDocumento, numeroDocumento, esSocio) VALUES ('Carlos', 'Diaz', 'DNI', '45678912', 1)")

        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        db?.execSQL("DROP TABLE IF EXISTS pagos")
        db?.execSQL("DROP TABLE IF EXISTS personas")
        onCreate(db)
    }

    //Funcion para obtener personas por nombre o otro tipo de identificador
    fun obtenerPersonaPorNombreCompleto(nombreCompleto: String): Persona? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM personas WHERE (nombre || ' ' || apellido) = ?",
            arrayOf(nombreCompleto)
        )
        var persona: Persona? = null
        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
            val tipoDoc = cursor.getString(cursor.getColumnIndexOrThrow("tipoDocumento"))
            val nroDoc = cursor.getString(cursor.getColumnIndexOrThrow("numeroDocumento"))
            val esSocio = cursor.getInt(cursor.getColumnIndexOrThrow("esSocio")) == 1

            persona = Persona(nombre, apellido, tipoDoc, nroDoc, esSocio)
        }
        cursor.close()
        return persona
    }

    fun obtenerTodosLosNombresCompletos(): List<String> {
        val lista = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nombre, apellido FROM personas", null)

        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                lista.add("$nombre $apellido")
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }




    fun login(nombre: String, contrasenia: String): Boolean {
        var db = readableDatabase
        var cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE nombre=? AND contrasenia=?",
            arrayOf(nombre, contrasenia)
        )
        var existe = cursor.count > 0
        return existe
    }

    fun usuarioExiste(input: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE nombre=?",
            arrayOf(input)
        )
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    fun insertarPago(nroOperacion: String,fecha: String, nombre: String,monto: String, formaPago: String): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nroOperacion", nroOperacion)
            put("fecha", fecha)
            put("nombre", nombre)
            put("monto", monto)
            put("formaPago", formaPago)
        }
        val resultado = db.insert("pagos", null, valores)
        return resultado != -1L
    }

    fun obtenerPagos(): List<Pago> {
        val listaPagos = mutableListOf<Pago>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM pagos", null)
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("ID"))
                val nroOperacion = getString(getColumnIndexOrThrow("nroOperacion"))
                val fecha = getString(getColumnIndexOrThrow("fecha"))
                val nombre = getString(getColumnIndexOrThrow("nombre"))
                val monto = getString(getColumnIndexOrThrow("monto"))
                val formaPago = getString(getColumnIndexOrThrow("formaPago"))

                listaPagos.add(Pago(id,nroOperacion, fecha, nombre,monto, formaPago))
            }
            close()
        }
        return listaPagos
    }

    data class Persona(
        val nombre: String,
        val apellido: String,
        val tipoDocumento: String,
        val numeroDocumento: String,
        val esSocio: Boolean
    )

    data class Pago(
        val id: Int,
        val nroOperacion: String,
        val fecha: String,
        val nombre: String,
        val monto: String,
        val formaPago: String
    )
}