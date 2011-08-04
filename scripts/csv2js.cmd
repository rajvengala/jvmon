@echo off

call setenv.cmd

"%JDK_HOME%\bin\java.exe" -classpath "%JVMON_HOME%\lib\jvmon.jar;%JDK_HOME%\lib\tools.jar" in.uglyhunk.jvm.mon.Main c sample.csv

