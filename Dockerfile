FROM clojure
COPY target/caius-0.1.0-SNAPSHOT-standalone.jar /usr/src/app/
WORKDIR /usr/src/app
CMD ["java", "-jar", "caius-0.1.0-SNAPSHOT-standalone.jar"]
