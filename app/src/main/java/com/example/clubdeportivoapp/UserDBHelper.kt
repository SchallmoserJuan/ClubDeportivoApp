package com.example.clubdeportivoapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDBHelper (context: Context): SQLiteOpenHelper(context, "ClubDB", null, 1) {

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
            db.execSQL("INSERT INTO usuarios(nombre,contrasenia) VALUES ('admin','1234','admin@gmail.com')")
            db.execSQL("INSERT INTO usuarios(nombre,contrasenia) VALUES ('admin2','12345','admin2@gmail.com')")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
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
}