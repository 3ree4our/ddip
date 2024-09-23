FROM openjdk:11-jre-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY ./*.jar app.jar

EXPOSE 443

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENV JAVA_OPTS="-Xms512m -Xmx1024m"

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
