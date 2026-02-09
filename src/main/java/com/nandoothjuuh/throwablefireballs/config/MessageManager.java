package com.nandoothjuuh.throwablefireballs.config;

import com.nandoothjuuh.throwablefireballs.ThrowableFireballsPlugin;
import com.nandoothjuuh.throwablefireballs.util.TextUtil;

public class MessageManager {

    private final ThrowableFireballsPlugin plugin;
    private ConfigManager config;

    public MessageManager(ThrowableFireballsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    public void reload() {
        this.config = plugin.getConfigManager();
    }

    private String getMessage(String path, String defaultMessage) {
        String message = plugin.getConfig().getString("messages." + path, defaultMessage);
        return TextUtil.colorize(message);
    }

    // General messages
    
    public String getPrefix() {
        return getMessage("prefix", "&8[&cFireballs&8]&r ");
    }
    
    public String getNoPermission() {
        return getPrefix() + getMessage("no-permission", "&cYou don't have permission to do that!");
    }
    
    public String getReloadSuccess() {
        return getPrefix() + getMessage("reload-success", "&aConfiguration reloaded successfully!");
    }
    
    public String getPlayerNotFound() {
        return getPrefix() + getMessage("player-not-found", "&cPlayer not found!");
    }
    
    public String getInvalidAmount() {
        return getPrefix() + getMessage("invalid-amount", "&cInvalid amount! Please use a positive number.");
    }
    
    // Throw messages
    
    public String getCooldownMessage(long remainingMillis) {
        double remainingSeconds = remainingMillis / 1000.0;
        String message = getMessage("cooldown", "&cWait {time}s before throwing another fireball!");
        return message.replace("{time}", String.format("%.1f", remainingSeconds));
    }
    
    public String getWrongItem() {
        return getPrefix() + getMessage("wrong-item", "&cYou need a fireball item to throw!");
    }
    
    public String getCannotThrowHere() {
        return getPrefix() + getMessage("cannot-throw-here", "&cYou cannot throw fireballs in this area!");
    }
    
    // Give command messages
    
    public String getGiveSuccess(String playerName, int amount) {
        String message = getMessage("give-success", "&aGave {amount} fireball(s) to {player}!");
        return getPrefix() + message.replace("{player}", playerName).replace("{amount}", String.valueOf(amount));
    }
    
    public String getReceiveFireballs(int amount) {
        String message = getMessage("receive-fireballs", "&aYou received {amount} fireball(s)!");
        return getPrefix() + message.replace("{amount}", String.valueOf(amount));
    }
    
    // Block damage toggle messages
    
    public String getBlockDamageEnabled() {
        return getPrefix() + getMessage("block-damage-enabled", "&aYour fireballs will now break blocks!");
    }
    
    public String getBlockDamageDisabled() {
        return getPrefix() + getMessage("block-damage-disabled", "&cYour fireballs will no longer break blocks, but will still damage entities.");
    }
    
    public String getBlockDamageToggleDisabled() {
        return getPrefix() + getMessage("block-damage-toggle-disabled", "&cBlock damage toggle is disabled by the server!");
    }
    
    // Usage messages
    
    public String getUsage() {
        return getPrefix() + getMessage("usage", "&eUsage: /tfireballs <reload|give|blockdamage>");
    }
    
    public String getUsageGive() {
        return getPrefix() + getMessage("usage-give", "&eUsage: /tfireballs give <player> [amount]");
    }
    
    public String getUsageBlockDamage() {
        return getPrefix() + getMessage("usage-blockdamage", "&eUsage: /tfireballs blockdamage <on|off>");
    }
}
