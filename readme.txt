jvmon is a command line tool to monitor jvm platform.

===============
Features:
===============

+ Reports metrics related to heap memory, non-heap memory, threads, classes, compilation, garbage collector, memory pools
  of the JVM platform (NOT for applications running inside JVM)

+ Using seperate web-based component called "plotter", all the data can be plotted easily.

+ Dynamically scans for new JVMs and gracefully handles dead JVMs

+ Shows all the JVMs running in a host and allows users to monitor JVMs of choice.

+ Saves metrics in csv format to a file and prints in a pretty format to console. Console output can be disabled.

+ A new csv data file for each day and js data file (used in plotter).

+ Can monitor all the OLT agent processes during load test. This is useful to determine the memory resource requirements for load test.

+ Can monitor JVM instances running as Windows services using another PsExec.exe tool.

===============
Requirements:
===============

+ JDK 1.6 (32 and 64-bit)

+ 1.6 equivalent of JRockit (32 and 64-bit)

+ Browsers (IE 7+ , FF 3+)

===============
OS Platforms:
===============
+ Windows (32 and 64-bit)

+ Linux (32 and 64-bit)

===============
Notes:
===============
+ Use 64-bit JDK to monitor 64-bit JVMs and 32-bit JDK to monitor 32-bit JVMs
