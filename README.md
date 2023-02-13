# Evaluación: JAVA
## Instrucciones

-La versión de Java sobre la cuál debe ejercutarse el IDE es la 8.

- Clonar ambos proyectos.
```sh
https://github.com/namichetti/Eureka.git
```
```sh
https://github.com/namichetti/User.git
```
- Correr primeramente el proyecto Eureka y luego User, ambos con el comando
```sh
mvn spring-boot:run
```
- Eureka se levanta en http://localhost:8761/
- User tiene puerto dinámico y se puede conocer el mismo accediendo a la URL anterior.

## Endpoints

- Crear un usuario. 
```sh
http://localhost:{PORT}/sign-up
```
- Devuelve todos los usuarios
```sh
http://localhost:{PORT}/
```
