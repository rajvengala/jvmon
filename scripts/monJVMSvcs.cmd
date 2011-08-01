@echo off

call "$JVMON_DIR\env.cmd" %*

psexec.exe -s "%JDK_HOME%\bin\java.exe" -classpath "%JVMON_DIR%\lib\jvmon.jar;%JDK_HOME%\lib\tools.jar" in.uglyhunk.jvm.mon.Main f 15 240 jconsole,oracle.oats 60
