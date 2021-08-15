package me.gabrielsantos.minecraft.oreautosell.manager;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.gabrielsantos.minecraft.oreautosell.OreAutoSell;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public final class AutoSellManager {

    private final OreAutoSell plugin;

    private final Map<String, Double> multipliers = Maps.newLinkedHashMap();
    private final Map<Material, Double> prices = Maps.newLinkedHashMap();

    public void init() {
        loadMultipliers(false);
        loadPrices(false);
    }

    public void loadPrices(boolean fromReload) {
        final FileConfiguration pricesConfiguration = plugin.getConfigurationManager().getPricesConfiguration();
        final ConfigurationSection pricesSection = pricesConfiguration.getConfigurationSection("prices");

        if (pricesSection == null) return;

        final Stopwatch timer = Stopwatch.createStarted();

        if (fromReload) prices.clear();

        for (String key : pricesSection.getKeys(false)) {
            final Material material = Material.valueOf(key);
            final double price = pricesSection.getDouble(key);

            prices.put(material, price);
        }

        timer.stop();

        if (!fromReload) {
            final Logger logger = plugin.getLogger();

            logger.log(Level.INFO, "All block prices have been loaded and cached. ({0}ms)", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public void loadMultipliers(boolean fromReload) {
        final FileConfiguration multipliersConfiguration = plugin.getConfigurationManager().getMultipliersConfiguration();
        final ConfigurationSection multipliersSection = multipliersConfiguration.getConfigurationSection("multipliers");

        if (multipliersSection == null) return;

        final Stopwatch timer = Stopwatch.createStarted();

        if (fromReload) multipliers.clear();

        for (String key : multipliersSection.getKeys(false)) {
            multipliers.put(key, multipliersSection.getDouble(key));
        }

        timer.stop();

        if (!fromReload) {
            final Logger logger = plugin.getLogger();

            logger.log(Level.INFO, "All multipliers have been loaded and cached. ({0}ms)", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public double getBlockPriceWithMultiplier(Material type, double multiplier) {
        final double price = prices.getOrDefault(type, 0D);

        return Math.floor(price * multiplier);
    }

    public double getPlayerMultiplier(Player player) {
        double playerMultiplier = multipliers.getOrDefault("default", 1D);

        for (String multiplier : multipliers.keySet()) {
            if (player.hasPermission("oreautosell.multiplier." + multiplier)) {
                playerMultiplier = multipliers.get(multiplier);
            }
        }

        return playerMultiplier;
    }

}
