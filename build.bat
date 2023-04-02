@echo off
gradle clean build
if %errorlevel% NEQ 0 goto :fail
gradle bootRun
goto :end

:fail
echo Build failed!
exit /b 1

:end
echo Build succeeded!
