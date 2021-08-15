package me.gabrielsantos.minecraft.oreautosell.listener;

import lombok.RequiredArgsConstructor;
import me.gabrielsantos.minecraft.oreautosell.hook.EconomyHook;
import me.gabrielsantos.minecraft.oreautosell.manager.AutoSellManager;
import me.gabrielsantos.minecraft.oreautosell.util.ActionBarUtil;
import me.gabrielsantos.minecraft.oreautosell.util.NumberFormatter;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public final class OreBreakListener implements Listener {

    private final FileConfiguration configuration;
    private final AutoSellManager autoSellManager;
    private final EconomyHook economy;

    @EventHandler
    public void handleOreBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        final List<String> allowedWorlds = configuration.getStringList("allowed-worlds");

        if (!allowedWorlds.contains(block.getWorld().getName())) return;

        final double playerMultiplier = autoSellManager.getPlayerMultiplier(player);

        final double blockPrice = autoSellManager.getBlockPriceWithMultiplier(block.getType(), playerMultiplier);

        if (blockPrice == 0) return;

        economy.depositCoins(player, blockPrice);

        final String formattedPrice = NumberFormatter.format(blockPrice);

        final String message = Objects.requireNonNull(configuration.getString("message"))
            .replace("{moneyEarned}", formattedPrice)
            .replace("{multiplier}", NumberFormatter.format(playerMultiplier));

        ActionBarUtil.sendActionBar(player, message);
    }

}
