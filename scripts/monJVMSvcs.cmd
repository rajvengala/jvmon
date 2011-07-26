@echo off

set JDK_HOME=C:\Program Files (x86)\Java\jdk1.6.0_25

set JVMON_DIR=C:\jvmon

@REM Monitor all JVMs with description oracle.oats and jconsole every 15 seconds for an hour while scanning for new JVMs every minute
psexec.exe -s "%JDK_HOME%\bin\java.exe" -classpath "%JVMON_DIR%\lib\jvmon.jar;%JDK_HOME%\lib\tools.jar" in.uglyhunk.jvm.mon.Main f 15 240 jconsole,oracle.oats 60
