# Gradle + Maven sample

One small Java app with **both** `build.gradle` and `pom.xml` at the root (same source, same jar).

- Gradle: `gradle build`  -> `build/libs/gradle-and-maven-app-1.0.0.jar`
- Maven:  `mvn package`   -> `target/gradle-and-maven-app-1.0.0.jar`
- Run:    `java -jar <either jar> [name]`
