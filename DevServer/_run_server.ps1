#####################################
## Local Dev Server Startup Script ##
#####################################

## Configuration ##
$paperMcDownload = "https://papermc.io/api/v1/paper/1.14.4/148/download"
## End Configuration ##

# Remove all plugins, or create the plugins directory if it does not exist
if (Test-Path .\plugins)
{
    Remove-Item .\plugins\*.*
}
else
{
    New-Item .\plugins -ItemType Directory
}

# Remove all modules, or create the modules directory if it does not exist
if (Test-Path .\modules)
{
    Remove-Item .\modules\*.*
}
else
{
    New-Item .\modules -ItemType Directory
}

# Download PaperMc if it is out of date or missing
$currentPaperMcVersion = ""

if (Test-Path .\papermc.jar -PathType Leaf)
{
    if (Test-Path .\papermc_version.txt -PathType Leaf)
    {
        $currentPaperMcVersion = Get-Content .\papermc_version.txt
        echo "Found existing PaperMc version: $currentPaperMcVersion"
    }
}

if ($currentPaperMcVersion -ne $paperMcDownload)
{
    echo "Downloading PaperMc from $paperMcDownload"
    Invoke-WebRequest $paperMcDownload -OutFile .\papermc.jar
    $paperMcDownload | Out-File -FilePath .\papermc_version.txt
    echo "Downloaded PaperMc"
}
else
{
    echo "PaperMc is already up-to-date"
}

# Copy all plugins external_plugins
if (Test-Path .\external_plugins)
{
    Copy-Item ".\external_plugins\*.jar" -Destination ".\plugins"
}

# Copy PluginCore to the plugins directory 
Copy-Item "..\jars\PluginCore-*-SNAPSHOT.jar" -Destination ".\plugins\PluginCore.jar"

# Copy all modules to the modules directory
Copy-Item "..\jars\*Module-*-SNAPSHOT.jar" -Destination ".\modules"

# Start the server
Start-Process -FilePath "java" -ArgumentList "-jar papermc.jar" -NoNewWindow -PassThru -Wait
