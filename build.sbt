name := "BookAdvertService"

version := "1.0"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

retrieveManaged := true

libraryDependencies += filters


val dynamoVersion =
  "[1.11,2.0["


val sqliteVersion =
  "1.0.392"


def sqliteTest(artifactId: String) =
  "com.almworks.sqlite4java" % artifactId % sqliteVersion % "test"


resolvers ++= List(
  "DynamoDB Local Release Repository" at "https://s3-us-west-2.amazonaws.com/dynamodb-local/release"
)


libraryDependencies ++= List(
  "com.amazonaws" % "aws-java-sdk-dynamodb" % dynamoVersion
)


libraryDependencies ++= List(
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % "test",

  "com.amazonaws" % "DynamoDBLocal" % dynamoVersion % "test",

  sqliteTest("sqlite4java"),
  sqliteTest("libsqlite4java-linux-amd64"),
  sqliteTest("libsqlite4java-linux-i386"),
  sqliteTest("sqlite4java-win32-x64"),
  sqliteTest("sqlite4java-win32-x86"),
  sqliteTest("libsqlite4java-osx")
)
