package com.nandoothjuuh.throwablefireballs.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TFireballsTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("reload", "give", "blockdamage");
    private static final List<String> BLOCK_DAMAGE_OPTIONS = Arrays.asList("on", "off");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // First argument - suggest subcommands
            String input = args[0].toLowerCase();
            for (String subCmd : SUBCOMMANDS) {
                if (subCmd.startsWith(input)) {
                    // Check permissions
                    if (subCmd.equals("reload") || subCmd.equals("give")) {
                        if (sender.hasPermission("throwablefireballs.admin")) {
                            completions.add(subCmd);
                        }
                    } else if (subCmd.equals("blockdamage")) {
                        if (sender.hasPermission("throwablefireballs.blockdamage.toggle")) {
                            completions.add(subCmd);
                        }
                    }
                }
            }
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            
            if (subCommand.equals("give") && sender.hasPermission("throwablefireballs.admin")) {
                // Suggest online player names
                String input = args[1].toLowerCase();
                completions = Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
            } else if (subCommand.equals("blockdamage") && sender.hasPermission("throwablefireballs.blockdamage.toggle")) {
                // Suggest on/off
                String input = args[1].toLowerCase();
                for (String option : BLOCK_DAMAGE_OPTIONS) {
                    if (option.startsWith(input)) {
                        completions.add(option);
                    }
                }
            }
        } else if (args.length == 3) {
            String subCommand = args[0].toLowerCase();
            
            if (subCommand.equals("give") && sender.hasPermission("throwablefireballs.admin")) {
                // Suggest amount
                completions.addAll(Arrays.asList("1", "8", "16", "32", "64"));
            }
        }

        return completions;
    }
}
