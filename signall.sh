cd build/output
mkdir final-geekhome-signed
mkdir final-geekheat-signed
mkdir update-geekhome-signed
mkdir update-geekheat-signed
cd ..

echo Signing final-geekheat
jarsigner -keystore geekhome.jks -storepass geekhome -keypass geekhome -signedjar output/final-geekheat-signed/final-dependencies-1.0-signed.jar output/final-geekheat/final-dependencies-1.0-jar-with-dependencies.jar heatsigning

jarsigner -keystore geekhome.jks -storepass geekhome -keypass geekhome -signedjar output/final-geekheat-signed/final-geekheat-obfuscated-signed.jar output/final-geekheat/final-geekheat-obfuscated.jar heatsigning

echo Signing final-geekhome
jarsigner -keystore geekhome.jks -storepass geekhome -keypass geekhome -signedjar output/final-geekhome-signed/final-dependencies-1.0-signed.jar output/final-geekhome/final-dependencies-1.0-jar-with-dependencies.jar homesigning

jarsigner -keystore geekhome.jks -storepass geekhome -keypass geekhome -signedjar output/final-geekhome-signed/final-geekhhome-obfuscated-signed.jar output/final-geekhome/final-geekhome-obfuscated.jar homesigning

echo Signing update-geekheat
jarsigner -keystore geekhome.jks -storepass geekhome -keypass geekhome -signedjar output/update-geekheat-signed/update-dependencies-1.0-signed.jar output/update-geekheat/update-dependencies-1.0-jar-with-dependencies.jar heatsigning

jarsigner -keystore geekhome.jks -storepass geekhome -keypass geekhome -signedjar output/update-geekheat-signed/update-geekheat-obfuscated-signed.jar output/update-geekheat/update-geekheat-obfuscated.jar heatsigning

echo Signing update-geekhome
jarsigner -keystore geekhome.jks -storepass geekhome -keypass geekhome -signedjar output/update-geekhome-signed/update-dependencies-1.0-signed.jar output/update-geekhome/update-dependencies-1.0-jar-with-dependencies.jar homesigning

jarsigner -keystore geekhome.jks -storepass geekhome -keypass geekhome -signedjar output/update-geekhome-signed/update-geekhhome-obfuscated-signed.jar output/update-geekhome/update-geekhome-obfuscated.jar homesigning
