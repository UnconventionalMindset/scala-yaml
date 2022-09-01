import BuildHelper._

def scala3Version        = "3.1.3"
def scala2Version        = "2.13.9"
def munitVersion         = "1.0.0-M6"
def projectName          = "scala-yaml"
def localSnapshotVersion = "0.0.5-SNAPSHOT"
def isCI                 = System.getenv("CI") != null

inThisBuild(
  List(
    organization       := "org.virtuslab",
    crossScalaVersions := Seq(scala2Version, scala3Version),
    scalaVersion       := scala3Version,
    version ~= { dynVer =>
      if (isCI) dynVer
      else localSnapshotVersion // only for local publishing
    },
    homepage := Some(url("https://github.com/VirtusLab/scala-yaml")),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "lwronski",
        "Łukasz Wroński",
        "lukaszwronski97@gmail.com",
        url("https://github.com/lwronski")
      ),
      Developer(
        "kpodsiad",
        "Kamil Podsiadło",
        "kpodsiadlo@virtuslab.com",
        url("https://github.com/kpodsiad")
      )
    )
  )
)

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .withoutSuffixFor(JVMPlatform)
  .settings(
    name := projectName,
    libraryDependencies ++= Seq(Deps.pprint % Test),
    libraryDependencies ++= List(
      "org.scalameta" %%% "munit" % munitVersion % Test
    )
  )
  .settings(docsSettings)

lazy val integration = project
  .in(file("integration-tests"))
  .dependsOn(core.jvm)
  .settings(
    name           := "integration",
    moduleName     := "integration",
    publish / skip := true,
    libraryDependencies ++= List(
      "org.scalameta" %% "munit"  % munitVersion,
      "com.lihaoyi"   %% "os-lib" % "0.8.1",
      "com.lihaoyi"   %% "pprint" % "0.7.3"
    )
  )
  .settings(docsSettings)
