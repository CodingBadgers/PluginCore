:: Delete existing plugins
if exist "plugins\PluginCore.jar" del /F "plugins\PluginCore.jar"

:: Delete existing modules
del /F "modules\*.jar"

:: Copy the core plugin and rename to not include version info
mkdir "plugins"
copy "..\jars\PluginCore-*.jar" "plugins\PluginCore.jar" /Y

:: Copy modules
mkdir "modules"
copy "..\jars\*Module*.jar" "modules" /Y

:: Start the server
java -jar spigot.jar