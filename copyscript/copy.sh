
#code\src\main\java\com\emtechies\foodcourt\web\security

#get previous directory

projectDir="$(dirname "$PWD")"
echo $projectDir
projectBaseDir=$projectDir/src/main

packageBase=com/hepsibah
projectName=timetable
javaFileBaseDir=$projectBaseDir/java/$packageBase/$projectName
resourcesBaseDir=$projectBaseDir/resources
webappBaseDir=$projectBaseDir/webapp
srcFilesDir=`pwd`/files


#echo $srcFilesDir/authority.csv
#echo $resourcesBaseDir/config/liquibase/data
#echo $webappBaseDir

cp -rf $srcFilesDir/authority.csv $resourcesBaseDir/config/liquibase/data 
cp -rf $srcFilesDir/application-dev.yml $resourcesBaseDir/config
cp -rf $srcFilesDir/AuthoritiesConstants.java $javaFileBaseDir/web/security 
cp -rf $srcFilesDir/banner.txt $resourcesBaseDir 

#LOGIN WEB PAGE 
echo "Copying LOGIN WEB PAGE..."
cp  -rf $srcFilesDir/web/login/* $webappBaseDir/app/login

#MAIN WEB PAGE
# echo "Copying MAIN WEB PAGE..."
# cp  -rf $srcFilesDir/web/main.component.html $webappBaseDir/app/layouts/main 

#HOME WEB PAGE
# echo "Copying HOME WEB PAGE..."
# cp  -rf $srcFilesDir/web/home/* $webappBaseDir/app/home 

#COPYING IMAGES
echo "Copying IMAGES..."
cp -rf $srcFilesDir/images/web/* $webappBaseDir/content/images



