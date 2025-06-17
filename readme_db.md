````markdown
# 📘 Guía para trabajar con la base de datos (SQLite) - ClubDeportivoApp

Esta guía explica cómo manejar correctamente los cambios en la base de datos SQLite para evitar errores, conflictos y pérdida de datos cuando trabajamos en equipo.

---

## 📦 Archivo clave: `UserDBHelper.kt`

Es la clase encargada de:
- Crear las tablas cuando se instala la app (`onCreate()`).
- Actualizar la estructura si cambia una versión (`onUpgrade()`).

⚠️ **No modifiques directamente la versión ni el esquema en `main`** sin pasar por tu propia rama.

---

## 🧠 Principios clave

1. 🔀 **Cada funcionalidad nueva va en su propia rama**.
2. 📈 **Cada cambio estructural en la base (crear o modificar una tabla) requiere aumentar la versión (`DB_VERSION`)**.
3. 🧪 **Antes de hacer merge, se revisa que `onUpgrade()` incluya todos los cambios combinados**.
4. 📝 **Documentá tus cambios en `README_DB.md` o `docs/README_DB.md`**.

---

## ⚙️ Cómo agregar una tabla nueva correctamente

1. **Aumentá la versión en la constante de la clase:**

```kotlin
const val DB_VERSION = 3 // o el número siguiente al actual
````

2. **Agregá tu tabla en `onCreate()` para instalaciones nuevas:**

```kotlin
db?.execSQL("""
    CREATE TABLE actividades (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        titulo TEXT,
        fecha TEXT
    )
""".trimIndent())
```

3. **Agregá la tabla en `onUpgrade()` para que se cree al actualizar la app:**

```kotlin
if (oldVersion < 3) {
    db?.execSQL("""
        CREATE TABLE actividades (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            titulo TEXT,
            fecha TEXT
        )
    """.trimIndent())
}
```

4. **Documentá tu cambio en `Cambios_DB.md`:**

    * Indicá la nueva versión.
    * Qué tabla agregaste.
    * Tu nombre (para rastreo).

---

## 🛡️ ¿Qué pasa si usamos el mismo número de versión?

‼️ **Problema:** Si dos personas usan `DB_VERSION = 3` para dos tablas diferentes, el último merge puede sobreescribir el bloque `if (oldVersion < 3)`, y solo una tabla se creará.

### ✅ Solución 1: Reservar versiones

* Avisá por el grupo o en un `CHANGELOG.md` que vos vas a usar la versión 4, otro la 5, etc.

### ✅ Solución 2: Merge controlado

* Todos trabajan con la misma versión base.
* Al hacer merge, se combinan todos los bloques de `onUpgrade()` en orden:

```

---

## 🧰 Herramientas útiles

* 🗃 `context.deleteDatabase("ClubDB")`: para resetear base en pruebas.
* 🧪 `adb shell pm clear com.example.clubdeportivoapp`: borra datos de la app desde terminal.
* 📂 `README_DB.md`: mantené ahí el historial de cambios y tablas.

---

## 🧼 Buenas prácticas

| Recomendación                                    | Por qué                                   |
| ------------------------------------------------ | ----------------------------------------- |
| 🔄 Cerrar cursores (`cursor.close()`)            | Evitás fugas de memoria                   |
| 🧩 Una tabla = una responsabilidad               | Mantené tu código modular                 |
| 🔢 Siempre subí la versión si cambiás estructura | Para que `onUpgrade()` se ejecute         |
| 👥 No borres tablas sin consultar                | Podés romper funcionalidad de otro        |
| 📝 Documentá tu cambio                           | Para no perder el seguimiento del esquema |

---

## 🧪 Antes de mergear a `main`...

1. Verificá que tu `UserDBHelper` tenga:

    * Versión correcta.
    * Bloques `onCreate()` y `onUpgrade()` completos.
2. Corré la app desde cero o limpiá la base para probar.
3. Asegurate de **no pisar código ajeno** (si es necesario, unifiquen la clase entre dos compañeros).

---

## 👋 ¿Preguntas?

Consultá al responsable técnico del equipo o escribí en el grupo antes de modificar algo delicado como la base de datos.

---

> ¡Siguiendo estas reglas trabajamos más rápido y con menos bugs! 💪

