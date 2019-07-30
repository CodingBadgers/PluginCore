package uk.codingbadgers.plugincore.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.commands.gui.*;
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
import uk.codingbadgers.plugincore.utilities.FileUtil;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModulesCommandHandler extends SubCommandHandler {

    private final PluginCore m_plugin;
    private final ModuleLoader m_moduleLoader;

    ModulesCommandHandler(PluginCore plugin) {
        super(plugin.getMessageSystem());

        m_plugin = plugin;
        m_moduleLoader = plugin.getModuleLoader();

        registerSubCommand("reload_all", new ReloadModulesCommandHandler(m_moduleLoader));
        registerSubCommand("disable_all", new DisableModulesCommandHandler(m_moduleLoader));
        registerSubCommand("enable_all", new EnableModulesCommandHandler(m_moduleLoader));
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
        File[] moduleFiles = m_moduleLoader.findAllModuleFiles();

        GuiInventory moduleInventory = new GuiInventory(m_plugin);
        moduleInventory.createInventory("Module Manager", (moduleFiles.length / 9) + 1);

        for (File moduleFile : moduleFiles) {

            boolean isModuleLoaded = m_moduleLoader.isModuleFileLoaded(moduleFile);
            boolean isModuleEnable = false;
            String moduleName = FileUtil.getNameWithoutExtension(moduleFile);
            String moduleVersion = "<unknown>";
            String moduleDescription = "<unknown>";
            Material moduleIconMaterial = Material.BEDROCK;

            if (isModuleLoaded) {
                Module loadedModule = m_moduleLoader.getModule(moduleFile);
                isModuleEnable = loadedModule.isEnabled();
                moduleName = loadedModule.getName();
                moduleVersion = loadedModule.getVersion();

                ModuleDescriptionFile description = loadedModule.getDescription();
                moduleDescription = description.getDescription();
                moduleIconMaterial = description.getIcon();
            }

            GuiSubInventory moduleGui = new GuiSubInventory(m_plugin, moduleInventory, moduleName, 1);
            moduleGui.addMenuItem("Disable Module", GetIcon(Material.RED_TERRACOTTA, isModuleLoaded && isModuleEnable), null, 2, new ModuleDisableGuiCallback(this, m_moduleLoader, moduleFile));
            moduleGui.addMenuItem("Enable Module", GetIcon(Material.LIME_TERRACOTTA, isModuleLoaded && !isModuleEnable), null, 3, new ModuleEnableGuiCallback(this, m_moduleLoader, moduleFile));
            moduleGui.addMenuItem("Unload Module", GetIcon(Material.BLACK_TERRACOTTA, isModuleLoaded), null, 4, new ModuleUnloadGuiCallback(this, m_moduleLoader, moduleFile));
            moduleGui.addMenuItem("Load Module", GetIcon(Material.WHITE_TERRACOTTA, !isModuleLoaded), null, 5, new ModuleLoadGuiCallback(this, m_moduleLoader, moduleFile));
            moduleGui.addMenuItem("Reload Module", GetIcon(Material.YELLOW_TERRACOTTA, isModuleLoaded), null, 6, new ModuleReloadGuiCallback(this, m_moduleLoader, moduleFile));
            moduleGui.addMenuItem("Go Back", new ItemStack(Material.NETHER_STAR), null, 8, new GuiReturnCallback(moduleInventory));

            List<String> details = new ArrayList<>();
            details.add("Version: " + moduleVersion);
            details.add("Description: " + moduleDescription);
            details.add("Enabled: " + isModuleEnable);

            ItemStack moduleIcon = new ItemStack(moduleIconMaterial);
            if (isModuleEnable) {
                moduleIcon.addEnchantment(new GuiEnchantment(), 1);
            }

            moduleInventory.addSubMenuItem(moduleName, moduleIcon, details, moduleGui);
        }

        return moduleInventory;
    }

    private ItemStack GetIcon(Material activeMaterial, boolean isActive) {
        if (isActive) {
            return new ItemStack(activeMaterial);
        }
        return new ItemStack(Material.BARRIER);
    }

}
