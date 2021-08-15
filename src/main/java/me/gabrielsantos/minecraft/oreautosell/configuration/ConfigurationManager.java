package me.gabrielsantos.minecraft.oreautosell.configuration;

import lombok.Data;
import lombok.SneakyThrows;
import me.gabrielsantos.minecraft.oreautosell.OreAutoSell;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Data(staticConstructor = "of")
public final class ConfigurationManager {

    private final OreAutoSell plugin;

    private FileConfiguration multipliersConfiguration;
    private FileConfiguration pricesConfiguration;

    public void init() {
        files();
    }

    private void files() {
        plugin.saveResource("multipliers.yml", false);
        plugin.saveResource("prices.yml", false);

        multipliersConfiguration = loadConfiguration("multipliers.yml");
        pricesConfiguration = loadConfiguration("prices.yml");
    }

    @SneakyThrows
    private FileConfiguration loadConfiguration(String name) {
        final File file = new File(plugin.getDataFolder(), name);

        return YamlConfiguration.loadConfiguration(file);
    }

    @SneakyThrows
    public String tryReloadAllAndGiveCallbackMessage() {
        final CompletableFuture<String> reloadFuture = CompletableFuture.supplyAsync(() -> {
            try {
                plugin.reloadConfig();

                this.multipliersConfiguration = loadConfiguration("multipliers.yml");
                this.pricesConfiguration = loadConfiguration("prices.yml");

                return ChatColor.GREEN + "All files was successfully reloaded.";
            } catch (Throwable t) {
                t.printStackTrace();
                return ChatColor.RED + "An error occurred while reloading the settings files.";
            }
        });

        return reloadFuture.get();
    }

}
