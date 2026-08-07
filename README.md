<div align="center">

<img src="./assets/banner.svg" alt="ServiHogar AI" width="100%"/>

<br/>

[![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-AI-FFCA28?style=flat-square&logo=firebase&logoColor=black)](https://firebase.google.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-2DD4BF.svg?style=flat-square)](LICENSE)
[![Status](https://img.shields.io/badge/Estado-En%20Desarrollo-E8A33D?style=flat-square)](#-estado-del-proyecto)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-9C7BFF?style=flat-square)](#-contribuir)

**Una plataforma que diagnostica el problema, encuentra al profesional correcto y da seguimiento al servicio — de principio a fin.**

[Características](#-características) ·
[Arquitectura](#%EF%B8%8F-arquitectura) ·
[Instalación](#-instalación) ·
[Estructura](#-estructura-del-proyecto) ·
[Roadmap](#-roadmap)

</div>

<br/>

## 📖 Sobre el proyecto

Contratar a alguien de confianza para un problema del hogar suele significar preguntar en grupos de WhatsApp, comparar precios a ciegas y esperar que la persona llegue. **ServiHogar AI** busca resolver eso con una experiencia de un solo flujo:

1. El usuario **describe el problema** con sus propias palabras.
2. La IA lo **diagnostica** y estima el tipo de servicio y su urgencia.
3. La app **conecta** al usuario con profesionales verificados cerca de su ubicación.
4. El servicio se **agenda, rastrea y paga** dentro de la misma app.

El proyecto está en **fase MVP**, construyendo primero una base Android sólida (Kotlin + Jetpack Compose) sobre la que se irán habilitando el resto de los módulos.

<br/>

## ✨ Características

<table>
<tr>
<td width="33%" valign="top">

### 🤖 Diagnóstico con IA
El usuario describe el problema en lenguaje natural. Firebase AI clasifica el tipo de servicio, estima la urgencia y sugiere una categoría de profesional.

</td>
<td width="33%" valign="top">

### 📍 Geolocalización
Encuentra profesionales verificados cerca de tu ubicación, con distancia estimada y disponibilidad en tiempo real.

</td>
<td width="33%" valign="top">

### 📅 Agenda integrada
Programa el servicio en el horario que te convenga y recibe confirmación y recordatorios automáticos.

</td>
</tr>
<tr>
<td width="33%" valign="top">

### 🧾 Órdenes de servicio
Cada solicitud genera una orden con estado, técnico asignado y costo estimado — trazabilidad total del servicio.

</td>
<td width="33%" valign="top">

### 🔒 Perfiles verificados
Clientes y profesionales operan bajo perfiles verificados, pensados para generar confianza en ambos sentidos.

</td>
<td width="33%" valign="top">

### 💳 Pagos seguros *(planeado)*
Pago dentro de la app una vez confirmado el servicio, sin manejar efectivo ni datos sensibles fuera de la plataforma.

</td>
</tr>
</table>

<br/>

<div align="center">
<img src="./assets/preview.svg" alt="Vista previa conceptual de ServiHogar AI" width="100%"/>
<sub>Mockups de diseño que ilustran el flujo objetivo del producto. La app está en fase MVP; estas pantallas aún no están implementadas.</sub>
</div>

<br/>

## 🚧 Estado del proyecto

| Módulo | Estado |
|---|:---:|
| Arquitectura base (Clean Architecture + MVVM) | ✅ Completado |
| Proyecto Android inicial (Kotlin + Compose) | ✅ Completado |
| Integración inicial con Firebase AI | ✅ Completado |
| Autenticación de usuarios | 🔜 Planificado |
| Perfiles de cliente / profesional | 🔜 Planificado |
| Publicación y gestión de servicios | 🔜 Planificado |
| Geolocalización en tiempo real | 🔜 Planificado |
| Chat cliente–profesional | 🔜 Planificado |
| Pagos seguros | 🔜 Planificado |
| Panel administrativo | 🔜 Planificado |

<br/>

## 🛠 Tecnologías

<div align="center">

| Categoría | Stack |
|---|---|
| **Lenguaje** | Kotlin |
| **UI** | Jetpack Compose · Material 3 |
| **Persistencia local** | Room |
| **Red** | Retrofit · OkHttp · Moshi |
| **Backend / Cloud** | Firebase · Firebase AI |
| **Concurrencia** | Kotlin Coroutines |
| **Build** | Gradle Kotlin DSL |
| **Control de versiones** | Git / GitHub |

</div>

<br/>

## 🏗️ Arquitectura

El proyecto sigue **Clean Architecture** combinada con **MVVM**: la UI nunca habla directamente con la red o la base de datos, todo pasa por el `Repository`, que es la única capa con acceso a servicios externos.

<div align="center">
<img src="./assets/architecture.svg" alt="Arquitectura de ServiHogar AI" width="100%"/>
</div>

- **Presentación** — `Screens` (Compose), `ViewModel`, `Navigation`. Sólo conoce estado de UI, nunca detalles de red o base de datos.
- **Dominio** — `Model` y casos de uso. Reglas de negocio puras, sin dependencias de Android.
- **Datos** — `Repository`, `Room` (local) y `Retrofit` (remoto). Decide si una petición se resuelve en caché o en red, y es el único punto de contacto con Firebase AI, APIs REST y, más adelante, mapas y pagos.

Esta separación permite testear la lógica de negocio de forma aislada y sostener el crecimiento del proyecto sin acoplar la UI a implementaciones concretas.

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

## 🗺 Roadmap

- [x] Arquitectura base + integración inicial con Firebase AI
- [ ] Autenticación de usuarios
- [ ] Perfiles de cliente y profesional
- [ ] Publicación y gestión de servicios
- [ ] Agenda de servicios
- [ ] Geolocalización en tiempo real
- [ ] Integración con mapas
- [ ] Chat entre cliente y profesional
- [ ] Diagnóstico mediante IA (extendido)
- [ ] Seguimiento del técnico en vivo
- [ ] Pagos seguros
- [ ] Panel administrativo

> El objetivo del MVP es validar el flujo diagnóstico → match → agenda → seguimiento con una arquitectura ya preparada para escalar.

<br/>

## 🤝 Contribuir

El proyecto está en desarrollo activo.

1. Haz un fork del repositorio.
2. Crea tu rama: `git checkout -b feature/nueva-funcionalidad`.
3. Sigue las [convenciones de commits](#-flujo-de-trabajo-con-git).
4. Haz push a tu rama y abre un Pull Request.

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
