cd build
rm -rf to-obfuscate /s /q
mkdir to-obfuscate

rm -rf output /s /q
mkdir output
cd output
mkdir final-geekhome
mkdir final-geekheat
mkdir update-geekhome
mkdir update-geekheat

cd ../..

echo Packaging final dependencies
cd dependencies-final
mvn assembly:assembly
cp target/*jar-with-dependencies.jar ../build/output/final-geekhome
cp target/*jar-with-dependencies.jar ../build/output/final-geekheat
mvn clean
cd ..

echo Packaging update dependencies
cd dependencies-update
mvn assembly:assembly
cp target/*jar-with-dependencies.jar ../build/output/update-geekhome
cp target/*jar-with-dependencies.jar ../build/output/update-geekheat
mvn clean
cd ..

echo Packaging update geekhome
cd update-geekhome
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging update geekheat
cd update-geekheat
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging library module
cd library-http
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging library update
cd library-update
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging synchronization module
cd module-synchronization
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging core module
cd module-core
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging alarm module
cd module-alarm
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging automation module
cd module-automation
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging central heating module
cd module-centralheating
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging email module
cd module-email
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging extafree module
cd module-extafree
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging hardwaremanager module
cd module-hardwaremanager
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging lights module
cd module-lights
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging onewire module
cd module-onewire
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging pi4j module
cd module-pi4j
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging users module
cd module-users
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging ventilation module
cd module-ventilation
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging firebase module
cd module-firebase
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging mdns module
cd module-mdns
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging mqtt module
cd module-mqtt
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging geekhome module
cd final-geekhome
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..

echo Packaging geekheat module
cd final-geekheat
mvn clean package -Ddir="../build/to-obfuscate"
mvn clean
cd ..
