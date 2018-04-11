@echo off
:run
if "%2" == "" goto end
    echo start %2
    c:/windows/system32/taskkill /F /IM %2.exe >nul 2>nul
    c:/windows/system32/choice /t 1 /d y /n >nul
    start /B %2 --Ice.Config=%1
    c:/windows/system32/choice /t 1 /d y /n >nul
    shift /2
goto run
:end
