@echo off
start /min
SET file_saved_in="C:\Users\Yehuda\Desktop\Logs\AP-logs\deviceutil%date:~-4,4%-%date:~-7,2%-%date:~-10,2%_%time:~-11,2%-%time:~-8,2%-%time:~-5,2%-%time:~-2,2%.txt"
copy /y NUL %file_saved_in% >NUL
echo file saved in: %file_saved_in%
CALL adb wait-for-device 
CALL START open_logs_baretail.bat %file_saved_in%
::remove SimpleRIL
SET adb_shell_command="logcat -v time | grep -E -i 'deviceutil'"
call adb shell %adb_shell_command% >> %file_saved_in%
