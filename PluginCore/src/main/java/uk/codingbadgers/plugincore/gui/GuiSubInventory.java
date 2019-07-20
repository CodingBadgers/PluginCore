package uk.codingbadgers.plugincore.gui;

import uk.codingbadgers.plugincore.PluginCore;

public class GuiSubInventory extends GuiInventory {

    final private GuiInventory m_ownerMenu;

    public GuiSubInventory(PluginCore plugin, GuiInventory ownerMenu, String title, int rowCount) {
        super(plugin);
        this.createInventory(title, rowCount);
        m_ownerMenu = ownerMenu;
    }

    public String getOwnerTitle() {
        return m_ownerMenu.getOwnerTitle();
    }

}