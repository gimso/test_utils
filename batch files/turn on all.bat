@echo off
adb shell am broadcast -a simgo.test --ei "set_action" 2
echo turn on 4g
adb shell am broadcast -a simgo.test --ei "set_action" 4
echo turn on 3g
adb shell am broadcast -a simgo.test --ei "set_action" 6
echo turn on hotspot
adb shell am broadcast -a simgo.test --ei "set_action" 12
echo connect the 4g
adb shell am broadcast -a simgo.test --ei "set_action" 13
echo connect the 3g
adb shell am broadcast -a simgo.test --ei "set_action" 27
echo Open the android settings page
PAUSE