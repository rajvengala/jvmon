JDK_HOME=/usr/local/java/jdk1.6.0_26

PATH=$JDK_HOME/bin

# Monitor all JVMs with description oracle.oats and jconsole every 15 seconds for an hour while scanning for new JVMs every minute
java -classpath .:jvmon.jar:$JDK_HOME/lib/tools.jar in.uglyhunk.jvm.mon.Main f 15 240 jconsole,oracle.oats 60


