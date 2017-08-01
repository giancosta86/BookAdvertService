package info.gianlucacosta.bookadvertservice.utils

/**
  * Provides OS- and architecture-dependent information related to Sqlite
  */
object NativeSqlite {
  /**
    * The directory, in the project structure,
    * containing Sqlite's native dynamic libraries
    */
  lazy val libraryDirectory: String = {
    val osString =
      System
        .getProperty("os.name")
        .toLowerCase

    val architectureString =
      System
        .getProperty("os.arch")


    if (osString.contains("linux")) {
      if (architectureString.contains("64"))
        "lib_managed/sos/com.almworks.sqlite4java/libsqlite4java-linux-amd64"
      else
        "lib_managed/sos/com.almworks.sqlite4java/libsqlite4java-linux-i386"
    } else if (osString.contains("win")) {
      if (architectureString.contains("64"))
        "lib_managed/dlls/com.almworks.sqlite4java/sqlite4java-win32-x64"
      else
        "lib_managed/dlls/com.almworks.sqlite4java/sqlite4java-win32-x86"
    } else if (osString.contains("mac"))
      "lib_managed/dylibs/com.almworks.sqlite4java/libsqlite4java-osx"
    else
      throw new RuntimeException(s"Unknown os: ${osString}")
  }
}
