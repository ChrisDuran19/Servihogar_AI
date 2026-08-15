<div align="center">

<br/>

[![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-AI-FFCA28?style=flat-square&logo=firebase&logoColor=black)](https://firebase.google.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-2DD4BF.svg?style=flat-square)](LICENSE)
[![Status](https://img.shields.io/badge/Estado-Relanzamiento-E8593A?style=flat-square)](#-estado-del-proyecto)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-9C7BFF?style=flat-square)](#-contribuir)

**Una plataforma que diagnostica el problema, encuentra al profesional correcto y da seguimiento al servicio — de principio a fin.**

[Sobre el proyecto](#-sobre-el-proyecto) ·
[Diagnóstico actual](#-diagnóstico-del-estado-actual) ·
[Personas y necesidades](#-personas-y-necesidades) ·
[Meta y alcance](#-meta-y-alcance-del-relanzamiento) ·
[Historias de usuario del MVP](#-historias-de-usuario-del-mvp) ·
[Sistema de diseño](#-sistema-de-diseño) ·
[Modelo de datos](#-modelo-de-datos) ·
[Estrategia de IA](#-estrategia-de-diagnóstico-con-ia) ·
[Roadmap](#-roadmap-con-cronograma-detallado) ·
[Testing](#-estrategia-de-pruebas) ·
[Riesgos](#-riesgos-y-supuestos) ·
[Definición de hecho](#-definición-de-hecho-para-el-mvp) ·
[Onboarding](#-guía-de-onboarding-para-el-nuevo-desarrollador) ·
[Instalación](#-instalación)

</div>

<br/>

## 📖 Sobre el proyecto

Contratar a alguien de confianza para un problema del hogar suele significar preguntar en grupos de WhatsApp, comparar precios a ciegas y esperar que la persona llegue. **ServiHogar AI** busca resolver eso con una experiencia de un solo flujo:

1. El usuario **describe el problema** con sus propias palabras.
2. La IA lo **diagnostica** y estima el tipo de servicio y su urgencia.
3. La app **conecta** al usuario con profesionales verificados cerca de su ubicación.
4. El servicio se **agenda, rastrea y paga** dentro de la misma app.

> ⚠️ **El proyecto entra en fase de relanzamiento.** La primera iteración dejó una base de arquitectura y una integración inicial con IA, pero el diseño de producto y buena parte de la implementación necesitan reconstruirse desde cero con un enfoque más disciplinado. Este README documenta ese reinicio a detalle: qué se conserva, qué se descarta, quién lo usa, cómo se ve, cómo se modela la información, y con qué meta de tiempo se entrega un MVP real.

### Problema que resuelve

| Dolor actual del usuario | Cómo lo resuelve ServiHogar AI |
|---|---|
| No sabe qué tipo de profesional necesita ("¿es plomero o gasfitero? ¿es eléctrico o del refrigerador?") | Diagnóstico con IA a partir de una descripción en lenguaje natural |
| Compara precios y confianza a ciegas en grupos de WhatsApp | Perfiles verificados con historial dentro de la plataforma |
| No sabe cuándo llega el profesional ni si va a llegar | Agenda con confirmación y estado de la orden visible |
| No tiene trazabilidad de qué se acordó ni qué se pagó | Orden de servicio con historial de estados (fase 2: pago dentro de la app) |

<br/>

## 🔍 Diagnóstico del estado actual

Antes de sumar funcionalidades nuevas, así queda el proyecto heredado, revisado módulo por módulo:

| Área | Estado heredado | Decisión para el relanzamiento |
|---|---|---|
| Arquitectura (Clean Architecture + MVVM) | Definida a nivel de carpetas, sin reglas de dependencia verificadas | ✅ Se conserva la estructura, se audita capa por capa y se añade un lint de arquitectura (p. ej. Konsist) |
| Integración con Firebase AI | Prueba de concepto inicial, sin manejo de errores ni estructura de respuesta definida | ✅ Se conserva el approach, se rediseña el prompt y se define un contrato de respuesta (JSON tipado) |
| Diseño UI/UX | No implementado (solo mockups conceptuales en `assets/`) | 🔴 Se diseña desde cero, con sistema de diseño propio (ver sección dedicada) |
| Autenticación | No iniciada | 🔴 Se construye desde cero sobre Firebase Auth |
| Perfiles cliente/profesional | No iniciada | 🔴 Se construye desde cero |
| Publicación y matching de servicios | No iniciada | 🔴 Se construye desde cero |
| Geolocalización | No iniciada | 🔴 Se construye desde cero, alcance acotado a distancia/listado, sin ruteo en el MVP |
| Agenda y órdenes de servicio | No iniciada | 🔴 Se construye desde cero |
| Chat y pagos | No iniciados | ⏸️ Quedan fuera del MVP (ver alcance) |
| Pruebas automatizadas | No existen | 🔴 Se incorporan desde el primer módulo funcional, no al final |
| CI/CD | No existe | 🔴 Se configura desde la fase 1 (build + lint + tests en cada PR) |
| Manejo de errores y estados de red | No existe patrón definido | 🔴 Se define un `Result`/`UiState` sellado, reutilizable en toda la app |

**Conclusión:** lo que existe hoy es un esqueleto de proyecto, no una app usable. El relanzamiento arranca prácticamente de cero en UI y en la mayoría de la lógica de negocio, pero reutiliza las decisiones de arquitectura que sí tienen sentido, y corrige desde el día uno los huecos de calidad (testing, CI, manejo de errores) que la primera iteración dejó pendientes.

<br/>

## 👥 Personas y necesidades

El MVP se diseña para dos tipos de usuario con necesidades distintas dentro de la misma app.

**Cliente (quien solicita el servicio)**
- Quiere resolver un problema del hogar sin saber el nombre técnico del oficio que necesita.
- Valora la rapidez de respuesta y la confianza (verificación, historial) más que el precio más bajo.
- Usa la app de forma esporádica, no diaria — la UI debe ser autoexplicativa sin curva de aprendizaje.

**Profesional (quien ofrece el servicio)**
- Quiere recibir solicitudes relevantes a su especialidad y zona, sin ruido.
- Necesita gestionar su disponibilidad y ver el historial de órdenes atendidas.
- Usa la app con mayor frecuencia — la prioridad para él es la velocidad para aceptar/rechazar solicitudes.

> El MVP construye ambos flujos (cliente y profesional) dentro de una sola app con roles, no como dos apps separadas, para simplificar el alcance de la fase 1.

<br/>

## 🎯 Meta y alcance del relanzamiento

### Meta general
Entregar una **primera versión MVP instalable y funcional** de ServiHogar AI para Android, que permita completar el flujo **diagnóstico → match → agenda → seguimiento básico**, con un diseño de UI propio, probado con usuarios reales en beta cerrada.

### Meta de finalización (realista)

| Escenario de dedicación | Meta de finalización del MVP |
|---|---|
| 1 desarrollador Android, tiempo completo | **~6 meses** desde el inicio del relanzamiento |
| 1 desarrollador Android, medio tiempo | **~10 a 12 meses** |

Estas cifras asumen un solo desarrollador Android llevando diseño + implementación, apoyado en Firebase como backend (sin backend propio adicional). Si se suma diseñador UI/UX o un segundo desarrollador, los tiempos de la tabla de roadmap se pueden comprimir entre un 20% y un 35%.

### Qué SÍ entra en el MVP
- Registro/login de clientes y profesionales.
- Descripción del problema en lenguaje natural y diagnóstico con IA (categoría + urgencia estimada).
- Listado de profesionales verificados sugeridos, con geolocalización básica (distancia, no ruteo en mapa).
- Creación de una orden de servicio con estado (solicitado, aceptado, en curso, finalizado).
- Agenda simple de fecha/hora para el servicio.
- Panel mínimo de administración (solo lectura, para moderar profesionales).

### Qué NO entra en el MVP (queda para fase 2)
- Pagos dentro de la app.
- Chat en tiempo real cliente–profesional (se usa notificación/estado como sustituto temporal).
- Seguimiento en vivo del técnico en mapa.
- Panel administrativo completo con métricas.

Separar esto es clave para la meta de 6 meses: intentar construir todo el roadmap original de una vez es la principal razón por la que un proyecto de este tipo no llega a tener una primera versión usable.

### Por qué se acota así (y no de otra forma)

- **Pagos fuera del MVP**: integrar una pasarela de pago añade requisitos legales, de seguridad (PCI) y de soporte que no aportan a validar si el matching diagnóstico→profesional funciona. Se valida primero el flujo con coordinación de pago fuera de la app (efectivo o transferencia acordada entre las partes).
- **Chat fuera del MVP**: un sistema de estados de la orden (solicitado → aceptado → en curso → finalizado) más notificaciones push cubre la comunicación mínima necesaria para la beta cerrada, sin construir infraestructura de mensajería en tiempo real todavía.
- **Ruteo en mapa fuera del MVP**: mostrar distancia estimada y ubicación aproximada del profesional es suficiente para decidir a quién contactar; el seguimiento en vivo (tipo Uber) es una inversión de tiempo alta que no bloquea la validación inicial del producto.

<br/>

## 📝 Historias de usuario del MVP

Formato: *Como [rol], quiero [acción], para [beneficio]*. Cada historia lleva sus criterios de aceptación mínimos — son la base para las pruebas de cada fase.

### Cliente

| # | Historia | Criterios de aceptación |
|---|---|---|
| C1 | Como cliente, quiero registrarme con correo o Google, para crear mi cuenta rápido | Registro válido crea documento en `users`; email duplicado muestra error claro; contraseña con mínimo 8 caracteres |
| C2 | Como cliente, quiero describir mi problema en texto libre, para no tener que saber el nombre técnico del servicio | Campo de texto con mínimo 10 caracteres; botón de enviar deshabilitado si está vacío |
| C3 | Como cliente, quiero recibir una categoría y urgencia sugeridas, para entender qué tipo de profesional necesito | Respuesta de IA en menos de 8 segundos percibidos (con estado de carga); si falla, se ofrece selección manual de categoría |
| C4 | Como cliente, quiero ver profesionales sugeridos cerca de mí, para elegir con quién contactar | Lista ordenada por distancia; mínimo nombre, categoría, distancia y estado de verificación visibles |
| C5 | Como cliente, quiero agendar una fecha y hora para el servicio, para coordinar sin salir de la app | No permite fechas pasadas; confirma con resumen antes de crear la orden |
| C6 | Como cliente, quiero ver el estado de mi orden, para saber si fue aceptada | Estados visibles: solicitado, aceptado, en curso, finalizado, cancelado |

### Profesional

| # | Historia | Criterios de aceptación |
|---|---|---|
| P1 | Como profesional, quiero crear mi perfil con especialidad y zona de cobertura, para recibir solicitudes relevantes | Campos obligatorios: nombre, especialidad(es), zona, documento de verificación (subida de imagen) |
| P2 | Como profesional, quiero ver las solicitudes que me llegan, para aceptar o rechazar | Lista con datos del problema diagnosticado, distancia al cliente y fecha propuesta |
| P3 | Como profesional, quiero marcar una orden como "en curso" y "finalizada", para mantener mi historial actualizado | Cambio de estado sincroniza en tiempo real con la vista del cliente |

<br/>

## 🎨 Sistema de diseño

Antes de construir cualquier pantalla, se define un UI Kit propio en Compose. Esto evita rehacer componentes a mitad de roadmap.

| Elemento | Definición para el MVP |
|---|---|
| **Paleta** | 1 color primario (marca), 1 color secundario/acento, escala neutra de 6 tonos para texto/fondos, colores semánticos (éxito, advertencia, error, info) |
| **Tipografía** | Escala de 6 tamaños (display, título, subtítulo, cuerpo, cuerpo pequeño, caption) sobre una sola familia tipográfica variable |
| **Espaciado** | Escala de 8pt (4, 8, 12, 16, 24, 32, 48) aplicada de forma consistente, sin valores "mágicos" sueltos en cada pantalla |
| **Componentes base** | Botón (primario, secundario, texto), campo de texto, tarjeta de profesional, chip de estado de orden, barra de progreso del diagnóstico IA, estado vacío, estado de error, estado de carga |
| **Iconografía** | Un solo set de iconos (Material Symbols) para evitar mezclar estilos |
| **Modo oscuro** | Contemplado desde el theming inicial (`ColorScheme` dinámico), aunque no sea prioridad de QA en la primera beta |

> Regla de la fase 1: **ninguna pantalla de features se empieza a construir hasta que el UI Kit tenga, al menos, botón, campo de texto, tarjeta y los tres estados (carga/vacío/error) implementados y documentados en un catálogo de componentes (puede ser una pantalla interna de desarrollo tipo "component gallery").**

<br/>

## 🗄 Modelo de datos

Diseño inicial de colecciones en **Firestore** (sujeto a ajuste en la fase 0, pero sirve como contrato de partida para no bloquear el diseño de pantallas).

```
users (colección)
 └─ {userId}
     ├─ nombre: string
     ├─ correo: string
     ├─ rol: "cliente" | "profesional"
     ├─ telefono: string
     ├─ fotoUrl: string?
     └─ creadoEn: timestamp

professionalProfiles (colección)
 └─ {userId}
     ├─ especialidades: string[]         // ej. ["plomeria", "electricidad"]
     ├─ zonaCobertura: geopoint + radioKm: number
     ├─ verificado: boolean
     ├─ documentoVerificacionUrl: string
     ├─ calificacionPromedio: number     // reservado para fase 2
     └─ disponible: boolean

diagnostics (colección)
 └─ {diagnosticId}
     ├─ clienteId: string
     ├─ descripcionOriginal: string
     ├─ categoriaSugerida: string
     ├─ urgenciaSugerida: "baja" | "media" | "alta"
     ├─ confianzaModelo: number
     └─ creadoEn: timestamp

serviceOrders (colección)
 └─ {orderId}
     ├─ clienteId: string
     ├─ profesionalId: string
     ├─ diagnosticId: string
     ├─ estado: "solicitado" | "aceptado" | "en_curso" | "finalizado" | "cancelado"
     ├─ fechaProgramada: timestamp
     ├─ direccionAproximada: geopoint
     ├─ historialEstados: array<{estado, timestamp}>
     └─ creadoEn: timestamp
```

- Reglas de seguridad de Firestore se definen en la fase 1, junto con la autenticación (un cliente no puede leer/escribir órdenes que no le pertenecen; un profesional no puede editar el perfil de otro).
- El campo `historialEstados` existe desde el MVP para no rehacer el modelo cuando se agregue el panel administrativo en fase 2.

<br/>

## 🤖 Estrategia de diagnóstico con IA

1. **Entrada**: texto libre del cliente (mínimo 10, máximo ~500 caracteres para controlar costo/latencia).
2. **Prompt controlado**: se le pide al modelo responder **solo** en un JSON con esquema fijo: `{ "categoria": string, "urgencia": "baja"|"media"|"alta", "confianza": number, "resumen": string }`. Esto evita parsear texto libre en la capa de datos.
3. **Validación**: la respuesta se valida contra una lista cerrada de categorías soportadas (plomería, electricidad, gasfitería, cerrajería, electrodomésticos, pintura, otro). Si el modelo devuelve una categoría fuera de esa lista, se cae a `"otro"` en vez de fallar.
4. **Fallback**: si la llamada falla o tarda más de un umbral definido (p. ej. 10s), se muestra selección manual de categoría — el flujo nunca debe bloquear al usuario por una falla de IA.
5. **Costo y límites**: se define un límite de solicitudes de diagnóstico por usuario/día desde el inicio, para evitar abuso y controlar el gasto en la beta cerrada.
6. **Métrica a observar en beta**: % de diagnósticos donde el cliente termina eligiendo la categoría sugerida vs. corrigiéndola manualmente — es el indicador principal de si vale la pena seguir invirtiendo en este módulo.

<br/>

## 🚧 Estado del proyecto

| Módulo | Estado |
|---|:---:|
| Arquitectura base (Clean Architecture + MVVM) | ♻️ En auditoría |
| Sistema de diseño / UI Kit propio | 🔜 Por iniciar |
| Autenticación de usuarios | 🔜 Por iniciar |
| Perfiles de cliente / profesional | 🔜 Por iniciar |
| Diagnóstico con IA (rediseño de flujo) | 🔜 Por iniciar |
| Publicación y matching de servicios | 🔜 Por iniciar |
| Geolocalización básica | 🔜 Por iniciar |
| Agenda de servicios | 🔜 Por iniciar |
| Órdenes de servicio y estados | 🔜 Por iniciar |
| Chat cliente–profesional | ⏸️ Fuera del MVP |
| Pagos seguros | ⏸️ Fuera del MVP |
| Panel administrativo completo | ⏸️ Fuera del MVP |

<br/>

## 🛠 Tecnologías

<div align="center">

| Categoría | Stack |
|---|---|
| **Lenguaje** | Kotlin |
| **UI** | Jetpack Compose · Material 3 |
| **Persistencia local** | Room |
| **Red** | Retrofit · OkHttp · Moshi |
| **Backend / Cloud** | Firebase (Auth, Firestore, Storage) · Firebase AI |
| **Geolocalización** | Google Play Services Location · Maps SDK (solo visualización de puntos en MVP) |
| **Concurrencia** | Kotlin Coroutines · Flow |
| **Inyección de dependencias** | Hilt |
| **Build** | Gradle Kotlin DSL |
| **Control de versiones** | Git / GitHub |
| **Testing** | JUnit · Turbine · Compose UI Test |

</div>

<br/>

## 🏗️ Arquitectura

Se mantiene **Clean Architecture** combinada con **MVVM**: la UI nunca habla directamente con la red o la base de datos, todo pasa por el `Repository`, que es la única capa con acceso a servicios externos.

- **Presentación** — `Screens` (Compose), `ViewModel`, `Navigation`. Sólo conoce estado de UI, nunca detalles de red o base de datos.
- **Dominio** — `Model` y casos de uso. Reglas de negocio puras, sin dependencias de Android.
- **Datos** — `Repository`, `Room` (local) y `Retrofit`/Firebase SDK (remoto). Decide si una petición se resuelve en caché o en red, y es el único punto de contacto con Firebase AI, geolocalización y, más adelante, pagos.

Como parte del relanzamiento, el nuevo desarrollador debe **auditar cada capa existente antes de escribir código nuevo sobre ella**: confirmar que el `Repository` no tenga lógica de UI filtrada, que los `ViewModel` no llamen directamente a Firebase, y que el dominio esté realmente libre de dependencias de Android.

<br/>

## 📁 Estructura del proyecto

```
ServiHogar_AI/
├── .github/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── dao/
│   │   │   │   │   │   ├── database/
│   │   │   │   │   │   └── entities/
│   │   │   │   │   ├── remote/
│   │   │   │   │   │   ├── api/
│   │   │   │   │   │   ├── dto/
│   │   │   │   │   │   └── services/
│   │   │   │   │   └── repository/
│   │   │   │   ├── model/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/
│   │   │   │   │   ├── navigation/
│   │   │   │   │   ├── screens/
│   │   │   │   │   ├── theme/
│   │   │   │   │   └── viewmodel/
│   │   │   │   ├── utils/
│   │   │   │   ├── di/
│   │   │   │   ├── workers/
│   │   │   │   ├── services/
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── test/
│   │   └── androidTest/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── README.md
└── LICENSE
```

<br/>

## 🗓 Roadmap con cronograma detallado

Cronograma pensado para **1 desarrollador Android a tiempo completo** (referencia base de 6 meses). Cada fase incluye diseño, implementación y pruebas del módulo correspondiente, y termina solo cuando se cumplen sus criterios de salida.

### Fase 0 — Auditoría y limpieza (2 semanas)
- Revisar código heredado capa por capa (`data`, `domain`, `ui`, `di`).
- Decidir explícitamente qué se conserva, qué se reescribe y qué se elimina; documentarlo en un `MIGRATION.md` o issue fijado.
- Definir la paleta, tipografía y escala de espaciado del sistema de diseño.
- Configurar el repositorio: ramas protegidas, plantilla de PR, plantilla de issues.
- **Criterio de salida:** el repositorio compila limpio, sin código muerto, y existe un documento con las decisiones de arquitectura confirmadas.

### Fase 1 — Fundación técnica (2 semanas)
- Configurar inyección de dependencias (Hilt) y navegación (Navigation Compose).
- Implementar el `ColorScheme`/`Typography` en Compose a partir del sistema de diseño.
- Construir el catálogo de componentes base (botón, campo de texto, tarjeta, chip de estado, estados de carga/vacío/error).
- Configurar CI: build + lint + tests unitarios en cada Pull Request (GitHub Actions).
- Definir el patrón de manejo de estado y errores (`UiState` sellado, `Result` en el dominio).
- **Criterio de salida:** un desarrollador nuevo puede crear una pantalla vacía usando solo componentes del catálogo, con CI en verde.

### Fase 2 — Autenticación y perfiles (3-4 semanas)
- Registro/login con correo y Google Sign-In (Firebase Auth).
- Selección de rol (cliente/profesional) en el onboarding.
- Formulario de perfil de profesional: especialidades, zona de cobertura, subida de documento de verificación.
- Reglas de seguridad de Firestore para `users` y `professionalProfiles`.
- Pruebas: casos de éxito y error de registro/login, validaciones de formulario.
- **Criterio de salida:** historias C1 y P1 completas y probadas (ver [Historias de usuario](#-historias-de-usuario-del-mvp)).

### Fase 3 — Diagnóstico con IA (3 semanas)
- Pantalla de descripción del problema con validaciones de longitud.
- Integración con Firebase AI usando el contrato JSON definido en [Estrategia de IA](#-estrategia-de-diagnóstico-con-ia).
- Manejo de fallback a selección manual de categoría.
- Límite de solicitudes por usuario/día.
- Pruebas: parsing de respuesta válida/ inválida, comportamiento ante timeout.
- **Criterio de salida:** historias C2 y C3 completas; métrica de "categoría aceptada vs. corregida" instrumentada (aunque sea con logging simple).

### Fase 4 — Matching y geolocalización (3-4 semanas)
- Permisos de ubicación (runtime permissions) con explicación previa al usuario.
- Listado de profesionales por categoría sugerida, ordenado por distancia.
- Cálculo de distancia cliente-profesional (sin ruteo, solo distancia en línea recta o Distance Matrix puntual).
- Filtro básico por especialidad y disponibilidad.
- Pruebas: cálculo de distancia, orden de la lista, comportamiento sin permisos de ubicación.
- **Criterio de salida:** historia C4 completa.

### Fase 5 — Órdenes y agenda (3-4 semanas)
- Selección de fecha/hora con validación de fechas pasadas.
- Creación de `serviceOrder` vinculando cliente, profesional y diagnóstico.
- Vista de detalle de orden con `historialEstados`.
- Bandeja de solicitudes para el profesional (aceptar/rechazar/cambiar estado).
- Pruebas: transición de estados válida/ inválida, sincronización en tiempo real (listener de Firestore).
- **Criterio de salida:** historias C5, C6, P2 y P3 completas.

### Fase 6 — Notificaciones y pulido (2-3 semanas)
- Notificaciones push (Firebase Cloud Messaging) en cambios de estado de la orden.
- Revisión de accesibilidad básica (contraste, tamaños táctiles, `contentDescription`).
- Revisión de todos los estados vacíos, de carga y de error de cada pantalla.
- Copys revisados (sin lorem ipsum, sin textos de debug).
- **Criterio de salida:** ninguna pantalla del flujo principal queda sin estado de error o de carga manejado.

### Fase 7 — QA y beta cerrada (3-4 semanas)
- Pruebas end-to-end de los flujos completos de cliente y profesional.
- Corrección de bugs priorizados por severidad.
- Distribución vía Firebase App Distribution o Play Internal Testing a un grupo reducido (10-30 usuarios reales).
- Recolección de feedback estructurado (formulario corto + métricas de uso mínimas).
- **Criterio de salida:** ver [Definición de hecho para el MVP](#-definición-de-hecho-para-el-mvp).

**Total estimado: ~22-26 semanas (≈ 5 a 6 meses) a tiempo completo con un solo desarrollador.**

### Fase 2 del producto (post-MVP, no incluida en la meta anterior)
- [ ] Chat cliente–profesional en tiempo real
- [ ] Pagos seguros dentro de la app
- [ ] Seguimiento en vivo del técnico en mapa
- [ ] Panel administrativo completo con métricas
- [ ] Sistema de calificaciones y reseñas
- [ ] Ruteo y ETA estimado (tipo Uber)
- [ ] Soporte multi-idioma

<br/>

## 🧪 Estrategia de pruebas

| Nivel | Qué se prueba | Herramientas |
|---|---|---|
| **Unitarias** | Casos de uso del dominio, mappers, validadores de formulario, parsing de respuesta de IA | JUnit, Turbine (para `Flow`) |
| **Repositorio** | Lógica de caché vs. red, manejo de errores de Firestore/Firebase AI | JUnit + fakes/mocks (MockK) |
| **UI** | Componentes del catálogo, estados de pantalla (carga/vacío/error/éxito) | Compose UI Test |
| **Instrumentadas** | Flujos críticos end-to-end (registro → diagnóstico → orden) | Compose UI Test + Firebase Test Lab (opcional) |
| **Manual / beta** | Usabilidad real, comprensión del diagnóstico sugerido | Beta cerrada con usuarios reales (Fase 7) |

**Regla del roadmap:** ninguna fase se marca como completa sin pruebas del módulo correspondiente — evita que "funciona en mi dispositivo" sea el único criterio de avance.

<br/>

## ⚠️ Riesgos y supuestos

| Riesgo / supuesto | Impacto si se materializa | Mitigación |
|---|---|---|
| Un solo desarrollador a tiempo completo durante 6 meses | El roadmap se extiende a 10-12 meses (ver tabla de meta) | Priorizar fases 0-5 sobre pulido si hay que recortar tiempo |
| Costo/latencia de Firebase AI mayor al esperado | Diagnóstico lento o costoso en producción | Límite de solicitudes/día desde el inicio + fallback manual siempre disponible |
| Baja adopción de profesionales verificados al lanzar la beta | Listado de matching vacío en algunas zonas | Reclutamiento manual de un grupo inicial de profesionales antes de la beta (fuera del alcance técnico, pero condición de éxito del piloto) |
| Cambios de alcance a mitad de fase ("agreguemos chat ya") | Corre el riesgo de repetir el problema original (nunca se termina el MVP) | Cualquier adición a media fase se registra como backlog de fase 2, no se mezcla con el sprint en curso |
| Reglas de seguridad de Firestore mal configuradas | Exposición de datos de otros usuarios | Revisión de reglas como parte del criterio de salida de la Fase 2, con pruebas específicas de acceso denegado |

<br/>

## ✅ Definición de "hecho" para el MVP

El MVP se considera **entregado** cuando, simultáneamente:

- [ ] Las historias de usuario C1-C6 y P1-P3 están implementadas y probadas.
- [ ] El flujo completo cliente (registro → diagnóstico → selección de profesional → agenda → seguimiento de estado) se puede completar sin errores en un dispositivo real.
- [ ] El flujo completo profesional (registro → perfil verificado → recibir solicitud → aceptar → cambiar estados) se puede completar sin errores en un dispositivo real.
- [ ] CI está en verde (build, lint, tests) en la rama principal.
- [ ] Reglas de seguridad de Firestore están activas y probadas (no solo en modo de prueba abierto).
- [ ] La app fue instalada y usada por al menos un grupo de beta cerrada real, y el feedback fue documentado.
- [ ] La tabla de [Estado del proyecto](#-estado-del-proyecto) de este README refleja el estado real de cada módulo.

<br/>

## 👨‍💻 Guía de onboarding para el nuevo desarrollador

Antes de escribir código nuevo, se recomienda este orden:

1. **Clonar y compilar el proyecto tal cual está**, confirmar que el build heredado corre sin errores.
2. **Auditar `data/` y `di/`**: entender qué está realmente conectado a Firebase y qué es solo estructura vacía.
3. **Definir el sistema de diseño** (paleta, tipografía, componentes Compose reutilizables) antes de tocar cualquier pantalla — esto evita rehacer UI más adelante.
4. **Construir la Fase 0 y 1 del roadmap** antes de avanzar a autenticación, aunque parezca "no producir features visibles" al inicio.
5. **Escribir al menos una prueba unitaria por caso de uso** desde el primer módulo, no dejarlo para el final.
6. Actualizar la tabla de **Estado del proyecto** de este README al cerrar cada fase, para que el progreso quede visible para cualquiera que entre al repositorio.

<br/>

## ✅ Requisitos previos

- **Android Studio** (Narwhal o superior)
- **JDK 21**
- **Android SDK 36**
- **Git**

<br/>

## 🚀 Instalación

**1. Clonar el repositorio**

```bash
git clone https://github.com/ChrisDuran19/Servihogar_AI.git
cd Servihogar_AI
```

**2. Sincronizar y compilar**

```bash
# Linux / macOS
./gradlew build

# Windows
gradlew.bat build
```

**3. Instalar en modo Debug**

```bash
./gradlew installDebug
```

<br/>

## ⚙️ Comandos de Gradle

| Comando | Descripción |
|---|---|
| `./gradlew build` | Sincroniza y compila el proyecto |
| `./gradlew installDebug` | Instala la app en modo Debug |
| `./gradlew assembleDebug` | Genera el APK de Debug |
| `./gradlew assembleRelease` | Genera el APK de Release |
| `./gradlew bundleRelease` | Genera el Bundle de Release |
| `./gradlew clean` | Limpia el proyecto |
| `./gradlew clean build` | Limpia y reconstruye el proyecto |
| `./gradlew test` | Corre las pruebas unitarias |
| `./gradlew connectedAndroidTest` | Corre las pruebas instrumentadas |

<br/>

## 🐞 Depuración

Desde Android Studio:

1. Abre el proyecto.
2. Espera a que finalice la sincronización de Gradle.
3. Selecciona la configuración `app`.
4. Inicia un emulador o conecta un dispositivo físico.
5. Ejecuta con **Run ▶️** o **Debug 🐛**.

Herramientas recomendadas: **Breakpoints**, **Logcat**, **Layout Inspector**, **Database Inspector** y **Profiler**.

<br/>

## 🔀 Flujo de trabajo con Git

```bash
git checkout -b feature/nueva-funcionalidad
git add .
git commit -m "feat: descripción"
git push origin feature/nueva-funcionalidad
```

**Convenciones de commits:**

| Prefijo | Uso |
|---|---|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de errores |
| `docs` | Cambios en documentación |
| `refactor` | Refactorización de código |
| `test` | Añadir o modificar pruebas |
| `chore` | Tareas de mantenimiento |

<br/>

## 🤝 Contribuir

El proyecto está en fase de relanzamiento activo.

1. Haz un fork del repositorio.
2. Crea tu rama: `git checkout -b feature/nueva-funcionalidad`.
3. Sigue las [convenciones de commits](#-flujo-de-trabajo-con-git).
4. Haz push a tu rama y abre un Pull Request contra la fase del roadmap correspondiente.

<br/>

## 📄 Licencia

Distribuido bajo licencia **MIT**. Consulta [LICENSE](LICENSE) para más detalles.

<br/>

## 👤 Autor

**Chris Duran**

[![GitHub](https://img.shields.io/badge/GitHub-ChrisDuran19-181717?style=flat-square&logo=github)](https://github.com/ChrisDuran19)

<br/>

<div align="center">
<sub>⭐️ Si este proyecto te parece interesante, considera darle una estrella en GitHub.</sub>
</div>
