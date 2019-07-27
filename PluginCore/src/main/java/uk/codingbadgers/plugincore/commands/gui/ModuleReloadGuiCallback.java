package uk.codingbadgers.plugincore.commands.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import uk.codingbadgers.plugincore.commands.ModulesCommandHandler;
import uk.codingbadgers.plugincore.gui.GuiCallback;
import uk.codingbadgers.plugincore.gui.GuiInventory;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;

public class ModuleReloadGuiCallback implements GuiCallback {

    private final Module m_module;
    private final ModuleLoader m_moduleLoader;
    private final ModulesCommandHandler m_modulesCommandHandler;

    public ModuleReloadGuiCallback(ModulesCommandHandler modulesCommandHandler, ModuleLoader moduleLoader, Module module) {
        m_module = module;
        m_moduleLoader = moduleLoader;
        m_modulesCommandHandler = modulesCommandHandler;
    }

    @Override
    public void onClick(GuiInventory inventory, InventoryClickEvent clickEvent) {
        if (m_module.isEnabled()) {
            m_moduleLoader.disableModule(m_module);
        }
        m_moduleLoader.enableModule(m_module);
        m_modulesCommandHandler.showModulesGui((Player)clickEvent.getWhoClicked());
    }
}
