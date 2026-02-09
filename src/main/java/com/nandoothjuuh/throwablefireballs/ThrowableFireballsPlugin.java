package com.nandoothjuuh.throwablefireballs;

import com.nandoothjuuh.throwablefireballs.commands.TFireballsCommand;
import com.nandoothjuuh.throwablefireballs.commands.TFireballsTabCompleter;
import com.nandoothjuuh.throwablefireballs.compat.LegacyVersionAdapter;
import com.nandoothjuuh.throwablefireballs.compat.ModernVersionAdapter;
import com.nandoothjuuh.throwablefireballs.compat.VersionAdapter;
import com.nandoothjuuh.throwablefireballs.config.ConfigManager;
import com.nandoothjuuh.throwablefireballs.config.MessageManager;
import com.nandoothjuuh.throwablefireballs.cooldown.CooldownManager;
import com.nandoothjuuh.throwablefireballs.items.FireballItemFactory;
import com.nandoothjuuh.throwablefireballs.listeners.ExplosionControlListener;
import com.nandoothjuuh.throwablefireballs.listeners.ThrowListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class ThrowableFireballsPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private MessageManager messageManager;
    private CooldownManager cooldownManager;
    private FireballItemFactory itemFactory;
    private VersionAdapter versionAdapter;
    private NamespacedKey fireballKey;
    
    // Per-player block damage preference (UUID -> enabled)
    private final Map<UUID, Boolean> playerBlockDamagePreference = new HashMap<>();
    
    // WorldGuard integration
    private boolean worldGuardPresent = false;

    @Override
    public void onEnable() {
        // Initialize version adapter
        versionAdapter = createVersionAdapter();
        getLogger().info("Using version adapter: " + versionAdapter.getClass().getSimpleName());
        
        // Create NamespacedKey for tagging projectiles
        fireballKey = new NamespacedKey(this, "throwable_fireball");
        
        // Initialize managers
        configManager = new ConfigManager(this);
        messageManager = new MessageManager(this);
        cooldownManager = new CooldownManager();
        itemFactory = new FireballItemFactory(this);
        
        // Check for WorldGuard
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardPresent = true;
            getLogger().info("WorldGuard detected! Region protection will be respected.");
        }
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new ThrowListener(this), this);
        getServer().getPluginManager().registerEvents(new ExplosionControlListener(this), this);
        
        // Register commands
        TFireballsCommand commandExecutor = new TFireballsCommand(this);
        TFireballsTabCompleter tabCompleter = new TFireballsTabCompleter();
        getCommand("tfireballs").setExecutor(commandExecutor);
        getCommand("tfireballs").setTabCompleter(tabCompleter);
        
        getLogger().info("ThrowableFireballs has been enabled!");
    }

    @Override
    public void onDisable() {
        // Clean up cooldowns
        if (cooldownManager != null) {
            cooldownManager.clearAll();
        }
        
        // Clear player preferences
        playerBlockDamagePreference.clear();
        
        getLogger().info("ThrowableFireballs has been disabled!");
    }
    
    /**
     * Create the appropriate version adapter based on server version
     */
    private VersionAdapter createVersionAdapter() {
        String version = getServer().getBukkitVersion();
        getLogger().info("Detected server version: " + version);
        
        try {
            // Try modern method first (1.16+)
            return new ModernVersionAdapter();
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Modern adapter unavailable, falling back to legacy", e);
            return new LegacyVersionAdapter();
        }
    }
    
    /**
     * Reload configuration
     */
    public void reload() {
        configManager.reload();
        messageManager.reload();
        getLogger().info("Configuration reloaded!");
    }
    
    // Getters
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public MessageManager getMessageManager() {
        return messageManager;
    }
    
    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
    
    public FireballItemFactory getItemFactory() {
        return itemFactory;
    }
    
    public VersionAdapter getVersionAdapter() {
        return versionAdapter;
    }
    
    public NamespacedKey getFireballKey() {
        return fireballKey;
    }
    
    public boolean isWorldGuardPresent() {
        return worldGuardPresent;
    }
    
    /**
     * Get player's block damage preference
     * @param playerId Player UUID
     * @return true if block damage is enabled, false if disabled, null if not set (use global config)
     */
    public Boolean getPlayerBlockDamagePreference(UUID playerId) {
        return playerBlockDamagePreference.get(playerId);
    }
    
    /**
     * Set player's block damage preference
     * @param playerId Player UUID
     * @param enabled true to enable block damage, false to disable
     */
    public void setPlayerBlockDamagePreference(UUID playerId, boolean enabled) {
        playerBlockDamagePreference.put(playerId, enabled);
    }
    
    /**
     * Clear player's block damage preference (will use global config)
     * @param playerId Player UUID
     */
    public void clearPlayerBlockDamagePreference(UUID playerId) {
        playerBlockDamagePreference.remove(playerId);
    }
    
    /**
     * Check if block damage should be enabled for a specific player's fireball
     * @param playerId Player UUID who threw the fireball
     * @return true if block damage should occur, false otherwise
     */
    public boolean shouldEnableBlockDamage(UUID playerId) {
        Boolean playerPref = getPlayerBlockDamagePreference(playerId);
        if (playerPref != null) {
            return playerPref;
        }
        // Fall back to global config
        return configManager.isBlockDamageEnabled();
    }
}
