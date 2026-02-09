package com.nandoothjuuh.throwablefireballs.compat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

/**
 * Modern version adapter for Paper 1.16.5+
 * Uses Adventure API (net.kyori.adventure) which is available on modern Paper
 */
public class ModernVersionAdapter implements VersionAdapter {

    private final LegacyComponentSerializer serializer;

    public ModernVersionAdapter() {
        // Initialize the legacy serializer for converting color codes
        this.serializer = LegacyComponentSerializer.legacyAmpersand();
    }

    @Override
    public void sendActionBar(Player player, String message) {
        try {
            // Convert legacy color codes to Component
            Component component = serializer.deserialize(message);
            // Send action bar using Adventure API
            player.sendActionBar(component);
        } catch (Exception e) {
            // Fallback - if something goes wrong, just send as chat message
            player.sendMessage(message);
        }
    }

    @Override
    public String getAdapterName() {
        return "ModernVersionAdapter (Adventure API - Paper 1.16.5+)";
    }
}
