# 🎬 FFE Cinema

Aplicacion web completa para gestion y compra de entradas de cine hecha por ***Marius Puruguay, Patrick Firczyk y Daniel Jimenez*** para el proyecto intermodular final del curso de ***2do de Desarrollo de Aplicaciones Web***.
### El proyecto combina:

- ⚙️ Backend Spring Boot con API REST protegida por JWT.
- 🖥️ Frontend server-side con Pebble Templates.
- 🛡️ Seguridad mixta: login web por sesion + API stateless.
- 🧾 Generacion de facturas en PDF con QR por entrada.
- 🌍 Internacionalizacion multiidioma.

# 📘 1. Introduccion

### FFE Cinema simula el flujo real de una plataforma de cine:

1. El usuario consulta cartelera y sesiones.
2. Selecciona butacas de una sesion.
3. Compra entradas.
4. Se genera una factura con detalle de entradas y codigos QR.

### Flujo rapido con emojis:

1. 🍿 Consulta cartelera y sesiones.
2. 💺 Selecciona butacas.
3. 💳 Realiza la compra.
4. 🧾 Recibe factura con QR.

### La aplicacion esta pensada para dos perfiles:

- 👤 **Usuario final:** navega, se registra, compra y descarga su factura.
- 👑 **Administrador:** gestiona usuarios, peliculas y sesiones desde panel web y API.

# 🧰 2. Stack Tecnologico

### ⚙️ Backend

- ☕ Java 25 (propiedad `java.version`), compilando a compatibilidad Java 21 (`release/source/target=21`).
- 🚀 Spring Boot 3.5.9.
- 🧩 Spring Web, Validation, Data JPA, Security, Cache, GraphQL.
- 🗄️ Base de datos H2 en desarrollo.
- 🔐 JWT (Auth0 java-jwt).
- 📚 OpenAPI/Swagger (springdoc).

### 🎨 Frontend

- 🪨 Pebble Template Engine.
- 🎛️ Bootstrap 5 + Bootstrap Icons.
- 🎨 CSS propio y JS vanilla para interacciones (selector de butacas, panel admin).

### 📄 Documentos y extras

- 📄 OpenPDF para facturas.
- 📡 API externa de QR: https://api.qrserver.com/

# 🛠️ 3. Requisitos Previos (Instalacion en Orden)

## 📦 3.1 Que necesitas tener instalado

1. 🧬 Git.
2. ☕ JDK 25 recomendado (tambien compatible con JDK 21+ mientras se mantenga `release` en 21).
3. 🌐 Conexion a internet para:
	 - 📥 Descargar dependencias Maven.
	 - 📱 Cargar QR desde api.qrserver.com al generar PDF.

**Nota:** No necesitas instalar Maven global porque el proyecto incluye Maven Wrapper (mvnw, mvnw.cmd).

## 🪟 3.2 Instalacion paso a paso (Windows)

1. Instala Git:
	 - https://git-scm.com/download/win

2. Instala Java (JDK):
	 - Eclipse Temurin: https://adoptium.net/

3. Verifica que Java esta disponible:

```powershell
java -version
```

4. Clona el repositorio y entra en la carpeta del proyecto:

```powershell
git clone <https://github.com/danieljimenez45/FFE.git>
cd FFE
```

5. Compila el proyecto:

```powershell
.\mvnw.cmd -DskipTests package
```

6. Arranca la aplicacion en desarrollo:

```powershell
.\mvnw.cmd spring-boot:run
```

7. Abre el navegador:

- 🏠 App web: http://localhost:3010
- 📘 Swagger UI (dev): http://localhost:3010/swagger-ui/index.html
- 🗄️ H2 Console (dev): http://localhost:3010/h2-console

## 🔐 3.3 Credenciales de prueba incluidas

Datos cargados desde data.sql:


- 👑 Admin:
	- usuario: admin
	- password: admin123
- 👤 Usuario normal (ejemplo):
	- usuario: laura
	- password: clave123

# 🚀 4. Ejecucion por Entornos

## 🧪 4.1 Desarrollo (default)

- 🗄️ DB: H2 en memoria.
- ♻️ hibernate ddl-auto: create-drop.
- 📥 Se carga data.sql en cada arranque.
- 🧪 Swagger/GraphiQL/H2 habilitados.

Comando:

```powershell
.\mvnw.cmd spring-boot:run
```

## 🏭 4.2 Produccion (perfil prod)

En produccion debes definir variables de entorno:

- 🗄️ DB_URL
- 👤 DB_USERNAME
- 🔑 DB_PASSWORD
- 🔐 JWT_SECRET
- ⏳ JWT_EXPIRATION (opcional, por defecto 86400)
- 🚪 PORT (opcional, por defecto 3010)

Comando ejemplo:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_URL="jdbc:postgresql://localhost:5432/ffe"
$env:DB_USERNAME="ffe_user"
$env:DB_PASSWORD="ffe_pass"
$env:JWT_SECRET="cambia_este_secreto"
.\mvnw.cmd spring-boot:run
```

En prod se deshabilitan Swagger, GraphiQL y H2 console.

# 🌳 5. Arbol de Carpetas del Proyecto

```text
📁 FFE/
|- 📄 pom.xml                      # Dependencias y build Maven
|- 🧩 mvnw, mvnw.cmd               # Maven Wrapper
|- 📁 src/
|  |- 📁 main/
|  |  |- 📁 java/com/entradas_cine/ffe/
|  |  |  |- 📄 FfeApplication.java
|  |  |  |- 📁 cine/
|  |  |  |  |- 📁 config/          # Seguridad, rutas API, i18n, JWT
|  |  |  |  |- 📁 rest/            # API REST (auth, usuarios, peliculas, sesiones, entradas, facturas)
|  |  |  |  |- 📁 web/             # Controladores MVC y vistas web
|  |  |- 📁 resources/
|  |  |  |- ⚙️ application.properties
|  |  |  |- ⚙️ application-prod.properties
|  |  |  |- 🗃️ data.sql            # Datos semilla
|  |  |  |- 🌐 messages*.properties # Textos multiidioma
|  |  |  |- 📁 static/
|  |  |  |  |- 📁 css/
|  |  |  |  |- 📁 images/
|  |  |  |  |- 📁 video/
|  |  |  |- 📁 templates/
|  |  |     |- 📁 fragments/       # layout, head, navbar, header, footer
|  |  |     |- 📁 auth/            # login, register
|  |  |     |- 📁 admin/           # panel admin
|  |  |     |- 📁 butacas/         # selector de asientos
|  |  |     |- 📁 facturas/        # confirmacion y preview PDF
|  |  |     |- 📁 peliculas/       # listado y detalle
|  |  |     |- 📁 usuario/         # perfil
|  |- 📁 test/
|     |- 📁 java/com/entradas_cine/ffe/
|     |  |- 🧪 ...ServiceImplTest.java
|     |- 🗃️ resources/reset.sql
|- 🏗️ target/                      # Artefactos de compilacion
```

### Carpetas mas importantes para entender el sistema rapido:

1. 🧠 `src/main/java/.../cine/rest` -> logica API y dominio.
2. 🛡️ `src/main/java/.../cine/config` -> seguridad, JWT, i18n, rutas.
3. 🖼️ `src/main/resources/templates` -> frontend real de la aplicacion.
4. 🗃️ `src/main/resources/data.sql` -> datos iniciales y usuarios de prueba.

# ✨ 6. Caracteristicas Unicas y Diferenciales

## 🧾 6.1 Factura PDF automatica

- 🧾 Generacion de PDF por compra.
- 👤 Incluye datos cliente, entradas, sesion y total.
- 👀 Vista previa en navegador (inline) y opcion de descarga (attachment).

## 📱 6.2 QR por entrada

- 🎟️ Cada entrada dentro de la factura contiene un QR unico.
- 🧩 El QR se construye con codigo de factura + fila + butaca.
- 🌐 El QR se obtiene consumiendo api.qrserver.com en tiempo real.

## 🛡️ 6.3 Seguridad dual (caso real mixto)

- 🔌 API REST: JWT + stateless + sin CSRF.
- 🖥️ Frontend MVC: sesion HTTP + CSRF + formulario de login.
- 👤👑 Reglas de acceso por rol (USER/ADMIN) en rutas REST y web.

## 🌍 6.4 Internacionalizacion multiidioma

- 🇪🇸 Idioma por defecto: es.
- 🌐 Idiomas disponibles: es, en, fr, de, pt.
- 🍪 Cambio de idioma por parametro lang y **cookie** de 7 dias.

## 🧑‍💼 6.5 Panel Admin web completo

- 👥 CRUD de usuarios, peliculas y sesiones desde modales.
- ✅ Filtros visuales, acciones protegidas y confirmaciones.

## 💺 6.6 Selector visual de butacas

- 🧱 Renderizado por filas/columnas segun sala.
- 🚫 Control de butacas ocupadas.
- 💶 Calculo dinamico de total antes de pagar.

# 🔌 7. Endpoints Backend

### Prefijo base REST: /api/v1

## 🔐 7.1 Auth

- **POST** /api/v1/auth/signup
	- 🔓 Registro publico, devuelve JWT.
- **POST** /api/v1/auth/signin
	- 🔐 Login API, devuelve JWT.

## 🎞️ 7.2 Peliculas

- **GET** /api/v1/peliculas
	- 🔓 Publico, cartelera activa.
- **GET** /api/v1/peliculas/{id}
	- Publico.
- **GET** /api/v1/peliculas/buscar?genero=&edad=&activa=
	- Publico.
- **POST** /api/v1/peliculas
	- 👑 ADMIN.
- **PATCH** /api/v1/peliculas/{id}/estado
	- 👑 ADMIN.

## 🕒 7.3 Sesiones

- **GET** /api/v1/sesiones
	- 🔓 Publico.
- **GET** /api/v1/sesiones/pelicula/{peliculaId}
	- 🔓 Publico.
- **GET** /api/v1/sesiones/proximas
	- 🔓 Publico.
- **DELETE** /api/v1/sesiones?id={id}
	- 👑 ADMIN.

## 🎟️ 7.4 Entradas

- **GET** /api/v1/entradas/sesion/{id}/butacas-ocupadas
	- 🔓 Publico (clave para selector de asientos).
- **POST** /api/v1/entradas
	- 👤 Usuario autenticado.
- **GET** /api/v1/entradas/{id}
	- 👤 Usuario autenticado.
- **DELETE** /api/v1/entradas/{id}
	- 👑 ADMIN.

## 🧾 7.5 Facturas

- **POST** /api/v1/facturas
	- 👤 Usuario autenticado.
- **GET** /api/v1/facturas/me
	- 👤 Facturas del usuario autenticado.
- **GET** /api/v1/facturas/{id}
	- 👤 Usuario autenticado (solo propias, salvo ADMIN).
- **GET** /api/v1/facturas
	- 👑 ADMIN (todas).

## 👥 7.6 Usuarios (solo ADMIN)

- **GET** /api/v1/usuarios
- **GET** /api/v1/usuarios/{id}
- **POST** /api/v1/usuarios
- **POST** /api/v1/usuarios/admin
- **PUT** /api/v1/usuarios/{id}
- **DELETE** /api/v1/usuarios/{id}

# 🖥️ 8. Cómo funciona el frontend

## 🧱 8.1 Arquitectura de vistas

- 🧱 Motor de plantillas: Pebble.
- 🧭 Layout central con fragments (head/header/navbar/footer).
- Vistas principales:
	- 🏠 index (home/cartelera).
	- 🎬 detalle de pelicula.
	- 💺 selector de butacas.
	- 🔐 login/registro.
	- 👤 perfil de usuario.
	- 👑 panel admin.
	- 🧾 confirmacion de factura y preview de PDF.

## 🔒 8.2 Seguridad frontend

- 🛡️ CSRF inyectado en todas las vistas mediante ControllerAdvice.
- 🔒 Formularios protegidos en login, logout, compra y panel admin.
- ✅ Register con autologin tras alta correcta.

## 🎨 8.3 Estilo y UX

- 🎛️ Bootstrap 5 + estilos propios (look cinematografico).
- 🎥 Header con video de fondo.
- 📱 Navbar responsive y menu lateral movil.
- 🏳️ Selector de idioma con banderas.
- 💬 Componentes de compra con feedback de seleccion y total en vivo.

# ✅ 9. Calidad y Testing

Suites disponibles en src/test para servicios de:

- 👥 usuarios
- 🎬 peliculas
- 🕒 sesiones
- 🎟️ entradas
- 🧾 facturas

### Ejecutar tests:

```powershell
.\mvnw.cmd test
```

Estado actual observado en este entorno:

- ✅ Build de empaquetado: correcto con .\mvnw.cmd -DskipTests package.
- ✅ Tests: suite corregida y ejecutada en verde (52/52) tras alinear expectativas con claves i18n de data.sql.

# ⌨️ 10. Comandos Utiles

Compilar sin tests:

```powershell
.\mvnw.cmd -DskipTests package
```

Ejecutar app:

```powershell
.\mvnw.cmd spring-boot:run
```

Ejecutar tests:

```powershell
.\mvnw.cmd test
```

Limpiar y recompilar:

```powershell
.\mvnw.cmd clean package
```

# 🧯 11. Troubleshooting Rapido

1. ☕ La app no arranca por Java:
	 - Verifica java -version.
	 - Usa preferiblemente JDK 25 (o minimo JDK 21 compatible) y que JAVA_HOME apunte a ese JDK.

2. 🚪 Error de puerto ocupado:
	 - Cambia PORT o libera el 3010.

3. 📘 No carga Swagger o H2:
	 - Revisa que estas en perfil dev/default, no prod.

4. 📱 QR no aparece en PDF:
	 - Revisa conexion a internet y acceso a api.qrserver.com.

5. 🧪 Fallan tests de peliculas/sesiones por titulos:
	 - Revisar fixtures y aserciones para alinear con uso de claves i18n en data.sql.

# ☁️ 12. Despliegue en la Nube

La aplicacion esta desplegada en produccion utilizando servicios cloud gestionados.

### 🏗️ 12.1 Arquitectura desplegada

La arquitectura actual separa aplicacion y base de datos:

1. 🖥️ Aplicacion Spring Boot + Pebble desplegada en Render.
2. 🗄️ Base de datos PostgreSQL gestionada en Render.

Esquema real:

```text
👥 Usuarios (web/API)
	|
	v
☁️ Render Web Service
(Spring Boot + Pebble)
	|
	v
🗄️ PostgreSQL Render
(Base de datos gestionada)
```

### ⚙️ 12.2 Configuracion de produccion

La aplicacion utiliza el perfil `prod` para el entorno desplegado.

Variables de entorno configuradas:

* ⚙️ SPRING_PROFILES_ACTIVE=prod
* 🚪 PORT
* 🗄️ DB_URL
* 👤 DB_USERNAME
* 🔑 DB_PASSWORD
* 🔐 JWT_SECRET
* ⏳ JWT_EXPIRATION

### 🗄️ 12.3 Base de datos

* 🐘 Motor utilizado: PostgreSQL.
* ☁️ Servicio gestionado por Render.
* 🔒 Conexion mediante credenciales privadas definidas en variables de entorno.
* ♻️ Persistencia separada de la aplicacion para mantener datos entre despliegues.

### 🚀 12.4 Procedimiento de despliegue utilizado

1. 📦 Compilacion del proyecto Maven.
2. ☁️ Creacion de un Web Service en Render conectado al repositorio GitHub.
3. 🗄️ Creacion de instancia PostgreSQL en Render.
4. 🔗 Configuracion de variables de entorno para conexion segura.
5. 🚀 Despliegue automatico desde la rama principal del repositorio.
6. ✅ Verificacion de arranque correcto, login y acceso a base de datos.

### 🛡️ 12.5 Medidas de seguridad aplicadas

* 🔐 Secretos y credenciales almacenados como variables de entorno.
* 🚫 Datos sensibles fuera del repositorio.
* 🌐 Acceso HTTPS proporcionado por Render.
* 🛡️ Seguridad Spring Security activa en entorno productivo.
* 🔒 Swagger, GraphiQL y H2 deshabilitados en produccion.

### 📈 12.6 Escalabilidad y mantenimiento

* ♻️ Render permite redespliegue automatico desde GitHub.
* 📦 La aplicacion se ejecuta como servicio Java independiente.
* 🗄️ La base de datos permanece persistente entre despliegues.
* 🔄 La arquitectura permite migracion futura a Docker o Kubernetes sin cambios grandes en codigo.

### 🧭 12.7 Datos del despliegue

* ☁️ Plataforma cloud: Render.
* 🗄️ Base de datos: PostgreSQL gestionado.
* 🔄 Tipo de despliegue: CI/CD conectado a GitHub.
* 🌐 URL publica app: https://ffe.onrender.com
* 📍 Region cloud: Frankfurt (EU Central)
* 👨‍💻 Responsables tecnicos:

  * Marius Puruguay
  * Patrick Firczyk
  * Daniel Jimenez

### 📋 12.8 Posibles mejoras futuras

* 📈 Integracion de monitorizacion y metricas.
* 💾 Backups automatizados programados.
* 🐳 Contenerizacion completa con Docker.
* 🔁 Pipeline CI/CD avanzado con tests automaticos previos a despliegue.
* 🌐 Dominio personalizado y configuracion DNS propia.


# 📌 13. Notas Finales

- 🧱 El proyecto esta preparado para crecer en arquitectura modular por capas (controllers/services/repositories/dto/mappers).
- 🧪 Aunque existe dependencia de GraphQL, actualmente no hay resolvers implementados en codigo.
- 🎯 El enfoque principal activo es MVC + REST con seguridad fuerte y flujo completo de compra.
