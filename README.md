# TaskManager

TASK MANAGER Documentación técnica y guía de instalación

1.  DESCRIPCIÓN GENERAL

Task Manager es una aplicación web para administrar tareas. Permite
consultar las tareas registradas, crear nuevas tareas y cambiar su
estado entre pendiente y completada.

La solución está dividida en frontend y backend. El frontend consume una
API REST desarrollada con Jakarta EE, y el backend utiliza PostgreSQL
para almacenar la información.

Este documento describe la arquitectura, las tecnologías, la
configuración de la base de datos, el despliegue local, los endpoints y
las pruebas realizadas.

2.  TECNOLOGÍAS UTILIZADAS

Backend - Java 21 - Jakarta EE 10 - Payara Server 6 - JPA /
EntityManager - Maven 3.10.0 - PostgreSQL 17

Frontend - Angular 22 - TypeScript - HTML5 - CSS3 - Node.js 24 - npm

Herramientas de apoyo - NetBeans - Visual Studio Code - pgAdmin -
Postman - Git y GitHub

3.  ARQUITECTURA Y TIPO DE DESPLIEGUE

La aplicación utiliza una arquitectura cliente-servidor de tres
componentes:

1.  Frontend: aplicación Angular que presenta la interfaz y realiza
    solicitudes HTTP.
2.  Backend: API REST desarrollada con Jakarta EE y desplegada en Payara
    Server.
3.  Base de datos: PostgreSQL, donde se almacenan las tareas.

Durante el desarrollo, los tres componentes se ejecutan localmente y de
forma independiente:

-   Frontend: http://localhost:4200
-   Backend: http://localhost:8080/jakartaee-hello-world
-   API REST: http://localhost:8080/jakartaee-hello-world/api/tareas
-   PostgreSQL: localhost, puerto 5433

El backend se empaqueta como archivo WAR y se despliega en Payara
Server. El frontend se ejecuta mediante el servidor de desarrollo de
Angular.

Nota: esta documentación describe el despliegue local utilizado durante
el desarrollo. No se afirma que la aplicación esté desplegada en un
entorno de producción.

4.  REQUISITOS PREVIOS

Instalar las siguientes herramientas:

-   JDK 21.
-   Apache Maven 3.10.0.
-   Payara Server 6.
-   PostgreSQL 17.
-   Node.js y npm.
-   Angular CLI 22.
-   Git.

Verificar las instalaciones desde una terminal:

java -version mvn -version node -v npm -v ng version

5.  ESTRUCTURA DEL PROYECTO

El proyecto contiene dos partes principales:

Backend (proyecto Jakarta EE): - config: configuración de la aplicación
REST y CORS. - controller: endpoints REST. - dto: objetos de
transferencia de datos y respuestas de error. - entity: entidades JPA. -
repository: operaciones de persistencia. - service: lógica de negocio y
validaciones. - src/main/resources/META-INF/persistence.xml:
configuración de persistencia.

Frontend: - src/app/components: componentes de interfaz. -
src/app/models: modelos TypeScript. - src/app/services: servicios para
consumir la API. - app.config.ts: configuración de proveedores de
Angular. - app.routes.ts: configuración de rutas.

6.  CONFIGURACIÓN DE LA BASE DE DATOS

Crear una base de datos llamada:

pruebaTareas

En esa base de datos, ejecutar el siguiente script:

CREATE DATABASE "pruebaTareas";

CREATE TABLE tareas ( id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY
KEY, titulo VARCHAR(150) NOT NULL, completada BOOLEAN NOT NULL DEFAULT
FALSE, CONSTRAINT ck_tareas_titulo CHECK (BTRIM(titulo) <> ’’) );

La tabla contiene:

-   id: identificador único autogenerado.
-   titulo: descripción de la tarea, con un máximo de 150 caracteres.
-   completada: estado de la tarea. Su valor inicial es FALSE.

7.  CONFIGURACIÓN DE PAYARA Y JDBC

El backend utiliza un recurso JDBC administrado por Payara para
conectarse a PostgreSQL.

Configuración utilizada durante el desarrollo:

-   Connection Pool: TareasPool
-   JDBC Resource: jdbc/TareasDS
-   Tipo de recurso: javax.sql.DataSource
-   Clase de datasource: org.postgresql.ds.PGSimpleDataSource
-   Driver: org.postgresql.Driver
-   Base de datos: pruebaTareas
-   Servidor: localhost
-   Puerto PostgreSQL: 5433
-   Usuario: postgres

La contraseña debe configurarse localmente en Payara. No debe publicarse
en el repositorio.

El controlador JDBC de PostgreSQL debe estar disponible para Payara.
Durante el desarrollo se utilizó el archivo del controlador PostgreSQL
en el directorio lib del dominio de Payara y se reinició el servidor.

En la consola administrativa de Payara: 1. Crear el pool JDBC
TareasPool. 2. Configurar el driver y los datos de conexión. 3. Ejecutar
Ping para comprobar la conexión. 4. Crear el recurso JDBC jdbc/TareasDS
asociado al pool. 5. Verificar que el recurso esté habilitado.

La unidad de persistencia del backend utiliza el nombre TareasPU y el
recurso jdbc/TareasDS.

8.  DESPLIEGUE Y EJECUCIÓN DEL BACKEND

Desde la carpeta raíz del proyecto backend, ejecutar:

mvn clean package

Maven compilará el proyecto y generará el archivo WAR dentro de la
carpeta target.

Archivo generado:

target/jakartaee-hello-world.war

Iniciar Payara desde la carpeta glassfish/bin:

asadmin start-domain domain1

En Windows PowerShell también puede utilizarse:

..bat start-domain domain1

Desplegar el archivo WAR desde la consola administrativa de Payara,
disponible normalmente en:

http://localhost:4848

En la sección Applications: 1. Seleccionar Deploy. 2. Elegir el archivo
target/jakartaee-hello-world.war. 3. Completar el despliegue. 4.
Verificar que la aplicación aparezca habilitada.

La API quedará disponible en:

http://localhost:8080/jakartaee-hello-world/api/tareas

Si se realizan cambios en el backend, volver a ejecutar Maven y
desplegar la nueva versión del WAR.

9.  INSTALACIÓN Y EJECUCIÓN DEL FRONTEND

Abrir una terminal en la carpeta frontend-tareas.

Instalar las dependencias:

npm install

Iniciar el servidor de desarrollo:

ng serve -o

Si PowerShell impide ejecutar ng.ps1, en Windows puede utilizarse:

ng.cmd serve -o

Angular abrirá la aplicación en:

http://localhost:4200

El servicio de Angular debe apuntar a la URL de la API:

http://localhost:8080/jakartaee-hello-world/api/tareas

El backend debe estar desplegado y Payara debe estar iniciado para que
el frontend pueda consultar y modificar tareas.

10. CONFIGURACIÓN CORS

Durante el desarrollo, el frontend se ejecuta en localhost:4200 y el
backend en localhost:8080. Como utilizan orígenes distintos, se
configuró CORS en el backend para permitir las solicitudes desde:

http://localhost:4200

Se permiten los métodos GET, POST, PATCH y OPTIONS, así como los
encabezados necesarios para las solicitudes JSON.

Para un despliegue en otro entorno, el origen permitido debe ajustarse
al dominio real del frontend.

11. ENDPOINTS DE LA API REST

URL base:

http://localhost:8080/jakartaee-hello-world/api/tareas

GET /tareas Descripción: obtiene la lista de tareas registradas.
Respuesta exitosa: 200 OK.

POST /tareas Descripción: registra una nueva tarea. Cuerpo JSON de
ejemplo:

{ “titulo”: “Completar prueba técnica” }

Respuesta exitosa: 201 Created.

PATCH /tareas/{id} Descripción: actualiza el estado de una tarea
existente. Cuerpo JSON de ejemplo:

{ “completada”: true }

Respuesta exitosa: 200 OK.

Códigos HTTP contemplados: - 200 OK: operación realizada
correctamente. - 201 Created: recurso creado correctamente. - 400 Bad
Request: solicitud con datos inválidos. - 404 Not Found: tarea no
encontrada.

12. VALIDACIONES Y MANEJO DE ERRORES

El backend valida los datos antes de realizar las operaciones:

-   El título es obligatorio.
-   El título no puede estar compuesto únicamente por espacios.
-   El título no puede superar los 150 caracteres.
-   El ID debe ser válido.
-   El campo completada es obligatorio para actualizar y debe ser
    booleano.
-   Si el ID no corresponde a una tarea existente, se devuelve un error
    404.

Las respuestas de error utilizan una estructura JSON con los campos
error y mensaje.

Ejemplo:

{ “error”: “VALIDATION_ERROR”, “mensaje”: “El título es obligatorio” }

13. PRUEBAS REALIZADAS

Las pruebas se ejecutaron utilizando Postman contra la API desplegada
localmente.

Prueba 1: crear una tarea válida. - Método: POST. - Cuerpo: {“titulo”:
“Validación CP2”}. - Resultado obtenido: 201 Created. - Evaluación:
correcta.

Prueba 2: crear una tarea con título vacío. - Método: POST. - Cuerpo:
{“titulo”: ” “}. - Resultado obtenido: 400 Bad Request. - Mensaje: El
título es obligatorio. - Evaluación: correcta.

Prueba 3: crear una tarea con más de 150 caracteres. - Método: POST. -
Resultado obtenido: 400 Bad Request. - Mensaje: El título no puede
superar los 150 caracteres. - Evaluación: correcta.

Prueba 4: actualizar una tarea existente. - Método: PATCH. - ID
utilizado: 7. - Cuerpo: {“completada”: true}. - Resultado obtenido: 200
OK. - Evaluación: correcta.

Prueba 5: actualizar una tarea inexistente. - Método: PATCH. - ID
utilizado: 999999. - Cuerpo: {“completada”: true}. - Resultado obtenido:
404 Not Found. - Mensaje: Tarea no encontrada. - Evaluación: correcta.

Prueba 6: actualizar con un valor nulo. - Método: PATCH. - ID utilizado:
7. - Cuerpo: {“completada”: null}. - Resultado obtenido: 400 Bad
Request. - Mensaje: El campo completada es obligatorio y debe ser
booleano. - Evaluación: correcta.

14. DECISIONES TÉCNICAS

-   Jakarta EE y JAX-RS: se utilizaron para exponer los servicios REST.
-   JPA y EntityManager: permiten gestionar la persistencia de las
    entidades y las consultas a PostgreSQL.
-   Patrón Repository: separa las operaciones de acceso a datos de la
    lógica de negocio.
-   Capa Service: centraliza las reglas de negocio y validaciones.
-   DTO: separa los datos recibidos por la API de las entidades y
    permite estructurar las respuestas de error.
-   PostgreSQL: almacena las tareas de forma persistente.
-   Angular: proporciona una interfaz independiente que consume la API
    mediante HTTP.
-   CORS: permite la comunicación entre el frontend y el backend durante
    el desarrollo.
-   Maven: automatiza la compilación y el empaquetado del backend en
    formato WAR.

15. CONSIDERACIONES PARA EL REPOSITORIO

No incluir en GitHub: - Contraseñas reales. - Credenciales de bases de
datos. - Archivos locales con secretos. - Carpetas node_modules. -
Archivos generados que no sean necesarios para reconstruir el proyecto.

Incluir: - Código fuente del backend. - Código fuente del frontend. -
Archivos de configuración necesarios, sin secretos. - Script SQL para
crear la tabla. - Este README con las instrucciones de instalación,
ejecución y pruebas.

16. AUTOR

Daniel Alejandro Cortez Sánchez Proyecto desarrollado como parte de una
prueba técnica.
