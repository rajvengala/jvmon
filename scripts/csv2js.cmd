@echo off

set JDK_HOME=C:\Program Files (x86)\Java\jdk1.6.0_25

set JVMON_DIR=C:\jvmon

@REM Query running JVMs
"%JDK_HOME%\bin\java.exe" -classpath "%JVMON_DIR%\lib\jvmon.jar;%JDK_HOME%\lib\tools.jar" in.uglyhunk.jvm.mon.Main c sample.csv

