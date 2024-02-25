FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
# Firebase JSON 파일 복사
COPY src/main/resources/tteolione-firebase-adminsdk-fk0ms-ed40ba5d01.json /app/tteolione-firebase-adminsdk-fk0ms-ed40ba5d01.json
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]