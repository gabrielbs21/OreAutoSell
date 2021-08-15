package me.gabrielsantos.minecraft.oreautosell.command;

import lombok.RequiredArgsConstructor;
import me.gabrielsantos.minecraft.oreautosell.OreAutoSell;
import me.gabrielsantos.minecraft.oreautosell.configuration.ConfigurationManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public final class OreAutoSellCommand implements CommandExecutor {

    private final OreAutoSell plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(
                "",
                "§a§lOreAutoSell §8-§7 Help message",
                "",
                "§7/oas §8-§f Show this message",
                "§7/oas reload §8-§f Reload all the configuration files"
            );
            return true;
        }

        final String argument = args[0];

        if (argument.equalsIgnoreCase("reload") || argument.equalsIgnoreCase("rl")) {
            final ConfigurationManager configurationManager = plugin.getConfigurationManager();

            final String callbackMessage = configurationManager.tryReloadAllAndGiveCallbackMessage();

            sender.sendMessage(callbackMessage);

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid arguments! Use: /oreautosell or /oas for help.");
        }

        return false;
    }

}
