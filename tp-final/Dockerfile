# Usamos la imagen oficial de OpenJDK como base
FROM openjdk:17-jdk-alpine

# Exponemos el puerto que usa Spring Boot (por defecto 8080)
EXPOSE 8080

# Directorio de trabajo dentro del contenedor
WORKDIR /root

# Copiamos el pom y mvn
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

# Descargamos las dependencias
RUN ./mvnw dependency:go-offline

# Copiamos el código fuente al contenedor
COPY ./src /root/src

# Construimos la aplicacion
RUN ./mvnw clean install -DskipTests

# LEVANTAR NUESTRA APLICACION CUANDO EL CONTENEDOR INICIE
ENTRYPOINT ["java","-jar","/root/target/tp-final-0.0.1-SNAPSHOT.jar"]