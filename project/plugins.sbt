logLevel := Level.Warn

addSbtPlugin("com.localytics" % "sbt-dynamodb" % "1.5.3")


resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/maven-releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.12")