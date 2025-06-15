# Vive Málaga

Aplicación móvil que centraliza la oferta cultural, gastronómica y de ocio de la ciudad de Málaga.  
Dirigida tanto a residentes como a turistas, Vive Málaga promueve la sostenibilidad, la digitalización y el apoyo a los negocios locales, en línea con los objetivos del Plan Estratégico Málaga 2030.

## 📱 Funcionalidades principales

- 🔍 **Búsqueda inteligente** por nombre, categoría y localidad, con sugerencias en tiempo real.
- 📍 **Exploración de lugares** con fichas informativas, imágenes, descripciones y ubicación.
- 🗓️ **Sistema de reservas** con selección de fecha y hora y control de disponibilidad.
- ✍️ **Valoraciones de usuarios**: añade reseñas con título, comentario y fecha.
- ⭐ **Favoritos**: guarda lugares en tu lista personalizada en la nube.
- 🕓 **Historial**: consulta los lugares que has visitado.
- 👤 **Perfil de usuario**: gestiona tu imagen y tus datos, con opción de restaurar la imagen por defecto.

## 🧱 Tecnologías utilizadas

- **Kotlin + Jetpack Compose** (UI declarativa)
- **Firebase**
    - Authentication (gestión de usuarios)
    - Firestore (base de datos en tiempo real)
    - Storage (imágenes y multimedia)
- **Room** (persistencia local para historial y favoritos offline)
- **Navigation Component** (navegación por pantallas)
- **GitHub** (control de versiones)
- **Figma** (prototipado gráfico y diseño de interfaz)

## 🏗️ Arquitectura

- **MVVM (Model - ViewModel - View)**
- Separación entre lógica de UI (`Screen`) y lógica de composición (`Content`)
- Repositorios centralizados para acceso a datos (Firebase / Room)
- ViewModels por pantalla para control de estado reactivo

## 🚀 Instalación y ejecución

1. Clona el repositorio:  
   `git clone https://github.com/jelennsu/vive-malaga-app.git`
2. Abre el proyecto en Android Studio.
3. Asegúrate de tener un dispositivo o emulador con Android 8.0 (API 26) o superior.
4. Añade tu archivo `google-services.json` en la carpeta `app/` para integrar Firebase.
5. Ejecuta la app desde Android Studio.

## 📦 Releases

El proyecto sigue un versionado semántico (MAJOR.MINOR.PATCH):

- **MAJOR**: cambios incompatibles o grandes novedades.
- **MINOR**: nuevas funcionalidades compatibles.
- **PATCH**: corrección de errores y mejoras menores.

### Versiones principales

- `v1.0.0` (2025-06-15): Primera versión estable con funcionalidades básicas:
    - Búsqueda inteligente
    - Exploración y detalle de lugares
    - Sistema de reservas
    - Valoraciones y favoritos
    - Perfil de usuario

### Descarga

Las versiones compiladas (APK) se publican en la sección [Releases](https://github.com/jelennsu/vive-malaga-app/releases) del repositorio GitHub.

Para compilar el proyecto desde el código fuente, sigue las instrucciones de la sección **Instalación y ejecución**.


## 🧑‍💻 Autora

Elena Suárez Serrano – Estudiante de Desarrollo de Aplicaciones Multiplataforma.  
Proyecto final de ciclo formativo.

## 📄 Licencia

El proyecto Vive Málaga se distribuye bajo licencia **MIT** para su código original.  
Parte del código, basado en el [Basic Layouts in Compose Codelab](https://developer.android.com/codelabs/jetpack-compose-layouts?hl=es-419#3), 
está bajo licencia **Apache 2.0** y se mantiene conforme a sus términos.

Copyright 2022 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.



   
