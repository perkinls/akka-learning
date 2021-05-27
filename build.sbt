import sbt.Keys.libraryDependencies

//工程通用配置
lazy val commonSettings = Seq(
  organization := "com.lp.akka.notes",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.8"
)
lazy val dependencies =
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.3.6",
    "com.typesafe.akka" %% "akka-remote" % "2.3.6",
    "org.scala-lang.modules" %% "scala-java8-compat" % "0.3.0",
    "junit" % "junit" % "4.12" % "test",
    "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )




lazy val root = (project in file("."))
  .settings(commonSettings, name := "akka-learning")
  .aggregate(chapter01, chapter02, chapter03, chapter04, chapter05, chapter06)


//第一章节
lazy val chapter01 = (project in file("chapter01"))
  .settings(commonSettings, name := "chapter01")
  .aggregate(chapter01_akkademy_db_java, chapter01_akkademy_db_scala)

lazy val chapter01_akkademy_db_java = (project in file("chapter01/akkademy-db-java"))
  .settings(
    commonSettings,
    name := "akkademy-db-java",
    dependencies
  )
lazy val chapter01_akkademy_db_scala = (project in file("chapter01/akkademy-db-scala"))
  .settings(
    commonSettings,
    name := "akkademy-db-scala",
    dependencies
  )


//第二章节
lazy val chapter02 = (project in file("chapter02"))
  .settings(commonSettings, name := "chapter02")
  .aggregate(chapter02_akkademy_client_db_java, chapter02_akkademy_client_db_scala, chapter02_akkademy_db_java_2, chapter02_akkademy_db_scala_2, chapter02_futures_examples_java, chapter02_futures_examples_scala)

lazy val chapter02_akkademy_client_db_java = (project in file("chapter02/akkademy-db-client-java"))
  .settings(
    commonSettings,
    name := "akkademy-db-client-java",
    dependencies,
    libraryDependencies += "com.lp.akka.notes" %% "akkademy-db-java-2" % "0.0.1-SNAPSHOT"
  )
lazy val chapter02_akkademy_client_db_scala = (project in file("chapter02/akkademy-db-client-scala"))
  .settings(
    commonSettings,
    name := "akkademy-db-client-scala",
    dependencies,
    libraryDependencies += "com.lp.akka.notes" %% "akkademy-db-scala-2" % "0.0.1-SNAPSHOT"
  )

lazy val chapter02_akkademy_db_java_2 = (project in file("chapter02/akkademy-db-java-2"))
  .settings(
    commonSettings,
    name := "akkademy-db-java-2",
    dependencies,
    mappings in(Compile, packageBin) ~= { //发布的内容中排除 application.conf，防止客户端也能够试图启动远程服务器。
      _.filterNot { case (_, name) => Seq("application.conf").contains(name) }
    },
    mainClass in(Compile, run) := Some("com.lp.akka.notes.Main")
  )

lazy val chapter02_akkademy_db_scala_2 = (project in file("chapter02/akkademy-db-scala-2"))
  .settings(
    commonSettings,
    name := "akkademy-db-scala-2",
    dependencies,
    mappings in(Compile, packageBin) ~= {
      _.filterNot { case (_, name) => Seq("application.conf").contains(name) }
    }
  )

lazy val chapter02_futures_examples_java = (project in file("chapter02/futures-examples-java"))
  .settings(
    commonSettings,
    name := "futures-examples-java",
    dependencies
  )

lazy val chapter02_futures_examples_scala = (project in file("chapter02/futures-examples-scala"))
  .settings(
    commonSettings,
    name := "futures-examples-scala",
    dependencies
  )

//第三章
lazy val chapter03 = (project in file("chapter03"))
  .settings(commonSettings, name := "chapter03")
  .aggregate(chapter03_akkademaid_java, chapter03_akkademaid_scala)

lazy val chapter03_akkademaid_java = (project in file("chapter03/akkademaid-java"))
  .settings(
    commonSettings,
    name := "akkademaid-java",
    dependencies,
    libraryDependencies ++= Seq(
      "com.syncthemall" % "boilerpipe" % "1.2.2", // 文本解析工具
      "com.lp.akka.notes" %% "akkademy-db-java-2" % "0.0.1-SNAPSHOT"
    )
  )
lazy val chapter03_akkademaid_scala = (project in file("chapter03/akkademaid-scala"))
  .settings(
    commonSettings,
    name := "akkademaid-scala",
    libraryDependencies ++= Seq(
      "com.syncthemall" % "boilerpipe" % "1.2.2", // 文本解析工具
      "com.lp.akka.notes" %% "akkademy-db-scala-2" % "0.0.1-SNAPSHOT"
    )
  )

//第四章
lazy val chapter04 = (project in file("chapter04"))
  .settings(commonSettings, name := "chapter04")
  .aggregate(akkademy_db_client_java_4, akkademy_db_client_scala_4, akkademy_db_java_4, akkademy_db_scala_4, function_examples_4)

lazy val akkademy_db_client_java_4 = (project in file("chapter04/akkademy-db-client-java-4"))
  .settings(
    commonSettings,
    name := "akkademy-db-client-java-4",
    dependencies,
    libraryDependencies ++= Seq(
      "com.lp.akka.notes" %% "akkademy-db-java-4" % "0.0.1-SNAPSHOT"
    )
  )
lazy val akkademy_db_client_scala_4 = (project in file("chapter04/akkademy-db-client-scala-4"))
  .settings(
    commonSettings,
    name := "akkademy-db-client-scala-4",
    dependencies,
    libraryDependencies ++= Seq(
      "com.lp.akka.notes" %% "akkademy-db-scala-4" % "0.0.1-SNAPSHOT"
    )
  )
lazy val akkademy_db_java_4 = (project in file("chapter04/akkademy-db-java-4"))
  .settings(
    commonSettings,
    name := "akkademy-db-java-4",
    dependencies
  )
lazy val akkademy_db_scala_4 = (project in file("chapter04/akkademy-db-scala-4"))
  .settings(
    commonSettings,
    name := "akkademy-db-scala-4",
    dependencies,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-agent" % "2.3.6",
//      "com.lp.akka.notes" %% "akkademy-db-java-2" % "0.0.1-SNAPSHOT",
      "org.scalatest" %% "scalatest" % "2.1.6" % "test"
    )
  )
lazy val function_examples_4 = (project in file("chapter04/function-examples-4"))
  .settings(
    commonSettings,
    name := "function-examples-4",
    dependencies
  )

//第五章
lazy val chapter05 = (project in file("chapter05"))
  .settings(commonSettings, name := "chapter05")
  .aggregate(akkademaid_java_5, akkademaid_scala_5)

lazy val akkademaid_java_5 = (project in file("chapter05/akkademaid-java-5"))
  .settings(
    commonSettings,
    name := "akkademaid-java-5",
    dependencies,
    libraryDependencies ++= Seq(
      "com.jason-goodwin" % "better-monads" % "0.2.0",
      "com.syncthemall" % "boilerpipe" % "1.2.2",
      "com.novocode" % "junit-interface" % "0.11" % "test"
    )

  )
lazy val akkademaid_scala_5 = (project in file("chapter05/akkademaid-scala-5"))
  .settings(
    commonSettings,
    name := "akkademaid-scala-5",
    dependencies,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" % "akka-http-experimental_2.11" % "1.0-M4",
      "com.typesafe.akka" % "akka-http-core-experimental_2.11" % "1.0-M4",
      "com.syncthemall" % "boilerpipe" % "1.2.2"
    )
  )

//第六章节
lazy val chapter06 = (project in file("chapter06"))
  .settings(commonSettings, name := "chapter06")
  .aggregate(aakkademaid_client_java_6, aakkademaid_client_scala_6,aakkademaid_java_6,aakkademaid_scala_6)

lazy val aakkademaid_client_java_6 = (project in file("chapter06/aakkademaid-client-java-6"))
  .settings(
    commonSettings,
    name := "aakkademaid-client-java-6",
    dependencies,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-cluster" % "2.3.6",
      "com.typesafe.akka" %% "akka-contrib" % "2.3.6"
    )

  )
lazy val aakkademaid_client_scala_6 = (project in file("chapter06/aakkademaid-client-scala-6"))
  .settings(
    commonSettings,
    name := "aakkademaid-client-scala-6",
    dependencies,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-cluster" % "2.3.6",
      "com.typesafe.akka" %% "akka-contrib" % "2.3.6"
    )
  )

lazy val aakkademaid_java_6 = (project in file("chapter06/aakkademaid-java-6"))
  .settings(
    commonSettings,
    name := "aakkademaid-java-6",
    dependencies,
    libraryDependencies ++= Seq(
      "com.syncthemall" % "boilerpipe" % "1.2.2",
      "com.jason-goodwin" % "better-monads" % "0.2.0",
      "com.typesafe.akka" %% "akka-cluster" % "2.3.6",
      "com.typesafe.akka" %% "akka-contrib" % "2.3.6"
    )
  )
lazy val aakkademaid_scala_6 = (project in file("chapter06/aakkademaid-scala-6"))
  .settings(
    commonSettings,
    name := "aakkademaid-scala-6",
    dependencies,
    libraryDependencies ++= Seq(
      "com.syncthemall" % "boilerpipe" % "1.2.2",
      "com.jason-goodwin" % "better-monads" % "0.2.0",
      "com.typesafe.akka" %% "akka-cluster" % "2.3.6",
      "com.typesafe.akka" %% "akka-contrib" % "2.3.6"
    )
  )

//第七章节
lazy val chapter07 = (project in file("chapter07"))
  .settings(commonSettings, name := "chapter07")
  .aggregate(mailbox_demo_java, mailbox_demo_scala)

lazy val mailbox_demo_java = (project in file("chapter07/mailbox-demo-java"))
  .settings(
    commonSettings,
    name := "mailbox-demo-java",
    dependencies
  )
lazy val mailbox_demo_scala = (project in file("chapter07/mailbox-demo-scala"))
  .settings(
    commonSettings,
    name := "mailbox-demo-scala",
    dependencies
  )

//第八章节
lazy val chapter08 = (project in file("chapter08"))
  .settings(commonSettings, name := "chapter08")
  .aggregate(java_akka_chat, scala_akka_chat)

lazy val java_akka_chat = (project in file("chapter08/java-akka-chat"))
  .settings(
    commonSettings,
    name := "java-akka-chat",
    dependencies
  )
lazy val scala_akka_chat = (project in file("chapter08/scala-akka-chat"))
  .settings(
    commonSettings,
    name := "scala-akka-chat",
    dependencies
  )

//第九章节
lazy val chapter09 = (project in file("chapter09"))
  .settings(commonSettings, name := "chapter09")

lazy val akka_agent = (project in file("chapter09/akka-agent"))
  .settings(
    commonSettings,
    name := "akka-agent",
    dependencies
  )
lazy val akka_event_bus = (project in file("chapter09/akka-event-bus"))
  .settings(
    commonSettings,
    name := "akka-event-bus",
    dependencies
  )
lazy val akka_logging = (project in file("chapter09/akka-logging"))
  .settings(
    commonSettings,
    name := "akka-logging",
    dependencies,
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.0.0" % "runtime",
      "com.typesafe.akka" %% "akka-slf4j" % "2.3.11"
    )
  )

javacOptions ++= Seq("-encoding", "UTF-8")
javaOptions in run += "-Xmx1G"

//发布到本地maven仓库的时候，允许覆盖jar。
//发布到仓库后本地maven才能引入，而不再需要加入lib文件
publishM2Configuration := publishM2Configuration.value.withOverwrite(true)
publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)