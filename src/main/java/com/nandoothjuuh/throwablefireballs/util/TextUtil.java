package com.nandoothjuuh.throwablefireballs.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

/**
 * Utility class for text formatting and colorization
 */
public class TextUtil {

    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = 
        LegacyComponentSerializer.legacyAmpersand();

    /**
     * Colorize a string with & color codes
     * @param text Text to colorize
     * @return Colorized text
     */
    public static String colorize(String text) {
        if (text == null) {
            return "";
        }
        
        // Try modern Adventure API first
        try {
            Component component = AMPERSAND_SERIALIZER.deserialize(text);
            return LegacyComponentSerializer.legacySection().serialize(component);
        } catch (Exception e) {
            // Fallback to ChatColor
            return ChatColor.translateAlternateColorCodes('&', text);
        }
    }

    /**
     * Strip all color codes from text
     * @param text Text to strip
     * @return Text without colors
     */
    public static String stripColor(String text) {
        if (text == null) {
            return "";
        }
        return ChatColor.stripColor(colorize(text));
    }

    /**
     * Convert a Component to legacy string
     * @param component Component to convert
     * @return Legacy string
     */
    public static String toLegacy(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    /**
     * Convert legacy string to Component
     * @param legacy Legacy string
     * @return Component
     */
    public static Component fromLegacy(String legacy) {
        return LegacyComponentSerializer.legacySection().deserialize(legacy);
    }
}
