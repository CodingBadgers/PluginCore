package uk.codingbadgers.plugincore.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.gui.callback.GuiReturnCallback;

public class GuiInventorySubMenu extends GuiSubInventory {

    public GuiInventorySubMenu(PluginCore plugin, GuiInventory ownerMenu, String title, int rowCount) {
        super(plugin, ownerMenu, title, rowCount);
        this.addMenuItem("Back", new ItemStack(Material.NETHER_STAR), new String[]{"Return to", ownerMenu.getTitle()}, (rowCount - 1) * 9, new GuiReturnCallback(ownerMenu));
    }

}