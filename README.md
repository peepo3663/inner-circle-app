# CS673F20T1

### Project Description

Social and relationship have never been overemphasized. The Internet is supposed to increase social interaction and strengthen connections. Unexpectedly, it greatly weakens the ability of our interpersonal communication in real life. We have lost the confidence to build a loving relationship and lost the incentive to connect with individuals and nature.   Although the existing social media is designed to help individuals building open and diverse relationships, we still feel lonely, and we still long for intimacy, privacy, and communication with our inner circles such as our close friends and loved ones.

### Code Style

- Implementation Kotiln base on standards by [JetBrain Kotlin Code Standard](https://kotlinlang.org/docs/reference/coding-conventions.html)
- Implementation Java base on standards by [Oracle](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)

### Building

#### Android Building Status

[![Actions Status](https://github.com/BUMETCS673/CS673F20T1/workflows/Android%20CI/badge.svg)](https://github.com/BUMETCS673/CS673F20T1/actions)

### Feature

- As a user, I want to send a message to others, so that I can have a private chat with my friend.
- As a user, I want to send pictures to others, so that I can share my beautiful pictures to my friend.
- As a user, I want to login to the application with my social media, so that this will make me more comfortable using this application.
- As a user, I want to create a chat group of my close friends, so that I can have my close friends discussion.
- As a user, I want to send a file or location to my friend, so that I can share important files or location for meeting up.
- As a user, I want to invite other friends to my current chatroom, so that I can carry on my topics when my friends.
- As a user, I want to receive a notification when my friends message me, so that I can reply to them instantly.


### Technology and Frameworks

- Kotlin: mobile application development language.
- Java: backend language used for controllers between mobile application and Firebase instance.
- Android Studio: IDE used for project's Android.
- IntelliJ: IDE used for project's back end.
- Fastlane: Automatic script and building for Android application.
- CircleCi: Automated Build Tools on cloud.
- Espresso: UI Testing for Android.
- JUnit: Testing Framework for Android and Backend.
- Git(Github): Source control version repository.
- Firebase Cloud Message: Support Notification feature.
- Firebase Cloud Firestore: Support real time database for chat application.
- Gradle: Project configuration for Android.
- Google API (Login): Authentication for application with Google Account.

### Setup and running
#### Running and Building Android Application
1. Install [Android Studio](https://developer.android.com/studio) and All SDKs.
2. Clone this repo.
3. run 
```sh
./gradlew build
```

#### Running and Building Java Spring Boot Application
1. Install Java JDK8 and Maven
2. Clone this repo.
3. Go to `/Code/Backend/innerCircle`
4. Run
```sh
mvn clean install
```
and
5. Run
```sh
mvn spring-boot:run
```
6. If you would like to build as executable JAR file.
```sh
mvn clean package spring-boot:repackage
java -jar target/innerCircle-0.1.0-SNAPSHOT.jar
```

#### Running and Testing on Postman

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/ea75d44e19a87debfefe)

#### Distribute the APKs for the application
1. Install [bundler](https://bundler.io/) and [fastlane](https://fastlane.tools/) build tools.
2. install all bundler dependencies by execute the command.
```sh
bundle install
```
3. to create debuggable APK, run this command.
```sh
bundle exec fastlane compile_app
```
4. to create signed apk, run this command
```sh
bundle exec fastlane build_release_apk
```
5. install to your device, you need `adb` which is bundled in Android SDK platform or [download here](https://developer.android.com/studio)

#### Here is the team introduction [team 1 members](https://github.com/BUMETCS673/CS673F20T1/blob/master/team1.md)
