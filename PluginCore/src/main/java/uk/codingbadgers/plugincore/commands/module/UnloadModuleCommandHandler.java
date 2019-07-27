package uk.codingbadgers.plugincore.commands.module;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.commands.ICommandHandler;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.io.File;

public class UnloadModuleCommandHandler implements ICommandHandler {

    private final ModuleLoader m_moduleLoader;
    private final String m_moduleName;

    UnloadModuleCommandHandler(ModuleLoader moduleLoader, String moduleName) {
        m_moduleLoader = moduleLoader;
        m_moduleName = moduleName;
    }

    @Override
    public String getHelpMessage() {
        return "Unload a loaded module from memory.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        Module module = m_moduleLoader.getModule(m_moduleName);
        if (module == null) {
            messageSystem.SendMessage(sender, "Module " + m_moduleName + " is already unloaded.");
            return;
        }

        module.setEnabled(false);
        m_moduleLoader.unloadModule(module);
        messageSystem.SendMessage(sender, "Module " + m_moduleName + " has been unloaded.");
    }
}
