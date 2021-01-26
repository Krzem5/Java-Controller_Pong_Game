echo off
echo NUL>_.class&&del /s /f /q *.class
cls
javac -cp com/krzem/controller_game/modules/purejavahidapi.jar;com/krzem/controller_game/modules/jna-4.0.0.jar; com/krzem/controller_game/Main.java&&java -cp com/krzem/controller_game/modules/purejavahidapi.jar;com/krzem/controller_game/modules/jna-4.0.0.jar; com/krzem/controller_game/Main
start /min cmd /c "echo NUL>_.class&&del /s /f /q *.class"