package uk.codingbadgers.plugincore.commands.module;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.commands.ICommandHandler;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

public class DisableModuleCommandHandler implements ICommandHandler {

    private final ModuleLoader m_moduleLoader;
    private final String m_moduleName;

    DisableModuleCommandHandler(ModuleLoader moduleLoader, String moduleName) {
        m_moduleLoader = moduleLoader;
        m_moduleName = moduleName;
    }

    @Override
    public String getHelpMessage() {
        return "Disables the module, but leaves it loaded in memory.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        Module module = m_moduleLoader.getModule(m_moduleName);

        if (!module.isEnabled()) {
            messageSystem.SendMessage(sender, m_moduleName + " is already disabled.");
            return;
        }

        module.setEnabled(false);
        messageSystem.SendMessage(sender, "Disabled " + m_moduleName);
    }
}
