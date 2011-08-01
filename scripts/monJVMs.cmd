@echo off

call "$JVMON_DIR\env.cmd" %*

"%JDK_HOME%\bin\java.exe" -classpath "%JVMON_DIR%\lib\jvmon.jar;%JDK_HOME%\lib\tools.jar" in.uglyhunk.jvm.mon.Main b 5 20 jconsole 30
