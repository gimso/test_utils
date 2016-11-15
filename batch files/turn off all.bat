@echo off
adb shell am broadcast -a simgo.test --ei "set_action" 3
echo turn off 4g
adb shell am broadcast -a simgo.test --ei "set_action" 5
echo turn off 3g
adb shell am broadcast -a simgo.test --ei "set_action" 7
echo turn off hotspot
adb shell am broadcast -a simgo.test --ei "set_action" 10
echo disconnect the 4g
adb shell am broadcast -a simgo.test --ei "set_action" 11
echo disconnect the 3g

PAUSE