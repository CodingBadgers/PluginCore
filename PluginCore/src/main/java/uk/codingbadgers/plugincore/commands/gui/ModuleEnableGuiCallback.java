package uk.codingbadgers.plugincore.commands.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import uk.codingbadgers.plugincore.commands.ModulesCommandHandler;
import uk.codingbadgers.plugincore.gui.GuiCallback;
import uk.codingbadgers.plugincore.gui.GuiInventory;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;

import java.io.File;

public class ModuleEnableGuiCallback implements GuiCallback {

    private final File m_moduleFile;
    private final ModuleLoader m_moduleLoader;
    private final ModulesCommandHandler m_modulesCommandHandler;

    public ModuleEnableGuiCallback(ModulesCommandHandler modulesCommandHandler, ModuleLoader moduleLoader, File moduleFile) {
        m_moduleFile = moduleFile;
        m_moduleLoader = moduleLoader;
        m_modulesCommandHandler = modulesCommandHandler;
    }

    @Override
    public void onClick(GuiInventory inventory, InventoryClickEvent clickEvent) {
        Module module = m_moduleLoader.getModule(m_moduleFile);
        if (module == null) {
            return; // Module isn't loaded
        }

        if (module.isEnabled()) {
            return;
        }

        m_moduleLoader.enableModule(module);
        m_modulesCommandHandler.showModulesGui((Player)clickEvent.getWhoClicked());
    }
}
