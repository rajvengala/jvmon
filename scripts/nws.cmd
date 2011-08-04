@echo off
@REM this is same as JVMON  directory
set NWS_HOME=C:\jvmon

call setenv.cmd

"%JDK_HOME%\bin\java.exe" -classpath %NWS_HOME%\lib\nws.jar in.uglyhunk.http.server.Main 6781 %NWS_HOME%\jvmon-plotter


