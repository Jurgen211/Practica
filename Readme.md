# Spring Boot + PostgreSQL Docker

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener los siguientes componentes instalados:

- **Docker**: Asegúrate de tener Docker instalado en tu máquina.
- **JDK 17**: Se requiere el SDK de Java 17 para ejecutar el proyecto.
- **Postman**: Para realizar pruebas de integración y solicitudes HTTP.

---

## Pasos para Ejecutar el Proyecto

1. **Iniciar PostgreSQL con Docker**

   Ejecuta el siguiente comando para iniciar una instancia de PostgreSQL en Docker:

   ```bash
   docker run --name some-postgres -dp 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres

2. Ejecutar proyecto desde el archivo JwtApplication 

3. Abrir postman en la coleccion Libreria para lo que son usuarios
   * Ejecutar el Post de Register/User
   * Login/User (Esto crea un web Token)
   * Create/User
   * FindById/User
   * Update/User
4. Ejecutar los siguientes Puntos de la coleccion Libreria
   * Create/Book (Esto crea un libro y su id queda registrada en una variable global)
   * List/Books
   * FindById/Books
   * Update/Books
5. Para la solicitud de libros se utiliza los siguientes Endpoints
   * Loan/Books (Esto realiza una peticion desde el usuario que esta logueado junto al id de algun libro y cambia su estado)
   * Return/Books (Retorna el libro a su estado inicial y agrega una variable de la fecha de devolucion)

## Pruebas de Integracion

Para ejecutar las pruebas de integracion 
1. Entrar en las carpetas test/java/com/proyect.proy
2. Entrar en el archivo JwtApplicationTest
3. Ejecutar la clase con el mismo nombre

## Nota


