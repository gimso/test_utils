@ECHO off
:: set the file path
SET file_path=C:\Users\Yehuda\Desktop\Logs\AP-logs\
:: set the file name (header + date)
SET file_name=%file_path%All_vphTest_%date:~-4,4%-%date:~-7,2%-%date:~-10,2%_%time:~-11,2%-%time:~-8,2%-%time:~-5,2%-%time:~-2,2%.txt
:: create the file
COPY /y NUL %file_name% >NUL
:: print the file location
ECHO file saved in: %file_name%
:: wait for the device
CALL adb wait-for-device 
:: run the open_logs_baretail.bat file with the file name parameter to open the file from baretail
CALL START open_logs_baretail.bat %file_name%
:: set the logcat filter for the components
SET adb_shell_command="logcat -v time | grep -E 'VSimControl|ServersControl|SimpleRIL|VirtualSim|AndroidRuntime|GUI'"
:: start logcat and save the file
CALL adb shell %adb_shell_command% >> %file_name%
