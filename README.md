🏠 ServiHogar AI

Plataforma inteligente de servicios para el hogar impulsada por Inteligencia Artificial.

Descripción

ServiHogar AI es una aplicación móvil en desarrollo cuyo objetivo es transformar la contratación de servicios para el hogar mediante Inteligencia Artificial, geolocalización, programación de servicios y una experiencia segura para clientes y profesionales.

Actualmente el proyecto se encuentra en la fase inicial (MVP), enfocada en construir una base sólida y escalable para Android utilizando Kotlin y Jetpack Compose.

Estado del proyecto

🚧 En desarrollo

Arquitectura base configurada

Proyecto Android inicial creado

Integración inicial con Firebase AI

Objetivos

Construir una plataforma moderna para servicios del hogar.

Implementar una arquitectura escalable basada en Clean Architecture y MVVM.

Integrar IA para diagnóstico y estimación de servicios.

Preparar el proyecto para futuras integraciones con mapas, pagos y notificaciones.

Tecnologías

Kotlin

Jetpack Compose

Material 3

Room

Retrofit

OkHttp

Moshi

Firebase

Firebase AI

Coroutines

Gradle Kotlin DSL

Git / GitHub

Estructura del proyecto

ServiHogar_AI
│
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

Requisitos

Android Studio (Narwhal o superior)

JDK 21

Android SDK 36

Git

Clonar

git clone https://github.com/ChrisDuran19/Servihogar_AI.git
cd Servihogar_AI

Ejecutar

Sincronizar:

./gradlew build

Windows:

gradlew.bat build

Instalar en modo Debug:

./gradlew installDebug

APK Debug:

./gradlew assembleDebug

APK Release:

./gradlew assembleRelease

Bundle:

./gradlew bundleRelease

Limpiar:

./gradlew clean

Reconstruir:

./gradlew clean build

Depuración

Desde Android Studio:

Abrir el proyecto.

Esperar sincronización de Gradle.

Seleccionar la configuración app.

Iniciar un emulador o conectar un dispositivo.

Ejecutar con Run o Debug.

Utilizar Breakpoints, Logcat, Layout Inspector, Database Inspector y Profiler para analizar la aplicación.

Flujo Git

git checkout -b feature/nueva-funcionalidad
git add .
git commit -m "feat: descripción"
git push origin feature/nueva-funcionalidad

Convenciones:

feat

fix

docs

refactor

test

chore

Próximos pasos

Las siguientes fases incorporarán autenticación, perfiles de usuario, publicación y gestión de servicios, agenda, geolocalización en tiempo real, integración con mapas, chat, diagnóstico mediante IA, seguimiento del técnico, pagos seguros, panel administrativo y las funcionalidades necesarias para completar el MVP de ServiHogar AI con una arquitectura preparada para crecer.
