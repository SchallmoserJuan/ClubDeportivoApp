# Club Deportivo App

Aplicación móvil desarrollada en Android para la gestión de clientes y pagos de un club deportivo.  
Permite registrar nuevos socios, emitir carnets, registrar pagos y generar listados según distintos criterios.

---

## 🧑‍💻 Integrantes del equipo

- [Juan Schallmoser] 
- [Carlos Andres Lovera Rodriguez]  
- [Sofia Agostina Trucco]  
- [Nahir Jasmin Icare]
- [Chico Rodrigo José]

---

## 📱 Funcionalidades principales

- Inicio de sesión y recuperación de contraseña
- Registro de nuevos clientes (socios y no socios)
- Carga de foto y datos personales
- Registro de pagos con múltiples formas de pago (efectivo, tarjeta, transferencia)
- Emisión de carnet digital
- Visualización y descarga de comprobantes
- Generación de listados: socios, no socios y vencimientos

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje**: Kotlin
- **Framework**: Android SDK
- **Componentes**:
  - `ConstraintLayout`, `CardView`, `TextInputEditText`, `RadioGroup`, `Button`, etc.
  - Navegación entre `Activities`
- **Control de versiones**: Git / GitHub
- **Diseño UI**: Estilos personalizados y `drawable` para fondos

---

## 📁 Estructura del proyecto

```bash
app/
├── java/com/example/clubdeportivoapp/
│   ├── LoginActivity.kt
│   ├── InicioActivity.kt
│   ├── FormularioClienteActivity.kt
│   ├── InscribirNuevoClienteActivity.kt
│   ├── MenuPrincipalActivity.kt
│   ├── RegistroPagoActivity.kt
│   ├── FormaDePagoActivity.kt
│   ├── EmitirCarnet.kt
│   ├── Comprobante.kt
│   └── ...
├── res/
│   ├── layout/
│   │   ├── activity_login.xml
│   │   ├── activity_formulario_cliente.xml
│   │   └── ...
│   ├── values/strings.xml
│   └── drawable/
```

---

## 📝 Instrucciones de instalación

1. Clonar el repositorio:

```bash
git clone https://github.com/usuario/proyecto-club-deportivo.git
```

2. Abrir el proyecto en **Android Studio**.

3. Sincronizar Gradle y compilar.

4. Ejecutar en emulador o dispositivo físico.

---

## 📌 Consideraciones

- El proyecto fue trabajado de forma colaborativa utilizando ramas por funcionalidad (feature branches).
- Se realizó integración continua mediante merges progresivos a la rama `main`.
- La lógica está distribuida por pantallas/Activities, siguiendo el patrón MVC simple.

---

## 💬 Comentarios finales

Este proyecto refleja el desarrollo completo de una app Android con múltiples pantallas, formularios y flujos de navegación, orientado a la gestión de un club deportivo. Puede ser expandido fácilmente con una base de datos y funciones online.
