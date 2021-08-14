package me.gabrielsantos.minecraft.oreautosell;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class OreAutoSell extends JavaPlugin {

    public static OreAutoSell getInstance() {
        return getPlugin(OreAutoSell.class);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        try {
            getLogger().info("Plugin enabled successfully.");
        } catch (Throwable t) {
            t.printStackTrace();
            getLogger().info("An error occurred during a plugin initialization.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

}
