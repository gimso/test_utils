adb wait-for-device
adb shell am broadcast -a simgo.servers.TUNNELING -e "tunneling_action" "0"
