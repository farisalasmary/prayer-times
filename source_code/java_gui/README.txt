
# open the terminal and paste ALL of the following commands to create an executable jar file

mkdir build
javac *.java

mv *.class build/
cp athan.wav build/
cp settings.pryt build/
cp locations build/
cp icon.png build/
cd build/

echo Main-Class: MainGUI >manifest.txt

jar cvfm PrayerTimes.jar manifest.txt *
cp PrayerTimes.jar ../
cd ..
rm -R build/

java -jar PrayerTimes.jar

