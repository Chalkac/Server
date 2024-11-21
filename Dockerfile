# 1단계: 빌드 환경
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY ./gradlew .
COPY ./gradle ./gradle
COPY ./build.gradle ./settings.gradle ./
COPY ./src ./src

RUN ./gradlew build --no-daemon && rm -rf /root/.gradle

# 2단계: 실행 환경
FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt/app

# 빌드된 JAR 파일을 chalkac.jar로 복사
COPY --from=builder /app/build/libs/*.jar chalkac.jar

ENV PROFILE=local
EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s CMD curl -f http://localhost/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "chalkac.jar"]
