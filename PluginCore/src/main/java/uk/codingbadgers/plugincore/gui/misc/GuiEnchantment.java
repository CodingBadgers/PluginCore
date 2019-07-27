package uk.codingbadgers.plugincore.gui.misc;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.plugincore.PluginCore;

import java.lang.reflect.Field;

public class GuiEnchantment extends Enchantment {

    private static NamespacedKey s_enchantmentKey;

    public static void Register(PluginCore plugin) {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);

            try {
                s_enchantmentKey = new NamespacedKey(plugin, "gui_enchantment");
                GuiEnchantment enchantment = new GuiEnchantment();
                Enchantment.registerEnchantment(enchantment);
            }
            catch (Exception e) {
                // TODO: Log
            }

            f.set(null, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GuiEnchantment() {
        super(s_enchantmentKey);
    }

    @Override
    public String getName() {
        return s_enchantmentKey.getKey();
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
        return EnchantmentTarget.ALL;
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