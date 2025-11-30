@echo off
if not exist "bin" mkdir bin
javac -d bin src/com/spaceport/*.java src/com/spaceport/model/*.java src/com/spaceport/command/*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
java -cp bin com.spaceport.Main
pause
