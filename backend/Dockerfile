FROM java:8
ADD build/libs/backend-0.0.1-SNAPSHOT.jar //
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "/backend-0.0.1-SNAPSHOT.jar"]