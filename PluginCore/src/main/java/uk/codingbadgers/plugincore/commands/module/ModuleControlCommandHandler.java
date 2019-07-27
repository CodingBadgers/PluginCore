package uk.codingbadgers.plugincore.commands.module;

import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.commands.SubCommandHandler;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;

public class ModuleControlCommandHandler extends SubCommandHandler {

    private final Module m_module;

    public ModuleControlCommandHandler(PluginCore plugin, Module module) {
        super(plugin.getMessageSystem());
        m_module = module;

        ModuleLoader moduleLoader = plugin.getModuleLoader();
        registerSubCommand("unload", new UnloadModuleCommandHandler(moduleLoader, module.getName()));
        registerSubCommand("load", new LoadModuleCommandHandler(moduleLoader, module));
        registerSubCommand("reload", new ReloadModuleCommandHandler(moduleLoader, module));
        registerSubCommand("disable", new DisableModuleCommandHandler(moduleLoader, module.getName()));
        registerSubCommand("enable", new EnableModuleCommandHandler(moduleLoader, module.getName()));
    }

    @Override
    public String getHelpMessage() {
        return m_module.getDescription().getDescription();
    }
}
