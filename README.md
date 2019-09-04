# About
This is a simple showcase to demonstrate why should AdoptOpenJDK Docker image include libfontconfig1 lib.

# How to run

Gradle has 3 commands this case:
* startWithAdoptOpenJdkBad
* startWithAdoptOpenJdkFixed
* startWithOpenJdk

## startWithOpenJdk

this command embeds the simple image generation example to an OpenJDK image and runs it on port `8080`

## startWithAdoptOpenJdkBad

this command embeds the simple image generation example to the default AdoptOpenJDK image and runs it on port `8081`

this service will fail because Java can not paint text without libfontconfig1 library (which is insalled in openjdk image)

## startWithAdoptOpenJdkFixed

this command embeds the simple image generation example to an extended AdoptOpenJDK image and runs it on port `8082`

the image is extendeded with the `libfontconfig1` library is also installed

this service works correctly

## example command to start all 3 docker containers

`./gradlew startWithOpenJdk startWithAdoptOpenJdkBad startWithAdoptOpenJdkFixed`
