package org.zaksen.fancytrades.trade;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.zaksen.fancytrades.FancyTrades;
import org.zaksen.fancytrades.gui.TradeMenu;

import java.util.ArrayList;
import java.util.List;

public class TradeRequest {
    public static List<TradeRequest> requests = new ArrayList<>();
    public static TradeRequest getByPlayer(Player player) {
        for(TradeRequest request : requests) {
            if(request.fromPlayer == player || request.toPlayer == player) {
                return request;
            }
        }
        return null;
    }
    public static TradeRequest getByMenu(TradeMenu menu) {
        for(TradeRequest request : requests) {
            if(request.tradeMenu == menu) {
                return request;
            }
        }
        return null;
    }
    private final Player fromPlayer;
    private final Player toPlayer;
    private final TradeMenu tradeMenu;
    private RequestStatus requestStatus = RequestStatus.SENT;

    public TradeRequest(Player from, Player to) {
        this.fromPlayer = from;
        this.toPlayer = to;
        this.tradeMenu = new TradeMenu(from, to);
        requests.add(this);
        notifyPlayers();
        new BukkitRunnable() {
            @Override
            public void run() {
                if(requestStatus == RequestStatus.SENT) {
                    notAccepted();
                }
            }
        }.runTaskLater(FancyTrades.getInstance(), 400L);
    }

    private void notAccepted() {
        fromPlayer.sendMessage("[FT] ваш запрос не был принят!");
        requestStatus = RequestStatus.DISMISSED;
        tradeMenu.stopMenu();
        requests.remove(this);
    }

    private void notifyPlayers() {
        fromPlayer.sendMessage("[FT] Вы отправили запрос игроку " + toPlayer.getName());
        toPlayer.sendMessage("[FT] Вы получили запрос от игрока " + fromPlayer.getName());
        toPlayer.sendMessage("[FT] используйье /trade accept или /trade decline, чтобы прянить или отклонить запрос!");
    }

    public RequestStatus getStatus() {
        return requestStatus;
    }

    public void accept() {
        requestStatus = RequestStatus.ACCEPTED;
        tradeMenu.openMenu();
    }

    public void decline() {
        requestStatus = RequestStatus.DISMISSED;
        fromPlayer.sendMessage("[FT] Ваш запрос был откланён");
        tradeMenu.stopMenu();
        requests.remove(this);
    }

    public void stop() {
        requestStatus = RequestStatus.DISMISSED;
        tradeMenu.breakTrade();
        fromPlayer.sendMessage("[FT] Ваш запрос был прерван");
        tradeMenu.stopMenu();
        requests.remove(this);
    }

    public void end() {
        requestStatus = RequestStatus.ENDED;
        tradeMenu.makeTrade();
        fromPlayer.sendMessage("[FT] Обмен был успешно выполнен!");
        tradeMenu.stopMenu();
        requests.remove(this);
    }
}