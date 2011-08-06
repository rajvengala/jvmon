JVMON is a Java Virtual Machine(JVM) platform monitoring tool.

========
Features
========
	- gathers metrics about memory pools(heap & non-heap), classes, threads, garbage collectors
	- no need to configure or enable anything on target JVM which makes it ideal for monitoring production/running JVMs
	- saves all the collected metrics in csv format and js format
	- rotates csv files every 24 hours
	- automatically creates graphs for all the collected metrics using integrated web server component

============
Requirements
=============
	- JDK 1.6+ (32 and 64-bit)
	- 1.6+ equivalent of JRockit (32 and 64-bit)
	- Browsers (IE 7+ , FF 3+)
