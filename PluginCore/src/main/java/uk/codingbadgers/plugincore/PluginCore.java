package uk.codingbadgers.plugincore;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import uk.codingbadgers.plugincore.commands.CommandManager;
import uk.codingbadgers.plugincore.modules.ModuleLoader;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommandSystem;
import uk.codingbadgers.plugincore.utilities.MessageSystem;

import java.util.logging.Level;

public class PluginCore extends JavaPlugin {

    private final MessageSystem m_messageSystem;
    private ModuleLoader m_moduleLoader;
    private ModuleCommandSystem m_commandSystem;

    private Economy m_economy = null;
    private Permission m_permissions = null;
    private Chat m_chat = null;

    public PluginCore() {
        m_messageSystem = new MessageSystem(this);
    }

    @Override
    public void onLoad() {
        m_moduleLoader = new ModuleLoader(this, "modules");
        m_commandSystem = new ModuleCommandSystem(this);

        m_moduleLoader.load();
    }

    @Override
    public void onEnable() {
        setupVault();

        getCommand("PluginCore").setExecutor(new CommandManager(this, m_messageSystem));

        m_moduleLoader.enable();

        // Done
        getLogger().log(Level.INFO, "Enabled " + getDescription().getName() + " version: " + getDescription().getVersion() );
    }

    @Override
    public void onDisable() {
        m_moduleLoader.disable();

        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }

    private void setupVault() {
        ServicesManager servicesManager = getServer().getServicesManager();

        RegisteredServiceProvider<Economy> eco_rsp = servicesManager.getRegistration(Economy.class);
        if (eco_rsp != null) {
            m_economy = eco_rsp.getProvider();
        }

        RegisteredServiceProvider<Permission> permissions_rsp = servicesManager.getRegistration(Permission.class);
        if (permissions_rsp != null) {
            m_permissions = permissions_rsp.getProvider();
        }

        RegisteredServiceProvider<Chat> chat_rsp = servicesManager.getRegistration(Chat.class);
        if (chat_rsp != null) {
            m_chat = chat_rsp.getProvider();
        }
    }

    public ModuleLoader getModuleLoader() {
        return m_moduleLoader;
    }

    public ModuleCommandSystem getCommandSystem() {
        return m_commandSystem;
    }
    public Economy getVaultEconomy() {
        return m_economy;
    }

    public Permission getVaultPermissions() {
        return m_permissions;
    }

    public Chat getVaultChat() {
        return m_chat;
    }
}
