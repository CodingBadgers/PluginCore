package uk.codingbadgers.plugincore.commands.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import uk.codingbadgers.plugincore.commands.ModulesCommandHandler;
import uk.codingbadgers.plugincore.gui.GuiCallback;
import uk.codingbadgers.plugincore.gui.GuiInventory;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.ModuleLoader;

import java.io.File;

public class ModuleLoadGuiCallback implements GuiCallback {

    private final File m_moduleFile;
    private final ModuleLoader m_moduleLoader;
    private final ModulesCommandHandler m_modulesCommandHandler;

    public ModuleLoadGuiCallback(ModulesCommandHandler modulesCommandHandler, ModuleLoader moduleLoader, File moduleFile) {
        m_moduleFile = moduleFile;
        m_moduleLoader = moduleLoader;
        m_modulesCommandHandler = modulesCommandHandler;
    }

    @Override
    public void onClick(GuiInventory inventory, InventoryClickEvent clickEvent) {
        if (m_moduleLoader.isModuleFileLoaded(m_moduleFile)) {
            return;
        }

        Module module = m_moduleLoader.loadModule(m_moduleFile);
        m_moduleLoader.enableModule(module);

        m_modulesCommandHandler.showModulesGui((Player)clickEvent.getWhoClicked());
    }
}
