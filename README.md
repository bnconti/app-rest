# Repositorio para el trabajo final de PDyC

## Etapas

### Entrega 1 (11/05)

1. Configuración inicial del proyecto. (:heavy_check_mark:)
2. Clases del proyecto con su respectivo mapeo JPA. (:heavy_check_mark:)
3. El endpoint para obtener canciones debe estar implementado. (:heavy_check_mark:)
4. Aplicar patrón DTO y hacer uso de ModelMapper. (:heavy_check_mark:)

> **Estado:** (:heavy_check_mark:)

### Entrega 2 (25/05)

1. Autenticación y autorización con JWT. (:heavy_check_mark:)

> **Estado:** (:heavy_check_mark:)

### Entrega 3 (01/06)

1. Validaciones y permisos. (:x:)
2. Queries JPA con parámetros. (:x:)
3. Todas las funcionalidades requeridas por el enunciado implementadas.

* Autenticación mediante email y password. (:heavy_check_mark:)
```POST http://localhost:8080/music/auth```

* Se podría evitar InternalAuthenticationServiceException cuando el usuario
no existe... (:x:)

* Consultar las canciones disponibles, pudiendo ser filtradas por autor y
género. (:heavy_check_mark:)

    * Pero con el filtro ese no se pueden recuperar *todas* las canciones...
Poner otro método sin los QueryParams no funciona. (:x:)
```GET http://localhost:8080/music/songs?author=Divididos&genre=ROCK```

* Consultar las playlists creadas. (:x:)
``` GET http://localhost:8080/music/playlists/```
Tira una excepción

* Consultar los datos y las canciones de una playlist. (:x:)
```GET http://localhost:8080/music/playlists/{id}```

* Crear playlists. (:x:)
Requiere autenticación, y los datos de la lista en el body.
```PUT http://localhost:8080/music/playlists/```

* Modificación y eliminación de playlists
Requiere autenticación, y sólo puede realizarlo el usuario que la creó.

    * Renombrar. (:x:)
En el body se envía el nombre de la lista.
```POST http://localhost:8080/music/playlists/{id}```

    * Agregar canciones. (:x:)
En el body se envía el id de la canción a agregar.
```POST http://localhost:8080/music/playlists/{id}/songs/```

* Quitar canciones. (:x:)
```DELETE http://localhost:8080/music/playlists/{id}/songs/{song_id}```

* Borrar. (:x:)
```DELETE http://localhost:8080/music/playlists/{id}```


* Si hay tiempo poner una página de error mejor?


> **Estado:** (:x:)

## Guía de implementación

### Modelo de Clases y mapeo JPA (:heavy_check_mark:)
- Dentro del package ar.edu.unnoba.pdyc2021.mymusic.model crear todas las
clases del modelo.
- Realizar adecuadamente el mapeo entidad-relación con JPA.

### JPA Repositories (DAO) (:heavy_check_mark:)
- Dentro del package ar.edu.unnoba.pdyc2021.mymusic.repository, para cada
entidad del modelo, declarar el repository correspondiente.

### Service Layer (:x:)
- Definir en el package ar.edu.unnoba.pdyc2021.mymusic.service todos los
servicios que contengan la lógica de negocios que permitan implementar cada
una de las funcionalidades propuestas.

### Resources del API (:x:)
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

## Guía de uso

### Probar la autenticación
Enviar una request de tipo POST a http://localhost:8080/music/login que incluya
en el cuerpo una estructura JSON definiendo email y password. Por ejemplo:
```
{
    "email":"franco@yopmail.com",
    "password":"1"
}
```

(en el script SQL están cifradas la contraseña 3 para el usuario bruno y 1 para
franco)

Para codificar una contraseña con BCrypt:
```
System.out.println(new BCryptPasswordEncoder().encode("123456"));
```

Esta petición retornará un token llamado Autorization si la autenticación fue
exitosa.
Para permitir acceder a los endpoints que requieran autenticación, se deberá
incluir ese token en la cabezera de la HTTP.

Por ejemplo, usando ``cURL``:
```
curl localhost:8080/music/auth -s -D - -d '{"email":"franco@yopmail.com","password":"1"}' |grep Authorization > /tmp/token
curl localhost:8080/music/playlists -v -H @/tmp/token
```
