package uk.codingbadgers.plugincore.commands.module;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.commands.ICommandHandler;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.io.File;

public class ReloadModuleCommandHandler implements ICommandHandler {

    private final ModuleLoader m_moduleLoader;
    private final File m_moduleFile;
    private final String m_moduleName;

    ReloadModuleCommandHandler(ModuleLoader moduleLoader, Module module) {
        m_moduleLoader = moduleLoader;
        m_moduleFile = module.getFile();
        m_moduleName = module.getName();
    }

    @Override
    public String getHelpMessage() {
        return "Reload an already loaded module with the latest version from disk.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {

        Module module = m_moduleLoader.getModule(m_moduleName);
        if (module != null) {
            module.setEnabled(false);
            m_moduleLoader.unloadModule(module);
        }

        module = m_moduleLoader.loadModule(m_moduleFile);
        module.setEnabled(true);
        messageSystem.SendMessage(sender, "Reloaded " + m_moduleName);
    }
}
