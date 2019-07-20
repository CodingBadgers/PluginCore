package uk.codingbadgers.plugincore.gui;

import java.util.*;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.codingbadgers.plugincore.PluginCore;

public class GuiInventory implements Listener {
    private final PluginCore m_plugin;
    private String m_title;
    private int m_rowCount;

    private final Map<String, GuiSubInventory> m_subMenus;
    private final Map<String, ItemStack> m_items;
    private final Map<Integer, GuiCallback> m_callbacks;
    private Inventory m_inventory;

    public GuiInventory(PluginCore plugin) {
        m_plugin = plugin;

        m_plugin.getServer().getPluginManager().registerEvents(this, m_plugin);

        m_subMenus = new HashMap<String, GuiSubInventory>();
        m_items = new HashMap<String, ItemStack>();
        m_callbacks = new HashMap<Integer, GuiCallback>();
    }

    public void createInventory(String title, int rowCount) {
        m_title = title;
        m_rowCount = rowCount;

        if (rowCount > 6) {
            Bukkit.getLogger().log(Level.SEVERE, "rowCount can not be greater that 6.");
        }

        m_inventory = Bukkit.createInventory(null, m_rowCount * 9, m_title);
    }

    public void createInventory(String title, InventoryType type) {
        m_title = title;
        m_inventory = Bukkit.createInventory(null, type);
    }

    public void open(Player player) {
        player.openInventory(m_inventory);
    }

    public void close(Player player, boolean force) {
        if (player.getInventory() == m_inventory || force) {
            player.closeInventory();
            player.updateInventory();
        }
    }

    public void close(Player player) {
        close(player, false);
    }

    public String getTitle() {
        return m_title;
    }

    public String getOwnerTitle() {
        return m_title;
    }

    public void addSubMenuItem(String name, Material icon, List<String> details, GuiSubInventory subInventory) {

        if (m_subMenus.containsKey(name)) {
            Bukkit.getLogger().log(Level.WARNING, "A submenu called '" + name + "' already exists in '" + m_title + "'.");
            return;
        }

        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(details);
        item.setItemMeta(meta);

        m_inventory.addItem(item);
        m_subMenus.put(name, subInventory);

    }

    public ItemStack addMenuItem(String name, ItemStack icon, String[] details, GuiCallback callback) {

        int slot = 0;
        while (m_inventory.getItem(slot) != null) {
            slot++;
        }

        return addMenuItem(name, icon, details, slot, callback);

    }

    public ItemStack addMenuItem(String name, ItemStack icon, String[] details, int slot, GuiCallback callback) {

        return addMenuItem(name, icon, details, slot, 1, callback);
    }

    public ItemStack addMenuItem(String name, ItemStack icon, String[] details, int slot, int amount, GuiCallback callback) {

        ItemStack item = icon.clone();
        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (details != null) {
            meta.setLore(Arrays.asList(details));
        } else {
            meta.setLore(new ArrayList<>());
        }
        item.setItemMeta(meta);

        m_inventory.setItem(slot, item);
        if (callback != null) {
            m_callbacks.put(slot, callback);
        }

        m_items.put(name, item);

        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // Do we care about this inventory?
        Inventory inventory = event.getInventory();
        if (inventory != m_inventory) { // Used to be a name check, this may fail
            return;
        }

        // Do we care about this player?
        Player player = (Player) event.getWhoClicked();

        boolean isViewer = false;
        for (HumanEntity entity : m_inventory.getViewers()) {
            if (entity.getName().equalsIgnoreCase(player.getName())) {
                isViewer = true;
                break;
            }
        }

        if (!isViewer) {
            return;
        }

        if (event.getSlotType() != SlotType.CONTAINER) {
            return;
        }

        InventoryView iv = event.getView();
        if (event.getRawSlot() >= iv.getTopInventory().getSize()) {
            return;
        }

        // If they haven't clicked an item, quit
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getItemMeta() == null || clickedItem.getItemMeta().getDisplayName() == null) {
            return;
        }

        // Always cancel the event
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
        player.updateInventory();

        // Get the name of the item
        final String itemName = clickedItem.getItemMeta().getDisplayName();

        // If the item is a submenu, close this and open the submenu
        if (m_subMenus.containsKey(itemName)) {
            GuiInventory subMenu = m_subMenus.get(itemName);
            subMenu.open(player);
            return;
        }

        final int itemSlot = event.getSlot();

        // Item is a normal item, call its gui callback method
        if (m_callbacks.containsKey(itemSlot)) {
            GuiCallback callback = m_callbacks.get(itemSlot);
            callback.onClick(this, event);
            return;
        }
    }

    public GuiInventory getSubMenu(String name) {
        return m_subMenus.get(name);
    }

    public ItemStack getItem(String name) {
        return m_items.get(name);
    }
}
