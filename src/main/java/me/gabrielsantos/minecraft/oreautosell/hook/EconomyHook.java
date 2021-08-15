package me.gabrielsantos.minecraft.oreautosell.hook;

import lombok.RequiredArgsConstructor;
import me.gabrielsantos.minecraft.oreautosell.OreAutoSell;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public final class EconomyHook {

    private final Logger logger;

    private Economy economy;

    public void init() {
        final RegisteredServiceProvider<Economy> registration = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (registration == null) {
            logger.severe("No economy plugin found on the server! Disabling the plugin...");
            Bukkit.getPluginManager().disablePlugin(OreAutoSell.getInstance());
        } else {
            economy = registration.getProvider();
            logger.log(Level.INFO, "Successfully linked economy plugin! ({0})", registration.getPlugin().getName());
        }
    }

    public void depositCoins(OfflinePlayer player, double amount) {
        economy.depositPlayer(player, amount);
    }

}
