package uk.codingbadgers.plugincore.commands.module;

import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.commands.SubCommandHandler;
import uk.codingbadgers.plugincore.modules.ModuleLoader;

import java.io.File;

public class ModuleControlCommandHandler extends SubCommandHandler {

    public ModuleControlCommandHandler(PluginCore plugin, File moduleFile) {
        super(plugin.getMessageSystem());

        ModuleLoader moduleLoader = plugin.getModuleLoader();
        registerSubCommand("unload", new UnloadModuleCommandHandler(moduleLoader, moduleFile));
        registerSubCommand("load", new LoadModuleCommandHandler(moduleLoader, moduleFile));
        registerSubCommand("reload", new ReloadModuleCommandHandler(moduleLoader, moduleFile));
        registerSubCommand("disable", new DisableModuleCommandHandler(moduleLoader,moduleFile));
        registerSubCommand("enable", new EnableModuleCommandHandler(moduleLoader, moduleFile));
    }

    @Override
    public String getHelpMessage() { return null; }
}
