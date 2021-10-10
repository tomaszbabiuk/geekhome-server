rm -rf /home/pi/geekhome
mkdir /home/pi/geekhome
cp scripts/geekhomerealm.properties /home/pi/geekhome
cp scripts/log4j2.xml /home/pi/geekhome
./gradlew :final-geekhome:shadowJar
cp final-geekhome/build/libs/final-geekhome-all.jar /home/pi/geekhome
