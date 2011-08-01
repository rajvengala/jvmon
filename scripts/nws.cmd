@echo off

call "$JVMON_DIR\env.cmd" %*

java -classpath %JVMON_DIR%lib\nws.jar in.uglyhunk.http.server.Main 6781 %JVMON_DIR%\jvmon-plotter





