FROM openjdk:8-alpine

COPY target/uberjar/capulcu.jar /capulcu/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/capulcu/app.jar"]
