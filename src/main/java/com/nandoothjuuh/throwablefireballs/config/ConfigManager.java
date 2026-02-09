package com.nandoothjuuh.throwablefireballs.config;

import com.nandoothjuuh.throwablefireballs.ThrowableFireballsPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final ThrowableFireballsPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(ThrowableFireballsPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    // Item settings
    
    public Material getItemMaterial() {
        String materialName = config.getString("item.material", "FIRE_CHARGE");
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid material: " + materialName + ", using FIRE_CHARGE");
            return Material.FIRE_CHARGE;
        }
    }
    
    public boolean hasCustomModelData() {
        return config.getBoolean("item.custom-model-data.enabled", false);
    }
    
    public int getCustomModelData() {
        return config.getInt("item.custom-model-data.value", 0);
    }
    
    public boolean hasCustomDisplayName() {
        return config.getBoolean("item.display-name.enabled", false);
    }
    
    public String getCustomDisplayName() {
        return config.getString("item.display-name.value", "&cFireball");
    }
    
    public boolean shouldConsumeItem() {
        return config.getBoolean("item.consume-on-throw", true);
    }
    
    public boolean consumeInCreative() {
        return config.getBoolean("item.consume-in-creative", false);
    }
    
    // Projectile settings
    
    public boolean useSmallFireball() {
        return config.getString("projectile.type", "SMALL").equalsIgnoreCase("SMALL");
    }
    
    public double getProjectileSpeed() {
        return config.getDouble("projectile.speed", 1.5);
    }
    
    // Explosion settings
    
    public boolean isBlockDamageEnabled() {
        return config.getBoolean("explosion.block-damage", false);
    }
    
    public boolean isEntityDamageEnabled() {
        return config.getBoolean("explosion.entity-damage", true);
    }
    
    public float getExplosionPower() {
        return (float) config.getDouble("explosion.power", 1.0);
    }
    
    public boolean shouldCreateFire() {
        return config.getBoolean("explosion.create-fire", true);
    }
    
    public int getFireTicks() {
        return config.getInt("explosion.fire-ticks", 100);
    }
    
    // Damage settings
    
    public boolean hasDirectHitDamage() {
        return config.getBoolean("damage.direct-hit.enabled", true);
    }
    
    public double getDirectHitDamage() {
        return config.getDouble("damage.direct-hit.amount", 5.0);
    }
    
    // Cooldown settings
    
    public long getCooldownMillis() {
        return config.getLong("cooldown.time-millis", 1000);
    }
    
    public boolean shouldShowCooldownMessage() {
        return config.getBoolean("cooldown.show-actionbar", true);
    }
    
    // Protection settings
    
    public boolean shouldRespectWorldGuard() {
        return config.getBoolean("protection.respect-worldguard", true);
    }
    
    public boolean shouldCheckBuildPermission() {
        return config.getBoolean("protection.check-build-permission", true);
    }
    
    // Permission toggle settings
    
    public boolean allowPlayerBlockDamageToggle() {
        return config.getBoolean("per-player-settings.allow-block-damage-toggle", true);
    }
    
    // Debug
    
    public boolean isDebugEnabled() {
        return config.getBoolean("debug", false);
    }
}
