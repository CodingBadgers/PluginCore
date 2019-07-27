package uk.codingbadgers.plugincore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.io.File;

public class ModuleCommandHandler implements ICommandHandler {

    private final ModuleLoader m_moduleLoader;

    ModuleCommandHandler(PluginCore plugin) {
        m_moduleLoader = plugin.getModuleLoader();
    }

    @Override
    public String getHelpMessage() {
        return "Allows interaction with modules, such as stop, start, restart and update.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            ShowModuleHelp(messageSystem, sender);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (args.length >= 2) {
                ReloadModule(messageSystem, sender, args[1]);
                return;
            }

            ReloadModules(messageSystem, sender);
        } else {
            ShowModuleHelp(messageSystem, sender);
        }
    }

    private void ReloadModules(MessageSystem messageSystem, CommandSender sender) {
        m_moduleLoader.disableModules();
        m_moduleLoader.unloadModules();
        m_moduleLoader.loadModules();
        m_moduleLoader.enableModules();
        messageSystem.SendMessage(sender, "Reloaded all modules (" + m_moduleLoader.getModules().size() + " loaded)");
    }

    private void ReloadModule(MessageSystem messageSystem, CommandSender sender, String moduleName) {
        Module module = m_moduleLoader.getModule(moduleName);

        if (module == null) {
            messageSystem.SendMessage(sender, "Could not find module with name " + moduleName);
            return;
        }

        File moduleFile = module.getFile();

        module.setEnabled(false);
        m_moduleLoader.unloadModule(module);

        module = m_moduleLoader.loadModule(moduleFile);
        module.setEnabled(true);
        messageSystem.SendMessage(sender, "Reloaded " + moduleName);
    }

    private void ShowModuleHelp(MessageSystem messageSystem, CommandSender sender) {
        messageSystem.SendMessage(sender, ChatColor.GOLD + "reload" + ChatColor.RESET + " - Stops the module, then reloads and starts the module.");
        messageSystem.SendMessage(sender, ChatColor.GOLD + "start" + ChatColor.RESET + " - Starts a module that is not already running.");
        messageSystem.SendMessage(sender, ChatColor.GOLD + "stop" + ChatColor.RESET + " - Stops a running module.");
        messageSystem.SendMessage(sender, ChatColor.GOLD + "update <url>" + ChatColor.RESET + " - Stops a running module, downloads a Jar file from a provided Url, starts the updated module.");
    }
}
