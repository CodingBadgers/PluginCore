package uk.codingbadgers.plugincore.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface GuiCallback {
    void onClick(GuiInventory inventory, InventoryClickEvent clickEvent);
}