# Repositorio para el trabajo final de PDyC

## Etapas

### Entrega 1 (11/05)

1. Configuración inicial del proyecto (:heavy_check_mark:).
2. Clases del proyecto con su respectivo mapeo JPA (:heavy_check_mark:).
3. El endpoint para obtener canciones debe estar implementado (:heavy_check_mark:).
4. Aplicar patrón DTO y hacer uso de ModelMapper (:heavy_check_mark:).

> **Estado:** (:heavy_check_mark:)

### Entrega 2 (25/05)

1. Autenticación y autorización con JWT (:heavy_check_mark:).

#### Probar la autenticación
Enviar una request de tipo POST a http://192.168.0.2:8081/login que incluya en
el cuerpo una estructura JSON definiendo email y password. Por ejemplo:
```
{
    "email":"franco@yopmail.com",
    "password":"1"
}
```

(en el script SQL están cifradas la contraseña 3 para el usuario bruno y 1 para franco)

aP/ra codificar una contraseña con BCrypt:
```
System.out.println(new BCryptPasswordEncoder().encode("123456"));
```

> **Estado:** (:heavy_check_mark:)

### Entrega 3 (01/06)

1. Validaciones y permisos (:x:).
2. Queries JPA con parámetros (:x:).
3. Todas las funcionalidades requeridas por el enunciado implementadas (:x:).

> **Estado:** (:x:)

## Guía de implementación

### Modelo de Clases y mapeo JPA (:heavy_check_mark:)
- Dentro del package ar.edu.unnoba.pdyc2021.mymusic.model crear todas las
clases del modelo.
- Realizar adecuadamente el mapeo entidad-relación con JPA.

### JPA Repositories (DAO) (:heavy_check_mark:)
- Dentro del package ar.edu.unnoba.pdyc2021.mymusic.repository, para cada
entidad del modelo, declarar el repository correspondiente.

### Service Layer
- Definir en el package ar.edu.unnoba.pdyc2021.mymusic.service todos los
servicios que contengan la lógica de negocios que permitan implementar cada
una de las funcionalidades propuestas.

### Resources del API
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
