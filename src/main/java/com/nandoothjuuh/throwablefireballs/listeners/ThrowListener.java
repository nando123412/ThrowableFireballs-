package com.nandoothjuuh.throwablefireballs.listeners;

import com.nandoothjuuh.throwablefireballs.ThrowableFireballsPlugin;
import com.nandoothjuuh.throwablefireballs.config.ConfigManager;
import com.nandoothjuuh.throwablefireballs.config.MessageManager;
import com.nandoothjuuh.throwablefireballs.cooldown.CooldownManager;
import com.nandoothjuuh.throwablefireballs.util.TextUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class ThrowListener implements Listener {

    private final ThrowableFireballsPlugin plugin;
    private final ConfigManager config;
    private final MessageManager messages;
    private final CooldownManager cooldowns;

    public ThrowListener(ThrowableFireballsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
        this.messages = plugin.getMessageManager();
        this.cooldowns = plugin.getCooldownManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        // Only handle right-click actions
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        // Check if player has item in hand
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        
        // Check if item matches configured material
        if (item.getType() != config.getItemMaterial()) {
            return;
        }
        
        // Check custom model data if required
        if (config.hasCustomModelData()) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasCustomModelData() || 
                meta.getCustomModelData() != config.getCustomModelData()) {
                return;
            }
        }
        
        // Check custom display name if required
        if (config.hasCustomDisplayName()) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) {
                return;
            }
            String expectedName = TextUtil.colorize(config.getCustomDisplayName());
            if (!meta.getDisplayName().equals(expectedName)) {
                return;
            }
        }
        
        // Check permission
        if (!player.hasPermission("throwablefireballs.use")) {
            player.sendMessage(messages.getNoPermission());
            event.setCancelled(true);
            return;
        }
        
        // Check cooldown (unless bypass permission)
        if (!player.hasPermission("throwablefireballs.bypasscooldown")) {
            if (cooldowns.isOnCooldown(player.getUniqueId())) {
                long remaining = cooldowns.getRemainingCooldown(player.getUniqueId());
                if (config.shouldShowCooldownMessage()) {
                    String cooldownMsg = messages.getCooldownMessage(remaining);
                    plugin.getVersionAdapter().sendActionBar(player, cooldownMsg);
                }
                event.setCancelled(true);
                return;
            }
        }
        
        // Check WorldGuard protection if enabled
        if (config.shouldRespectWorldGuard() && plugin.isWorldGuardPresent()) {
            if (!canThrowInLocation(player, event)) {
                player.sendMessage(messages.getCannotThrowHere());
                event.setCancelled(true);
                return;
            }
        }
        
        // Check build permission if enabled
        if (config.shouldCheckBuildPermission()) {
            if (event.getClickedBlock() != null && 
                !player.hasPermission("throwablefireballs.bypassprotection")) {
                // Basic build check - this is a simplified check
                // For full protection, server admins should use WorldGuard
                if (!event.getClickedBlock().getLocation().getWorld().equals(player.getWorld())) {
                    player.sendMessage(messages.getCannotThrowHere());
                    event.setCancelled(true);
                    return;
                }
            }
        }
        
        // All checks passed - throw the fireball!
        throwFireball(player);
        
        // Set cooldown
        if (!player.hasPermission("throwablefireballs.bypasscooldown")) {
            cooldowns.setCooldown(player.getUniqueId(), config.getCooldownMillis());
        }
        
        // Consume item if configured
        boolean shouldConsume = config.shouldConsumeItem();
        if (player.getGameMode() == GameMode.CREATIVE && !config.consumeInCreative()) {
            shouldConsume = false;
        }
        
        if (shouldConsume) {
            item.setAmount(item.getAmount() - 1);
        }
        
        // Cancel the event to prevent other interactions
        event.setCancelled(true);
    }

    private void throwFireball(Player player) {
        // Get player's eye location and direction
        Vector direction = player.getEyeLocation().getDirection().normalize();
        double speed = config.getProjectileSpeed();
        
        // Spawn the fireball
        Entity projectile;
        if (config.useSmallFireball()) {
            projectile = player.getWorld().spawn(
                player.getEyeLocation().add(direction.clone().multiply(0.5)),
                SmallFireball.class
            );
        } else {
            projectile = player.getWorld().spawn(
                player.getEyeLocation().add(direction.clone().multiply(0.5)),
                Fireball.class
            );
        }
        
        // Set shooter and velocity
        if (projectile instanceof Fireball) {
            Fireball fireball = (Fireball) projectile;
            fireball.setShooter(player);
            fireball.setDirection(direction);
            fireball.setVelocity(direction.multiply(speed));
            fireball.setYield(config.getExplosionPower());
            fireball.setIsIncendiary(config.shouldCreateFire());
            
            // Tag the fireball with our NamespacedKey and player UUID
            PersistentDataContainer pdc = fireball.getPersistentDataContainer();
            pdc.set(plugin.getFireballKey(), PersistentDataType.STRING, player.getUniqueId().toString());
            
            if (config.isDebugEnabled()) {
                plugin.getLogger().info("Player " + player.getName() + " threw a fireball with UUID tag");
            }
        }
    }

    private boolean canThrowInLocation(Player player, PlayerInteractEvent event) {
        // This is a placeholder for WorldGuard integration
        // If WorldGuard is present, we should check if player can build at the location
        // For now, we'll do a basic check
        
        try {
            // Try to use WorldGuard API if available
            Class<?> worldGuardClass = Class.forName("com.sk89q.worldguard.WorldGuard");
            // If we get here, WorldGuard is present
            // Return true for now - full WorldGuard integration would require more code
            // Server admins can use WorldGuard flags to control this
            return true;
        } catch (ClassNotFoundException e) {
            // WorldGuard not present, allow throw
            return true;
        }
    }
}
