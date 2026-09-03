@echo off
echo ================================================================================
echo  RUNNING SMART CAMPUS ASSET RECOVERY SYSTEM (CLI DEMONSTRATION MODE)
echo ================================================================================

if not exist bin\com\campus\lostfound\app\Main.class (
    echo [INFO] Binaries not found. Triggering compilation...
    call compile.bat
)

java -cp "bin;lib/*;sqlite-jdbc-3.45.1.0.jar;slf4j-api-1.7.36.jar;slf4j-simple-1.7.36.jar" com.campus.lostfound.app.Main --cli
