@echo off
echo Compiling Lab 6 Task Server...
javac -d bin src/com/spaceport/common/*.java src/com/spaceport/command/*.java src/com/spaceport/model/*.java src/com/spaceport/server/*.java
if %errorlevel% neq 0 (
    pause
    exit /b %errorlevel%
)

echo Starting Spaceport Server...
java -cp bin com.spaceport.server.Server
pause
