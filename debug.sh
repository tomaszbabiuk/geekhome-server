cd /home/pi/geekhome
java -Xdebug -Xrunjdwp:transport=dt_socket,address=9000,server=y,suspend=y -Dfile.encoding=UTF-8 -Dlog4j.configurationFile=./log4j2.xml -Djava.library.path=/usr/lib/jni -jar final-geekhome-all.jar
