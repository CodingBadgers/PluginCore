package uk.codingbadgers.plugincore.commands.module;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.commands.ICommandHandler;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.io.File;

public class LoadModuleCommandHandler implements ICommandHandler {

    private final ModuleLoader m_moduleLoader;
    private final File m_moduleFile;

    LoadModuleCommandHandler(ModuleLoader moduleLoader, File moduleFile) {
        m_moduleLoader = moduleLoader;
        m_moduleFile = moduleFile;
    }

    @Override
    public String getHelpMessage() {
        return "Load a module from disk if the module is not currently loaded.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        Module module = m_moduleLoader.getModule(m_moduleFile);
        if (module != null) {
            messageSystem.SendMessage(sender, "Module " + module.getName() + " is already loaded.");
            return;
        }

        module = m_moduleLoader.loadModule(m_moduleFile);
        module.setEnabled(true);

        messageSystem.SendMessage(sender, "Module " + module.getName() + " has been loaded.");
    }
}
