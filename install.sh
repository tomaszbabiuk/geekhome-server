rm -rf bin
mkdir bin
./gradlew :final-geekhome:shadowJar
cp final-geekhome/build/libs/final-geekhome-all.jar bin
cp scripts/log4j2.xml bin
cp geekhomerealm.properties bin
cp scripts/geekhome /etc/init.d
cp scripts/50-geekhome-extafree.rules /etc/udev/rules.d
cp scripts/51-geekhome-1wire.rules /etc/udev/rules.d
