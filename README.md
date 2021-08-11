# Repositorio para el trabajo final de PDyC

## Etapas

### Entrega 1 (11/05)

1. Configuración inicial del proyecto. (:heavy_check_mark:)
2. Clases del proyecto con su respectivo mapeo JPA. (:heavy_check_mark:)
3. El endpoint para obtener canciones debe estar implementado. 
(:heavy_check_mark:)
4. Aplicar patrón DTO y hacer uso de ModelMapper. (:heavy_check_mark:)

> **Estado:** (:heavy_check_mark:)

### Entrega 2 (25/05)

1. Autenticación y autorización con JWT. (:heavy_check_mark:)

> **Estado:** (:heavy_check_mark:)

### Entrega 3 (01/06)

1. Validaciones y permisos. (:heavy_check_mark:)
2. Queries JPA con parámetros. (:heavy_check_mark:)
3. Todas las funcionalidades requeridas por el enunciado implementadas.
(:heavy_check_mark:)

> **Estado:** (:heavy_check_mark:)

### Entrega 4 (22/06)

* Modificar endpoints de songs y playlists para que funcionen de forma
asincrónica. (:heavy_check_mark:)
  * Utilizar @Suspended y AsyncResponse de JAX-RS.
  * Utilizar CompletableFuture.
  * Implementar comportamiento asoncrónico en los endpoints.

> **Estado:** (:heavy_check_mark:)

### Entrega 5 (15/07)

* Frontend (Single Page Application) utilizando AngularJS.
  * Pedir email y contraseña.
    * Si es correcto, mostrar tabla con el listado de playlists.
    * Si no, mostrar mensaje de error.
  * Permitir cerrar sesión.

> **Estado:** (:heavy_check_mark:)

## Guía de implementación

### Modelo de Clases y mapeo JPA (:heavy_check_mark:)
- Dentro del package ar.edu.unnoba.pdyc2021.mymusic.model crear todas las
clases del modelo.
- Realizar adecuadamente el mapeo entidad-relación con JPA.

### JPA Repositories (DAO) (:heavy_check_mark:)
- Dentro del package ar.edu.unnoba.pdyc2021.mymusic.repository, para cada
entidad del modelo, declarar el repository correspondiente.

### Service Layer (:heavy_check_mark:)
- Definir en el package ar.edu.unnoba.pdyc2021.mymusic.service todos los
servicios que contengan la lógica de negocios que permitan implementar cada
una de las funcionalidades propuestas.

### Resources del API (:heavy_check_mark:)
- Definir en el package ar.edu.unnoba.pdyc2021.mymusic.resource todos los
resources del API
- Nombrar adecuadamente, siguiendo las convenciones, el URI para cada uno y
sus distintos métodos.

### DTO (:heavy_check_mark:)
- Definir en el package ar.edu.unnoba.pdyc2021.mymusic.dto todos los DTO
necesarios para intercambio de información.

### Authentication and Authorization (:heavy_check_mark:)
- Definir en el package ar.edu.unnoba.pdyc2021.mymusic.security todas las
clases necesarias para la implementación de la autenticación y autorización
- Se deberá implementar el estándar JWT y debe estar integrado con Spring
Security.

## Guía de uso de la API REST

1. Primero se debe inicializar PostgreSQL

2. Por única vez se debe inicializar la base de datos. El proyecto está
configurado para utilizar la base de datos PostgreSQL configurada en
``localhost`` en el puerto ``5432``. Se utiliza el usuario ``postgres`` con
contraseña ``postgres``.

Se debe crear la base de datos music_rest y
descomentar la línea ``spring.jpa.hibernate.ddl-auto = create`` de
``src/main/resources/application.properties`` para crear el esquema de la base
de datos y poner datos de prueba.

3. Ejecutar el proyecto.

Esto puede hacerse desde el IDE, o por la línea de comandos, ejecutando el
siguiente comando desde la carpeta back-end:
```mvnw test```

Se asume que el servidor se encuentra en ``localhost`` en el puerto ``8080``.

Mostramos los ejemplos usando ``cURL`` en un sistema tipo Unix, pero pueden
reproducirse usando otros programas, como ``Postman``.

### Obtener canciones

Para obtener todas las canciones, enviar una petición ``GET`` a
``http://localhost:8080/music/songs``

Para obtener las canciones filtradas por autor y/o género, se pueden incluir
los parámetros ``author`` y ``genre``.

Por ejemplo:
```
curl http://localhost:8080/music/songs?author=Divididos&genre=ROCK
```


### Obtener listas de reproducción

Para mostrar un resumen de todas las listas, enviar una petición ``GET`` a
``http://localhost:8080/music/playlists``

Para mostrar un detalle de una lista que incluye las canciones que contiene,
enviarla a ``http://localhost:8080/music/playlists/{id}``, reemplazando
``{id}`` por el id de la lista a consultar.


### Autenticación

En los datos de prueba se dejó cifrada la contraseña ``3`` para el usuario
``bruno@yahoo.com``, y ``1`` para
``franco@yopmail.com``.

Para codificar una contraseña con BCrypt:
```
System.out.println(new BCryptPasswordEncoder().encode("123456"));
```

Para autenticarse, enviar una petición de tipo ``POST`` a
``http://localhost:8080/music/login`` que incluya en el cuerpo una estructura
JSON definiendo email y password.
Esta petición retornará un token llamado ``Autorization`` si la autenticación
fue exitosa.
Para acceder a los endpoints que requieran autenticación, se deberá incluir ese
token en la cabezera de la petición.

Este comando guarda el token en ``/tmp/token`` para incluirlo en peticiones
posteriores:
```
curl localhost:8080/music/auth -s -D - -d \
    '{"email":"franco@yopmail.com","password":"1"}' | \
    grep Authorization > /tmp/token
```


### ABM de listas de reproducción

Todas estas operaciones requieren estar autenticado (se debe enviar en la
cabecera de la petición el token generado en la autenticación).

Además, las operaciones de baja y modificación sólo podrán ser realizadas por el
usuario dueño de la lista.

Para **crear una lista nueva**, enviar una petición ``POST`` a
``http://localhost:8080/music/playlists`` con los datos de la lista en el
cuerpo.
Ejemplo:
```
curl localhost:8080/music/playlists -v -H @/tmp/token \
    -H "Content-Type: application/json" -d \
    '{"name":"Lista de rock","user":{"id":2,"email":"franco@yopmail.com"}}'
```

Para **renombrar una lista**, enviar una petición ``PUT`` a
``http://localhost:8080/music/playlists/{id}``, incluyendo en el cuerpo el
nombre de la lista.
Ejemplo:
```
curl localhost:8080/music/playlists/3 -v -X PUT -H @/tmp/token \
    -H "Content-Type: application/json" -d '{"name":"Compilado"}'
```

Para **agregar una canción a una lista**, enviar una petición ``PUT`` a
``http://localhost:8080/music/playlists/{id}/songs``, incluyendo en el cuerpo
el id de la canción a agregar.
Por ejemplo, para agregar la canción 1 a la lista 3:
```
curl localhost:8080/music/playlists/3/songs -v -X PUT -H @/tmp/token \
    -H "Content-Type: application/json" -d '{"songId":1}'
```

Para **quitar una canción de una lista**, enviar una petición ``DELETE`` a
``http://localhost:8080/music/playlists/{id}/songs/{song_id}``.
Por ejemplo, para quitar la canción 1 de la lista 3:
```
curl localhost:8080/music/playlists/3/songs/1 -v -X DELETE -H @/tmp/token
```

Para **eliminar una lista**, enviar una petición ``DELETE`` a
``http://localhost:8080/music/playlists/{id}``.
Ejemplo:
```
curl localhost:8080/music/playlists/3 -v -H @/tmp/token -X DELETE
```

## Guía de uso del front-end

1. Instalar [Node.js](https://nodejs.org/), que también incluye
[npm](https://www.npmjs.com/) (Node package manager).
2. Con npm, instalar Angular CLI: `npm install -g @angular/cli` (la -g es para
poder invocar a la CLI globalmente).
3. En la carpeta `front-end`, ejecutar el comando `npm i` para instalar todas
las dependencias necesarias del front-end.
4. Luego, ejecutar el comando `ng serve -o` (-o es de --open) para inicializar
el servidor y abrir la aplicación (por ahora, la URL es
`http://localhost:4200/`).
Si no se instaló Angular con la opción -g, el comando a ejecutar es
`node_modules/.bin/ng serve -o`.

### Tareas pendientes:

- Terminar de implementar la funcionalidad del back-end en el front-end:
  - Crear y eliminar playlists.
  - Agregar y quitar canciones de una playlist.
- Opcionales:
  - Renombrar el recurso songs/find/{id} a songs/{id}, para que quede como el
    servicio de playlists??
