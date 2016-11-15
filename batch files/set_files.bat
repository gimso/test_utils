adb wait-for-device
adb shell am broadcast -a simgo.vsim.TUNNELING -e "tunnel_outgoing" "10050700150719027fff1a026f071c090849523000049960201f00" 
timeout /t 60 
PAUSE