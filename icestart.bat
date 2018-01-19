:run
if "%2" == "" goto end
    c:/windows/system32/taskkill /F /IM %2.exe
    %2 --Ice.Config=%1
    shift
goto run

:end
