package uk.codingbadgers.plugincore.commands;

import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.commands.module.ModuleControlCommandHandler;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;

import java.io.File;

class ModuleCommandHandler extends SubCommandHandler {

    ModuleCommandHandler(PluginCore plugin) {
        super(plugin.getMessageSystem());

        ModuleLoader moduleLoader = plugin.getModuleLoader();
        for (File moduleFile : moduleLoader.findAllModuleFiles()) {
            registerSubCommand(moduleFile.getName(), new ModuleControlCommandHandler(plugin, moduleFile));
        }
    }

    @Override
    public String getHelpMessage() {
        return "Allows interaction with modules, such as unload, load, reload and update.";
    }
}
