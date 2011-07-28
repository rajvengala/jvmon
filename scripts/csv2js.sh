JDK_HOME=/usr/local/java/jdk1.6.0_26
PATH=$JDK_HOME/bin

# Query running JVMs
java -classpath .:../lib/jvmon.jar:$JDK_HOME/lib/tools.jar in.uglyhunk.jvm.mon.Main c sample.csv



