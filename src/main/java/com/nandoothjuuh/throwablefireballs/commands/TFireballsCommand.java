package com.nandoothjuuh.throwablefireballs.commands;

import com.nandoothjuuh.throwablefireballs.ThrowableFireballsPlugin;
import com.nandoothjuuh.throwablefireballs.config.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TFireballsCommand implements CommandExecutor {

    private final ThrowableFireballsPlugin plugin;
    private final MessageManager messages;

    public TFireballsCommand(ThrowableFireballsPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(messages.getUsage());
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                return handleReload(sender);
            case "give":
                return handleGive(sender, args);
            case "blockdamage":
                return handleBlockDamage(sender, args);
            default:
                sender.sendMessage(messages.getUsage());
                return true;
        }
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("throwablefireballs.admin")) {
            sender.sendMessage(messages.getNoPermission());
            return true;
        }

        plugin.reload();
        sender.sendMessage(messages.getReloadSuccess());
        return true;
    }

    private boolean handleGive(CommandSender sender, String[] args) {
        if (!sender.hasPermission("throwablefireballs.admin")) {
            sender.sendMessage(messages.getNoPermission());
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(messages.getUsageGive());
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(messages.getPlayerNotFound());
            return true;
        }

        int amount = 1;
        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
                if (amount <= 0) {
                    sender.sendMessage(messages.getInvalidAmount());
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(messages.getInvalidAmount());
                return true;
            }
        }

        // Give the fireball item(s)
        ItemStack fireballItem = plugin.getItemFactory().createFireballItem(amount);
        target.getInventory().addItem(fireballItem);

        // Send messages
        sender.sendMessage(messages.getGiveSuccess(target.getName(), amount));
        target.sendMessage(messages.getReceiveFireballs(amount));

        return true;
    }

    private boolean handleBlockDamage(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.getPrefix() + "&cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("throwablefireballs.blockdamage.toggle")) {
            player.sendMessage(messages.getNoPermission());
            return true;
        }

        // Check if per-player toggle is enabled in config
        if (!plugin.getConfigManager().allowPlayerBlockDamageToggle()) {
            player.sendMessage(messages.getBlockDamageToggleDisabled());
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(messages.getUsageBlockDamage());
            return true;
        }

        String toggle = args[1].toLowerCase();
        
        switch (toggle) {
            case "on":
            case "true":
            case "enable":
            case "enabled":
                plugin.setPlayerBlockDamagePreference(player.getUniqueId(), true);
                player.sendMessage(messages.getBlockDamageEnabled());
                break;
            case "off":
            case "false":
            case "disable":
            case "disabled":
                plugin.setPlayerBlockDamagePreference(player.getUniqueId(), false);
                player.sendMessage(messages.getBlockDamageDisabled());
                break;
            default:
                player.sendMessage(messages.getUsageBlockDamage());
                break;
        }

        return true;
    }
}
