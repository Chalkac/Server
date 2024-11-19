FROM eclipse-temurin:17-jdk-alpine
COPY ./build/libs/*SNAPSHOT.jar chalkac.jar
ENTRYPOINT ["java", "-jar", "chalkac.jar"]