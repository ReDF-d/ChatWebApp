@ECHO OFF
FOR /f %%i IN ('dir /b /s *.jar') DO (
SET VERSION=%%~ni
)
java -jar %VERSION%.jar
pause