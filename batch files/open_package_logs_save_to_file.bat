@echo off

SET file_saved_in="C:\Users\Yehuda\Desktop\Logs\AP-logs\ALL_%date:~-4,4%-%date:~-7,2%-%date:~-10,2%_%time:~-11,2%-%time:~-8,2%-%time:~-5,2%-%time:~-2,2%.txt"
copy /y NUL %file_saved_in% >NUL
echo file saved in: %file_saved_in%
call adb wait-for-device 
call start open_logs_baretail.bat %file_saved_in%
SET adb_shell_command="logcat -v time | grep -E -i 'package'"
call adb shell %adb_shell_command% >> %file_saved_in%


