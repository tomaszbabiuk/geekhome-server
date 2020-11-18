./gradlew :final-geekhome:shadowJar

mkdir /home/pi/geekhome -p
cp final-geekhome/build/libs/final-geekhome-all.jar /home/pi/geekhome
cp setup/geekhome/geekhomerealm.properties /home/pi/geekhome
cp setup/geekhome/log4j2.xml /home/pi/geekhome

cp setup/etc/init.d/geekhome /etc/init.d
cp setup/etc/udev/rules.d/50-geekhome-extafree.rules /etc/udev/rules.d/50-geekhome-extafree.rules
cp setup/etc/udev/rules.d/51-geekhome-1wire.rules /etc/udev/rules.d/51-geekhome-extafree.rules