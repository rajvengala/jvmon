# this is same as JVMON  directory
export NWS_HOME=/home/uglyhunk/projects/jvmon/dist/jvmon

. ./setenv.sh

java -classpath $NWS_HOME/lib/nws.jar in.uglyhunk.http.server.Main 6781 $NWS_HOME/jvmon-plotter


