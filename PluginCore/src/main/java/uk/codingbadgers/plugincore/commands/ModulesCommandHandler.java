package uk.codingbadgers.plugincore.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.commands.gui.ModuleReloadGuiCallback;
import uk.codingbadgers.plugincore.commands.gui.ModuleEnableGuiCallback;
import uk.codingbadgers.plugincore.commands.gui.ModuleDisableGuiCallback;
import uk.codingbadgers.plugincore.commands.modules.EnableModulesCommandHandler;
import uk.codingbadgers.plugincore.commands.modules.ReloadModulesCommandHandler;
import uk.codingbadgers.plugincore.commands.modules.DisableModulesCommandHandler;
import uk.codingbadgers.plugincore.gui.GuiInventory;
import uk.codingbadgers.plugincore.gui.GuiSubInventory;
import uk.codingbadgers.plugincore.gui.callback.GuiReturnCallback;
import uk.codingbadgers.plugincore.gui.misc.GuiEnchantment;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleDescriptionFile;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.ArrayList;
import java.util.List;

public class ModulesCommandHandler extends SubCommandHandler {

    private final PluginCore m_plugin;
    private final ModuleLoader m_moduleLoader;

    ModulesCommandHandler(PluginCore plugin) {
        super(plugin.getMessageSystem());

        m_plugin = plugin;
        m_moduleLoader = plugin.getModuleLoader();

        registerSubCommand("reload", new ReloadModulesCommandHandler(m_moduleLoader));
        registerSubCommand("disable", new DisableModulesCommandHandler(m_moduleLoader));
        registerSubCommand("enable", new EnableModulesCommandHandler(m_moduleLoader));
    }

    @Override
    public String getHelpMessage() {
        return "Opens the modules graphical user interface.";
    }

    @Override
    public void handle(MessageSystem messageSystem, CommandSender sender, Command command, String label, String[] args) {
        // Sub command specified
        if (args.length != 0) {
            super.handle(messageSystem, sender, command, label, args);
            return;
        }

        // Command can only be ran by online players
        if (!(sender instanceof Player)) {
            messageSystem.SendMessage(sender, "The 'modules' command can only be executed by online players.");
            return;
        }

        showModulesGui((Player)sender);
    }

    public void showModulesGui(Player player) {
        GuiInventory moduleInventory = createModulesGui();
        moduleInventory.open(player);
    }

    private GuiInventory createModulesGui() {
        List<Module> modules = m_moduleLoader.getModules();

        GuiInventory moduleInventory = new GuiInventory(m_plugin);
        moduleInventory.createInventory("Module Manager", (modules.size() / 9) + 1);

        for (Module module : m_moduleLoader.getModules()) {
            ModuleDescriptionFile description = module.getDescription();

            GuiSubInventory moduleGui = new GuiSubInventory(m_plugin, moduleInventory, description.getName(), 1);
            moduleGui.addMenuItem("Disable Module", new ItemStack(Material.RED_TERRACOTTA), null, 3, new ModuleDisableGuiCallback(this, m_moduleLoader, module));
            moduleGui.addMenuItem("Enable Module", new ItemStack(Material.LIME_TERRACOTTA), null, 4, new ModuleEnableGuiCallback(this, m_moduleLoader, module));
            moduleGui.addMenuItem("Reload Module", new ItemStack(Material.YELLOW_TERRACOTTA), null, 5, new ModuleReloadGuiCallback(this, m_moduleLoader, module));
            moduleGui.addMenuItem("Go Back", new ItemStack(Material.NETHER_STAR), null, 8, new GuiReturnCallback(moduleInventory));

            List<String> details = new ArrayList<>();
            details.add("Version: " + description.getVersion());
            details.add("Description: " + description.getDescription());
            details.add("Enabled: " + module.isEnabled());

            ItemStack moduleIcon = new ItemStack(description.getIcon());
            if (module.isEnabled()) {
                moduleIcon.addEnchantment(new GuiEnchantment(), 1);
            }

            moduleInventory.addSubMenuItem(description.getName(), moduleIcon, details, moduleGui);
        }

        return moduleInventory;
    }

}
