package uk.codingbadgers.plugincore.commands;

import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.commands.module.ModuleControlCommandHandler;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;

class ModuleCommandHandler extends SubCommandHandler {

    ModuleCommandHandler(PluginCore plugin) {
        super(plugin.getMessageSystem());

        ModuleLoader moduleLoader = plugin.getModuleLoader();
        for (Module module : moduleLoader.getModules()) {
            registerSubCommand(module.getName(), new ModuleControlCommandHandler(plugin, module));
        }
    }

    @Override
    public String getHelpMessage() {
        return "Allows interaction with modules, such as unload, load, reload and update.";
    }
}
