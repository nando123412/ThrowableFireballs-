package com.nandoothjuuh.throwablefireballs.compat;

import org.bukkit.entity.Player;

/**
 * Version adapter interface to handle API differences across Minecraft versions
 */
public interface VersionAdapter {
    
    /**
     * Send an action bar message to a player
     * @param player The player to send the message to
     * @param message The message to send (already colorized)
     */
    void sendActionBar(Player player, String message);
    
    /**
     * Get the adapter version name for logging
     * @return Adapter name
     */
    default String getAdapterName() {
        return this.getClass().getSimpleName();
    }
}
