#!/usr/bin/env bash

cd library-http
mvn clean install
cd ..

cd library-onewire
mvn clean install
cd ..

cd library-update
mvn clean install
cd ..

cd module-synchronization
mvn clean install
cd ..

cd module-core
mvn clean install
cd ..

cd module-alarm
mvn clean install
cd ..

cd module-automation
mvn clean install
cd ..

cd module-centralheating
mvn clean install
cd ..

cd module-email
mvn clean install
cd ..

cd module-extafree
mvn clean install
cd ..

cd module-hardwaremanager
mvn clean install
cd ..

cd module-lights
mvn clean install
cd ..

cd module-onewire
mvn clean install
cd ..

echo Installing pi4j module
cd module-pi4j
mvn clean install
cd ..

cd module-users
mvn clean install
cd ..

cd module-ventilation
mvn clean install
cd ..

cd module-mqtt
mvn clean install
cd ..

cd module-firebase
mvn clean install
cd ..

cd module-gree
mvn clean install
cd ..

cd module-afore
mvn clean install
cd ..

cd module-shelly
mvn clean install
cd ..

