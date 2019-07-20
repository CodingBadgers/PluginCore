package uk.codingbadgers.plugincore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.gui.GuiInventory;
import uk.codingbadgers.plugincore.gui.GuiSubInventory;
import uk.codingbadgers.plugincore.gui.callback.GuiReturnCallback;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleDescriptionFile;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.ArrayList;
import java.util.List;

public class ModuleCommandHandler implements ICommandHandler {

    private final PluginCore m_plugin;
    private final ModuleLoader m_moduleLoader;

    public ModuleCommandHandler(PluginCore plugin) {
        m_plugin = plugin;
        m_moduleLoader = plugin.getModuleLoader();
        CreateModuleGui();
    }

    @Override
    public void Handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            if (sender instanceof Player) {
                ShowModuleGui((Player)sender);
            } else {
                ShowModuleHelp(messageSystem, sender);
            }
            return;
        }

    }

    private void ShowModuleGui(Player player) {
        GuiInventory moduleInventory = CreateModuleGui();
        moduleInventory.open(player);
    }

    private void ShowModuleHelp(MessageSystem messageSystem, CommandSender sender) {
        messageSystem.SendMessage(sender, ChatColor.GOLD + "reload" + ChatColor.RESET + " - Stops the module, then reloads and starts the module.");
        messageSystem.SendMessage(sender, ChatColor.GOLD + "start" + ChatColor.RESET + " - Starts a module that is not already running.");
        messageSystem.SendMessage(sender, ChatColor.GOLD + "stop" + ChatColor.RESET + " - Stops a running module.");
        messageSystem.SendMessage(sender, ChatColor.GOLD + "update <url>" + ChatColor.RESET + " - Stops a running module, downloads a Jar file from a provided Url, starts the updated module.");
    }

    private GuiInventory CreateModuleGui() {
        List<Module> modules = m_moduleLoader.getModules();

        GuiInventory moduleInventory = new GuiInventory(m_plugin);
        moduleInventory.createInventory("Module Manager", (modules.size() / 9) + 1);

        for (Module module : m_moduleLoader.getModules()) {
            ModuleDescriptionFile description = module.getDescription();

            GuiSubInventory moduleGui = new GuiSubInventory(m_plugin, moduleInventory, description.getName(), 1);
            moduleGui.addMenuItem("Stop Module", new ItemStack(Material.RED_TERRACOTTA), null, 3, null);
            moduleGui.addMenuItem("Start Module", new ItemStack(Material.LIME_TERRACOTTA), null, 4, null);
            moduleGui.addMenuItem("Restart Module", new ItemStack(Material.YELLOW_TERRACOTTA), null, 5, null);
            moduleGui.addMenuItem("Go Back", new ItemStack(Material.NETHER_STAR), null, 8, new GuiReturnCallback(moduleInventory));

            List<String> details = new ArrayList<>();
            details.add("Version: " + description.getVersion());
            details.add("Description: " + description.getDescription());
            moduleInventory.addSubMenuItem(description.getName(), Material.BLUE_WOOL, details, moduleGui);
        }

        return moduleInventory;
    }

}
