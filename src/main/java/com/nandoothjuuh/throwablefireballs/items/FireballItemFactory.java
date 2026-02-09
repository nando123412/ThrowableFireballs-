package com.nandoothjuuh.throwablefireballs.items;

import com.nandoothjuuh.throwablefireballs.ThrowableFireballsPlugin;
import com.nandoothjuuh.throwablefireballs.config.ConfigManager;
import com.nandoothjuuh.throwablefireballs.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for creating throwable fireball items
 */
public class FireballItemFactory {

    private final ThrowableFireballsPlugin plugin;
    private final ConfigManager config;

    public FireballItemFactory(ThrowableFireballsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    /**
     * Create a fireball item with the specified amount
     * @param amount Number of items to create
     * @return ItemStack of fireballs
     */
    public ItemStack createFireballItem(int amount) {
        Material material = config.getItemMaterial();
        ItemStack item = new ItemStack(material, amount);
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        // Set custom display name if configured
        if (config.hasCustomDisplayName()) {
            String displayName = TextUtil.colorize(config.getCustomDisplayName());
            meta.setDisplayName(displayName);
        }

        // Set custom model data if configured
        if (config.hasCustomModelData()) {
            meta.setCustomModelData(config.getCustomModelData());
        }

        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(TextUtil.colorize("&7Right-click to throw!"));
        lore.add(TextUtil.colorize("&8Throwable Fireball"));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Create a single fireball item
     * @return ItemStack of one fireball
     */
    public ItemStack createFireballItem() {
        return createFireballItem(1);
    }

    /**
     * Check if an item is a valid fireball item
     * @param item Item to check
     * @return true if valid, false otherwise
     */
    public boolean isFireballItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        // Check material
        if (item.getType() != config.getItemMaterial()) {
            return false;
        }

        // Check custom model data if required
        if (config.hasCustomModelData()) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasCustomModelData() || 
                meta.getCustomModelData() != config.getCustomModelData()) {
                return false;
            }
        }

        // Check display name if required
        if (config.hasCustomDisplayName()) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) {
                return false;
            }
            String expectedName = TextUtil.colorize(config.getCustomDisplayName());
            if (!meta.getDisplayName().equals(expectedName)) {
                return false;
            }
        }

        return true;
    }
}
