# Usa una imagen base de Java 17
FROM openjdk:17-alpine
VOLUME /tmp

RUN ./mvn clean package

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR de tu aplicación Spring Boot al contenedor
COPY target/nombre-de-tu-archivo.jar /app/app.jar

# Expón el puerto en el que se ejecuta tu aplicación Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
CMD ["java", "-jar", "app.jar"]
