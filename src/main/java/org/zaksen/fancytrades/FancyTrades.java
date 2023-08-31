package org.zaksen.fancytrades;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zaksen.fancytrades.command.Trade;
import org.zaksen.fancytrades.command.TradeCompleter;
import org.zaksen.fancytrades.events.TradeEvents;

public final class FancyTrades extends JavaPlugin {
    private static FancyTrades Instance;
    private static Logger LOGGER = LoggerFactory.getLogger("fancy_trades");

    public static FancyTrades getInstance() {
        return Instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        getCommand("trade").setExecutor(new Trade());
        getCommand("trade").setTabCompleter(new TradeCompleter());
        Bukkit.getPluginManager().registerEvents(new TradeEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
