FROM khipu/openjdk17-alpine

# Crea un directorio de trabajo en el contenedor
RUN mkdir -p /home/app

# Copiar el archivo JAR de la aplicación al contenedor
COPY target/jenkins-0.0.1-SNAPSHOT.jar /home/app/jenkins-service.jar

# Puerto en el que la aplicación expone sus servicios
EXPOSE 8081

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/home/app/jenkins-service.jar"]