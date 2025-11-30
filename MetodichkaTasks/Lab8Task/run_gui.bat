@echo off
echo Compiling GUI Client...
if not exist bin mkdir bin
javac -d bin -sourcepath src -cp lib/* src/com/spaceport/client/GuiClient.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Copying resources...
if not exist bin\resources mkdir bin\resources
copy src\resources\*.properties bin\resources\ >nul

echo Starting GUI Client...
java -cp bin;lib/* com.spaceport.client.GuiClient
pause
