rm -rf bin
mkdir bin
cp scripts/geekhomerealm.properties bin
cp scripts/log4j2.xml bin
./gradlew :final-geekhome:shadowJar
cp final-geekhome/build/libs/final-geekhome-all.jar bin
