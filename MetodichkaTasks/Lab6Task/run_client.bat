@echo off
echo Compiling Lab 6 Task Client...
javac -d bin src/com/spaceport/common/*.java src/com/spaceport/client/*.java
if %errorlevel% neq 0 (
    pause
    exit /b %errorlevel%
)

echo Starting Spaceport Client...
java -cp bin com.spaceport.client.Client
pause
