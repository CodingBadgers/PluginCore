package uk.codingbadgers.plugincore.commands;

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

public class ModulesCommandHandler implements ICommandHandler {

    private final PluginCore m_plugin;
    private final ModuleLoader m_moduleLoader;

    ModulesCommandHandler(PluginCore plugin) {
        m_plugin = plugin;
        m_moduleLoader = plugin.getModuleLoader();
    }

    @Override
    public String getHelpMessage() {
        return "Opens the modules graphical user interface.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        // Invalid args
        if (args.length != 0) {
            messageSystem.SendMessage(sender, "The 'modules' command does not take any arguments.");
            return;
        }

        // Command can only be ran by online players
        if (!(sender instanceof Player)) {
            messageSystem.SendMessage(sender, "The 'modules' command can only be executed by online players.");
            return;
        }

        ShowModulesGui((Player)sender);
    }

    private void ShowModulesGui(Player player) {
        GuiInventory moduleInventory = CreateModulesGui();
        moduleInventory.open(player);
    }

    private GuiInventory CreateModulesGui() {
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
