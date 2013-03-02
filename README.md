technologies used:

- Scala
- JavaFX
- ScalaFX (wrapped JavaFX)


use MAVEN for building the project

ScalaFX project doesn't have maven artifact, yet. To use it, you must manually add scalafx_2.10-1.0.0-M1.jar file to your local repository:

download [scalafx jar file](https://code.google.com/p/scalafx/downloads/detail?name=scalafx_2.10-1.0.0-M1.jar&can=2&q=)
then run maven command to install this file to your local repository:
>mvn install:install-file -DartifactId=scalafx -DgroupId=org.scalafx -Dpackaging=jar -Dfile=scalafx_2.10-1.0.0-M1.jar -Dversion=2.10-1.0.0-M1