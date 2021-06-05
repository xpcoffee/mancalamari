# Structure

This doc gives a non-exhaustive overview of key files in the project. If you're looking for a guide to orient yourself in the project you've come to the right place!

## Top level project files

- **README.md** - Read that first, if you haven't already.
- **build.gradle** - The Gradle build/dependency file.
- **gradlew/gradlew.bat** - Executables that perform a production build of the project.
- **Dockerfile** - Docker image build definition for running the application on Docker.
- **TODO.md** - Rough overview of next steps in the project.

## src/main/java

The primary Java assets for the project under `org.moncalamari`.

- **Application.java** - Entry point. Starts the SpringBoot application.
- **HttpController.java** - Defines the application API.
- **.../game** - Core classes that model the game.
- **.../persistence** - Classes that help persist the game state to a database via Hibernate.
- **.../reducers** - Core logic that process actions and determine their results on the game state.
- **.../request** - Models requests that can be made against the app.

## src/main/resources

Assets and configuration necessary to configure the webserver.

* **application.properties** - SpringBoot configuration.
* **log4j2.xml** - Logging configuration.
* **static/** - Static assets vended for the website. This contains a minimal React app.

## src/test

This is where the unit test suites are written.