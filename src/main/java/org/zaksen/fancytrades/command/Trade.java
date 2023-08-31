package org.zaksen.fancytrades.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zaksen.fancytrades.trade.TradeRequest;

public class Trade implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if(args.length > 0) {
            switch (args[0]) {
                case "accept": {
                    TradeRequest trade = TradeRequest.getByPlayer(player);
                    if(trade != null) {
                        trade.accept();
                    }
                    break;
                }
                case "decline": {
                    TradeRequest trade = TradeRequest.getByPlayer(player);
                    if(trade != null) {
                        trade.decline();
                    }
                    break;
                }
                case "sent": {
                    Player player2 = Bukkit.getPlayer(args[1]);
                    if(TradeRequest.getByPlayer(player) == null && TradeRequest.getByPlayer(player2) == null) {
                        if(player.isOnline() && player2.isOnline()) {
                            TradeRequest newRequest = new TradeRequest(player, player2);
                        }
                    } else {
                        player.sendMessage("[FT] у данного игрока уже есть запрос");
                    }
                    break;
                }
                default: {
                    player.sendMessage("[FT] такой команды не существует!");
                    break;
                }
            }
        }
        return true;
    }
}