JDK_HOME=/usr/local/java/jdk1.6.0_26

PATH=$JDK_HOME/bin

# Monitor all JVMs with description oracle.oats and jconsole every 15 seconds for an hour while scanning for new JVMs every minute
java -classpath .:../lib/jvmon.jar:$JDK_HOME/lib/tools.jar in.uglyhunk.jvm.mon.Main b 5 20 netbeans 60


