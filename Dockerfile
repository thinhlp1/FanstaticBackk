FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar FanstaticBack.jar
ENTRYPOINT ["java","-jar","/FanstaticBack.jar"]
EXPOSE 8080