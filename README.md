# Repositorio para el trabajo final de PDyC

## Etapas

### Entrega 1 (12/05)

1. Configuración inicial del proyecto (:heavy_check_mark:).
2. Clases del proyecto con su respectivo mapeo JPA (:heavy_check_mark:).
3. El endpoint para obtener canciones debe estar implementado (:heavy_check_mark:).
4. Aplicar patrón DTO y hacer uso de ModelMapper (:x:, se puede hacer en **SongResource**).

> **Estado:** (:x:)

### Entrega 2 (19/05)

1. Autenticación y autorización con JWT (:x:).

> **Estado:** (:x:)

### Entrega 3 (02/06)

1. Validaciones y permisos (:x:).
2. Queries JPA con parámetros (:x:).
3. Todas las funcionalidades requeridas por el enunciado implementadas (:x:).

> **Estado:** (:x:)

## Guía de implementación

### Modelo de Clases y mapeo JPA
- Dentro del package ar.edu.unnoba.pdyc2021.mymusic.model crear todas las
clases del modelo.
- Realizar adecuadamente el mapeo entidad-relación con JPA.

### JPA Repositories (DAO)
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

### DTO
- Definir en el package ar.edu.unnoba.pdyc2021.mymusic.dto para todos los DTO
necesarios para intercambio de información.

### Authentication and Authorization
- Definir en el package ar.edu.unnoba.pdyc2021.mymusic.security para todas las
clases necesarias para la implementación de la autenticación y autorización
- Se deberá implementar el estándar JWT y debe estar integrado con Spring
Security.
