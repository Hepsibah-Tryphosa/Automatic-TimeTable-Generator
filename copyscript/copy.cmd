@echo off
rem code\src\main\java\com\emtechies\foodcourt\web\security


set projectDir=d:\code\foodcourt
set projectBaseDir=%projectDir%\code\src\main

set packageBase=com\emtechies
set projectName=foodcourt
set javaFileBaseDir=%projectBaseDir%\java\%packageBase%\%projectName%
set resourcesBaseDir=%projectBaseDir%\resources
set webappBaseDir=%projectBaseDir%\webapp
set srcFilesDir=%projectDir%\copyscript\files


echo %srcFilesDir%\authority.csv
echo %resourcesBaseDir%\config\liquibase\data
echo %webappBaseDir%

COPY  %srcFilesDir%\authority.csv %resourcesBaseDir%\config\liquibase\data /y
COPY  %srcFilesDir%\application-dev.yml %resourcesBaseDir%\config\ /y
COPY  %srcFilesDir%\AuthoritiesConstants.java %javaFileBaseDir%\web\security /y
COPY  %srcFilesDir%\banner.txt %resourcesBaseDir% /y

REM LOGIN WEB PAGE 
ECHO "COPYING LOGIN WEB PAGE..."
COPY  %srcFilesDir%\web\login\login.component.scss %webappBaseDir%\app\login /y
COPY  %srcFilesDir%\web\login\login.component.html %webappBaseDir%\app\login /y

REM MAIN WEB PAGE
ECHO "COPYING MAIN WEB PAGE..."
COPY  %srcFilesDir%\web\main.component.html %webappBaseDir%\app\layouts\main /y

REM HOME WEB PAGE
ECHO "COPYING HOME WEB PAGE..."
COPY  %srcFilesDir%\web\home\* %webappBaseDir%\app\home /y


REM COPY IMAGES
ECHO "COPYING IMAGES..."
COPY %srcFilesDir%\images\web\* %webappBaseDir%\content\images /y



