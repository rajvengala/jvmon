@echo off

call setenv.cmd

psexec -s "%JDK_HOME%\bin\java.exe" -classpath "%JVMON_DIR%\lib\jvmon.jar;%JDK_HOME%\lib\tools.jar" in.uglyhunk.jvm.mon.Main q
