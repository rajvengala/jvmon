@echo off

@REM set JVMON_HOME system environment variable before executing this script

call setenv.cmd

psexec.exe -s "%JDK_HOME%\bin\java.exe" -classpath "%JVMON_HOME%\lib\jvmon.jar;%JDK_HOME%\lib\tools.jar" in.uglyhunk.jvm.mon.Main f 15 240 jconsole,oracle.oats 60

