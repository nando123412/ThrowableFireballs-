package com.nandoothjuuh.throwablefireballs.listeners;

import com.nandoothjuuh.throwablefireballs.ThrowableFireballsPlugin;
import com.nandoothjuuh.throwablefireballs.config.ConfigManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class ExplosionControlListener implements Listener {

    private final ThrowableFireballsPlugin plugin;
    private final ConfigManager config;

    public ExplosionControlListener(ThrowableFireballsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    /**
     * Handle fireball explosions - control block damage
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        
        // Check if this is one of our tagged fireballs
        if (!isOurFireball(entity)) {
            return;
        }
        
        // Get the player who threw this fireball
        UUID playerId = getFireballOwner(entity);
        if (playerId == null) {
            // No owner found, use global config
            if (!config.isBlockDamageEnabled()) {
                event.blockList().clear();
                event.setYield(0f);
            }
            return;
        }
        
        // Check if block damage should be enabled for this player
        boolean shouldDamageBlocks = plugin.shouldEnableBlockDamage(playerId);
        
        if (!shouldDamageBlocks) {
            // Clear block list to prevent block damage
            event.blockList().clear();
            event.setYield(0f);
            
            if (config.isDebugEnabled()) {
                plugin.getLogger().info("Prevented block damage from fireball owned by " + playerId);
            }
        }
        
        // Note: We do NOT cancel the event entirely, so entity damage still occurs
        // The explosion visual effect and knockback will still happen
    }

    /**
     * Handle entity damage from fireballs - can disable entity damage if configured
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        
        // Check if damage is from one of our fireballs
        if (!isOurFireball(damager)) {
            return;
        }
        
        // Check if entity damage is disabled globally
        if (!config.isEntityDamageEnabled()) {
            event.setCancelled(true);
            if (config.isDebugEnabled()) {
                plugin.getLogger().info("Cancelled entity damage (global setting)");
            }
            return;
        }
        
        // Apply custom direct hit damage if configured
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (config.hasDirectHitDamage()) {
                event.setDamage(config.getDirectHitDamage());
                if (config.isDebugEnabled()) {
                    plugin.getLogger().info("Applied custom direct hit damage: " + config.getDirectHitDamage());
                }
            }
        }
    }

    /**
     * Handle projectile hits - apply fire effects
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity projectile = event.getEntity();
        
        // Check if this is one of our fireballs
        if (!isOurFireball(projectile)) {
            return;
        }
        
        // Apply fire to hit entity if configured
        if (config.shouldCreateFire() && event.getHitEntity() != null) {
            event.getHitEntity().setFireTicks(config.getFireTicks());
        }
    }

    /**
     * Check if an entity is one of our tagged fireballs
     */
    private boolean isOurFireball(Entity entity) {
        if (entity == null) {
            return false;
        }
        
        // Must be a fireball type
        EntityType type = entity.getType();
        if (type != EntityType.FIREBALL && type != EntityType.SMALL_FIREBALL) {
            return false;
        }
        
        // Check for our tag
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.has(plugin.getFireballKey(), PersistentDataType.STRING);
    }

    /**
     * Get the UUID of the player who threw this fireball
     */
    private UUID getFireballOwner(Entity entity) {
        if (entity == null || !isOurFireball(entity)) {
            return null;
        }
        
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        String uuidString = pdc.get(plugin.getFireballKey(), PersistentDataType.STRING);
        
        if (uuidString == null) {
            return null;
        }
        
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid UUID in fireball tag: " + uuidString);
            return null;
        }
    }
}
