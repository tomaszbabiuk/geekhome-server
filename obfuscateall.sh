cd build
java -jar proguard.jar @obfuscate-final-geekhome.pro
java -jar proguard.jar @obfuscate-update-geekhome.pro

java -jar proguard.jar @obfuscate-final-geekheat.pro
java -jar proguard.jar @obfuscate-update-geekheat.pro
cd ..