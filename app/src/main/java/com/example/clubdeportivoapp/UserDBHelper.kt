package com.example.clubdeportivoapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log



class UserDBHelper (context: Context): SQLiteOpenHelper(context, "ClubDB", null, 6) {

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
                    esSocio INTEGER, -- 1 para socio, 0 para no socio
                    fotoPath TEXT,
                    fechaVencimiento TEXT
                )
            """.trimIndent())

            db.execSQL("INSERT INTO personas (nombre, apellido, tipoDocumento, numeroDocumento, esSocio, fechaVencimiento) VALUES ('Juan', 'Perez', 'DNI', '12345678', 1, '2025-06-20')")
            db.execSQL("INSERT INTO personas (nombre, apellido, tipoDocumento, numeroDocumento, esSocio, fechaVencimiento) VALUES ('Ana', 'Gomez', 'DNI', '87654321', 0, '2025-06-20')")
            db.execSQL("INSERT INTO personas (nombre, apellido, tipoDocumento, numeroDocumento, esSocio, fechaVencimiento) VALUES ('Carlos', 'Diaz', 'DNI', '45678912', 1, '2025-06-19')")
            db.execSQL("INSERT INTO personas (nombre, apellido, tipoDocumento, numeroDocumento, esSocio,fechaVencimiento) VALUES ('Alfonso', 'Chico', 'DNI', '36456789', 1, '2025-06-19')")

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

            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
            val tipoDoc = cursor.getString(cursor.getColumnIndexOrThrow("tipoDocumento"))
            val nroDoc = cursor.getString(cursor.getColumnIndexOrThrow("numeroDocumento"))
            val esSocio = cursor.getInt(cursor.getColumnIndexOrThrow("esSocio")) == 1
            val fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaVencimiento"))


            persona = Persona(id, nombre, apellido, tipoDoc, nroDoc, esSocio, fechaVencimiento)
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

    fun personaExiste(tipoDoc: String, numeroDoc: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM personas WHERE tipoDocumento = ? AND numeroDocumento = ?",
            arrayOf(tipoDoc, numeroDoc)
        )
        cursor.moveToFirst()
        val existe = cursor.getInt(0) > 0
        cursor.close()
        return existe
    }


    fun registrarPersona(
        nombre: String,
        apellido: String,
        tipoDocumento: String,
        numeroDocumento: String,
        esSocio: Boolean,
        fotoPath: String?
    ): Boolean {
        val db = writableDatabase

        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM personas WHERE tipoDocumento = ? AND numeroDocumento = ?",
            arrayOf(tipoDocumento, numeroDocumento)
        )

        cursor.moveToFirst()
        val existe = cursor.getInt(0) > 0
        cursor.close()

        if (existe) return false

        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("tipoDocumento", tipoDocumento)
            put("numeroDocumento", numeroDocumento)
            put("esSocio", if (esSocio) 1 else 0)
        }

        val resultado = db.insert("personas", null, valores)
        return resultado != -1L
    //obtiene los docios guardados en la base de datos
    fun obtenerSocios(): List<Persona> {
        val lista = mutableListOf<Persona>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM personas WHERE esSocio = 1", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                val tipoDoc = cursor.getString(cursor.getColumnIndexOrThrow("tipoDocumento"))
                val nroDoc = cursor.getString(cursor.getColumnIndexOrThrow("numeroDocumento"))
                val esSocio = cursor.getInt(cursor.getColumnIndexOrThrow("esSocio")) == 1
                val fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaVencimiento"))

                lista.add(Persona(id, nombre, apellido, tipoDoc, nroDoc, esSocio, fechaVencimiento))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }

    //obtiene los no socios ingresados el sistema
    fun obtenerNoSocios(): List<Persona> {
        val lista = mutableListOf<Persona>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM personas WHERE esSocio = 0", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                val tipoDoc = cursor.getString(cursor.getColumnIndexOrThrow("tipoDocumento"))
                val nroDoc = cursor.getString(cursor.getColumnIndexOrThrow("numeroDocumento"))
                val esSocio = cursor.getInt(cursor.getColumnIndexOrThrow("esSocio")) == 1
                val fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaVencimiento"))

                lista.add(Persona(id, nombre, apellido, tipoDoc, nroDoc, esSocio, fechaVencimiento))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }

        // busca la fecha de vencimiento del socio o no socio
    fun obtenerListadoVencimientos(): List<Persona> {
        val lista = mutableListOf<Persona>()
        val db = readableDatabase
        val hoy = java.time.LocalDate.now().toString() // formato "2025-06-19"

        val cursor = db.rawQuery(
            "SELECT * FROM personas WHERE fechaVencimiento IS NOT NULL AND fechaVencimiento <= ?",
            arrayOf(hoy)
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                val tipoDocumento = cursor.getString(cursor.getColumnIndexOrThrow("tipoDocumento"))
                val numeroDocumento = cursor.getString(cursor.getColumnIndexOrThrow("numeroDocumento"))
                val esSocio = cursor.getInt(cursor.getColumnIndexOrThrow("esSocio")) == 1
                val fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaVencimiento"))

                val persona = Persona(id, nombre, apellido, tipoDocumento, numeroDocumento, esSocio, fechaVencimiento)
                lista.add(persona)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }


    data class Persona(
        val id: Int,
        val nombre: String,
        val apellido: String,
        val tipoDocumento: String,
        val numeroDocumento: String,
        val esSocio: Boolean,
        val fechaVencimiento: String
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
}