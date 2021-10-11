cd /home/pi/geekhome
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:9000 -Dfile.encoding=UTF-8 -Dlog4j.configurationFile=./log4j2.xml -Djava.library.path=/usr/lib/jni -jar final-geekhome-all.jar
