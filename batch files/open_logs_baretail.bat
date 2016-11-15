@echo off
Timeout /t 3 /nobreak >nul
set file_saved_in=%~1
start /B baretailpro "%file_saved_in%" && Timeout /t 3
exit