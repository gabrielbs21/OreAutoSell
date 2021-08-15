package me.gabrielsantos.minecraft.oreautosell;

import lombok.Getter;
import me.gabrielsantos.minecraft.oreautosell.command.OreAutoSellCommand;
import me.gabrielsantos.minecraft.oreautosell.configuration.ConfigurationManager;
import me.gabrielsantos.minecraft.oreautosell.hook.EconomyHook;
import me.gabrielsantos.minecraft.oreautosell.listener.OreBreakListener;
import me.gabrielsantos.minecraft.oreautosell.manager.AutoSellManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class OreAutoSell extends JavaPlugin {

    private final AutoSellManager autoSellManager = new AutoSellManager(this);

    private final ConfigurationManager configurationManager = ConfigurationManager.of(this);

    private final EconomyHook economyHook = new EconomyHook(this.getLogger());

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        try {
            configurationManager.init();
            autoSellManager.init();
            economyHook.init();

            commands();
            listeners();

            getLogger().info("Plugin enabled successfully.");
        } catch (Throwable t) {
            t.printStackTrace();
            getLogger().info("An error occurred during a plugin initialization.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void listeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new OreBreakListener(getConfig(), this.autoSellManager, this.economyHook), this);
    }

    private void commands() {
        final PluginCommand command = getCommand("oreautosell");

        if (command == null) return;

        command.setExecutor(new OreAutoSellCommand(this));
    }

    public static OreAutoSell getInstance() {
        return getPlugin(OreAutoSell.class);
    }

}
