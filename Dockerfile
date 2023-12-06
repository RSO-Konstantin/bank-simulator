FROM eclipse-temurin:17-jre

RUN mkdir /app

WORKDIR /app

ADD ./target/bank-simulator-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "bank-simulator-0.0.1-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "image-catalog-api-1.0.0-SNAPSHOT.jar"]
#CMD java -jar image-catalog-api-1.0.0-SNAPSHOT.jar
