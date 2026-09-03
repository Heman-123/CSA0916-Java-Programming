@echo off
echo ================================================================================
echo  COMPILING SMART CAMPUS LOST ^& FOUND AND ASSET RECOVERY SYSTEM
echo ================================================================================

if not exist bin mkdir bin
if not exist data mkdir data

javac -encoding UTF-8 -d bin -cp ".;lib/*;sqlite-jdbc-3.45.1.0.jar" src/com/campus/lostfound/model/*.java src/com/campus/lostfound/exception/*.java src/com/campus/lostfound/security/*.java src/com/campus/lostfound/db/*.java src/com/campus/lostfound/dao/*.java src/com/campus/lostfound/io/*.java src/com/campus/lostfound/service/*.java src/com/campus/lostfound/gui/*.java src/com/campus/lostfound/app/*.java test/com/campus/lostfound/test/*.java

if %ERRORLEVEL% equ 0 (
    echo [SUCCESS] Compilation completed successfully! Class files placed in bin/
) else (
    echo [ERROR] Compilation failed with error code %ERRORLEVEL%
)
