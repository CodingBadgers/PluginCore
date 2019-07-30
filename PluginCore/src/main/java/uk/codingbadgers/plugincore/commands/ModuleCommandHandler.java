package uk.codingbadgers.plugincore.commands;

import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.commands.module.ModuleControlCommandHandler;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.FileUtil;

import java.io.File;

class ModuleCommandHandler extends SubCommandHandler {

    ModuleCommandHandler(PluginCore plugin) {
        super(plugin.getMessageSystem());

        ModuleLoader moduleLoader = plugin.getModuleLoader();
        for (File moduleFile : moduleLoader.findAllModuleFiles()) {
            registerSubCommand(FileUtil.getNameWithoutExtension(moduleFile), new ModuleControlCommandHandler(plugin, moduleFile));
        }
    }

    @Override
    public String getHelpMessage() {
        return "Allows interaction with modules, such as unload, load, reload and update.";
    }
}
