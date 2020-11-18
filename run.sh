cd ..
./gradlew :final-geekhome:shadowJar

cd final-geekhhome
java -jar build/libs/final-geekhome-all.jar -Dfile.encoding=UTF-8 -Dlog4j.configurationFile=/home/pi/geekhome/log4j2.xml -Djava.library.path=/usr/lib/jni