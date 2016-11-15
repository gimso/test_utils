adb wait-for-device
::adb shell am broadcast -a simgo.servers.TUNNELING -e "tunneling_action" "6"

for /l %%x in (1,1,1000) do (
echo %%x reset
adb shell am broadcast -a simgo.vsim.TUNNELING -e "tunnel_outgoing" "10030B00021A2400"
timeout /t 2
adb shell am broadcast -a simgo.vsim.TUNNELING -e "tunnel_incoming" "1001010002128100"
timeout /t 30
)
::
::adb shell am broadcast -a simgo.vsim.TUNNELING -e "tunnel_outgoing" "10030B00021A2400"