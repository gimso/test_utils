
:again
set A=adb wait-for-device
%A%
if %A% != "1"  do(goto :again)
       
done
echo done