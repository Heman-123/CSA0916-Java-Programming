@echo off
echo ================================================================================
echo  LAUNCHING SMART CAMPUS APPLET VIEWER (CO4 APPLET PROGRAMMING)
echo ================================================================================

if not exist bin\com\campus\lostfound\gui\SmartCampusApplet.class (
    echo [INFO] Binaries not found. Triggering compilation...
    call compile.bat
)

echo [INFO] Attempting to launch appletviewer on applet.html...
appletviewer -J-Djava.class.path="bin;lib/*;sqlite-jdbc-3.45.1.0.jar;slf4j-api-1.7.36.jar;slf4j-simple-1.7.36.jar" applet.html

if %ERRORLEVEL% neq 0 (
    echo [NOTE] appletviewer may not be included in standard JRE 11+. Launching desktop GUI launcher instead...
    java -cp "bin;lib/*;sqlite-jdbc-3.45.1.0.jar;slf4j-api-1.7.36.jar;slf4j-simple-1.7.36.jar" com.campus.lostfound.app.Main
)
