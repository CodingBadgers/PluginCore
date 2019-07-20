package uk.codingbadgers.plugincore.gui.misc;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.plugincore.PluginCore;

public class GuiEnchantment extends Enchantment {

    public GuiEnchantment(PluginCore plugin) {
        super(new NamespacedKey(plugin, "112"));
    }

    @Override
    public String getName() {
        return "GuiEnchantment";
    }

    @Override
    public int getMaxLevel() {
        return 128;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment e) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack is) {
        return true;
    }

}