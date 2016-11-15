@echo off
:versions
echo wait-for-device
adb wait-for-device
echo Date and Time:
echo %date:~-4,4%.%date:~-7,2%.%date:~-10,2% %time:~-11,2%:%time:~-8,2%:%time:~-5,2%.%time:~-2,2%
echo.
echo S/N:
adb devices -l 
echo OS: 
adb shell getprop ro.build.oeminfo
echo ServerControl: 
adb shell dumpsys package com.simgo.servers | find "versionName"
echo VSimControl:
adb shell dumpsys package com.simgo.vsim | find "versionName"
echo GUI:
adb shell dumpsys package com.simgo.vphgui | find "versionName"
echo DeviceUtil:
adb shell dumpsys package com.simgo.deviceutil | find "versionName"
echo LogService:
adb shell dumpsys package com.simgo.logservice | find "versionName"
echo WebServer:
adb shell dumpsys package com.simgo.webserver | find "versionName"
ECHO Extra:
adb shell am broadcast -a simgo.servers.GET_INFO -e params get_all
echo.
PAUSE
echo click to check it again
echo.
echo.
echo.
echo =======================
goto :versions

