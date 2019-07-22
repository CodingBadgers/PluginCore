# PluginCore

## About
PluginCore is a Bukkit/Spigot plugin for MineCraft which adds an additional layer on top of the standard plugin API. PluginCore provides a system which allows the loading of Modules, which act the same as plugins but are much more fine grain and can be stopped/started/updated whilst a MineCraft server is still running.


## Installation
Simply copy the PluginCore.jar to the `plugins` directory on your Bukkit or Spigot server. The plugin will automatically create a `modules` directory along side the `plugins` folder where modules can be installed.

## Commands

 * `/plugincore list` - Lists all installed modules and their state
 * `/plugincore modules` - Opens the modules graphical user interface
 * `/plugincore module <module_name> <stop|start|restart|state|info>` - Allows interaction with a specific module

## Modules

The following official modules exist for PluginCore:

#### [ChatModule](https://github.com/CodingBadgers/PluginCore/tree/master/Modules/ChatModule)
Adds chat improvements including; direct messaging, group chat, chat channels, chat formatting (including username prefixes/suffixes and color code support) as well as chat spam protection.

#### [TestModule](https://github.com/CodingBadgers/PluginCore/tree/master/Modules/TestModule)
A module used for development testing and should not be used on live servers. This allows us to test new feature or changes to module manipulation without touching core modules. This is however a good example to look at if you are wanting to create your own module.
