package com.nandoothjuuh.throwablefireballs.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    /**
     * Check if a player is on cooldown
     * @param playerId Player UUID
     * @return true if on cooldown, false otherwise
     */
    public boolean isOnCooldown(UUID playerId) {
        if (!cooldowns.containsKey(playerId)) {
            return false;
        }
        
        long lastUse = cooldowns.get(playerId);
        return System.currentTimeMillis() < lastUse;
    }

    /**
     * Get remaining cooldown time in milliseconds
     * @param playerId Player UUID
     * @return remaining time in millis, or 0 if not on cooldown
     */
    public long getRemainingCooldown(UUID playerId) {
        if (!cooldowns.containsKey(playerId)) {
            return 0;
        }
        
        long lastUse = cooldowns.get(playerId);
        long remaining = lastUse - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    /**
     * Set cooldown for a player
     * @param playerId Player UUID
     * @param durationMillis Cooldown duration in milliseconds
     */
    public void setCooldown(UUID playerId, long durationMillis) {
        long expiryTime = System.currentTimeMillis() + durationMillis;
        cooldowns.put(playerId, expiryTime);
    }

    /**
     * Remove cooldown for a player
     * @param playerId Player UUID
     */
    public void removeCooldown(UUID playerId) {
        cooldowns.remove(playerId);
    }

    /**
     * Clear all cooldowns
     */
    public void clearAll() {
        cooldowns.clear();
    }

    /**
     * Clean up expired cooldowns (should be called periodically)
     */
    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        cooldowns.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}
