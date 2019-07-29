package uk.codingbadgers.plugincore.commands.module;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.commands.ICommandHandler;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.io.File;

public class EnableModuleCommandHandler implements ICommandHandler {

    private final ModuleLoader m_moduleLoader;
    private final File m_moduleFile;

    EnableModuleCommandHandler(ModuleLoader moduleLoader, File moduleFile) {
        m_moduleLoader = moduleLoader;
        m_moduleFile = moduleFile;
    }

    @Override
    public String getHelpMessage() {
        return "Enables a disabled module.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        Module module = m_moduleLoader.getModule(m_moduleFile);
        if (module == null) {
            messageSystem.SendMessage(sender, m_moduleFile.getName() + " isn't currently loaded.");
            return;
        }

        if (module.isEnabled()) {
            messageSystem.SendMessage(sender, module.getName() + " is already enabled.");
            return;
        }

        module.setEnabled(true);
        messageSystem.SendMessage(sender, "Enabled " + module.getName());
    }
}
