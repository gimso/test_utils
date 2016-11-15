::set a=1;
:doit
adb shell am broadcast -a simgo.vsim.RESET_VSIM
timeout /t 10
::set a = %a%-1
goto :doit