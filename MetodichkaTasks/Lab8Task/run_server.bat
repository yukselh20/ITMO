@echo off
echo Compiling Lab 8 Task Server...
javac -d bin -cp "lib/*" src/com/spaceport/common/*.java src/com/spaceport/command/*.java src/com/spaceport/model/*.java src/com/spaceport/server/*.java src/com/spaceport/server/db/*.java src/com/spaceport/server/util/*.java
if %errorlevel% neq 0 (
    pause
    exit /b %errorlevel%
)

echo Starting Spaceport Server...
java -cp "bin;lib/*" com.spaceport.server.Server
pause
