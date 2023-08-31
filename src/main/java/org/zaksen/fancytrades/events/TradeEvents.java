package org.zaksen.fancytrades.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.zaksen.fancytrades.gui.TradeMenu;
import org.zaksen.fancytrades.gui.item.ClickableItem;
import org.zaksen.fancytrades.gui.item.InteractSlot;
import org.zaksen.fancytrades.trade.RequestStatus;
import org.zaksen.fancytrades.trade.TradeRequest;

public class TradeEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        TradeMenu menu = TradeMenu.getByPlayer(player);
        if(menu != null) {
            Inventory inv = event.getClickedInventory();
            if(menu.isInventory(inv)) {
                InteractSlot slot = menu.getActSlot(event.getSlot());
                if (slot != null) {
                    if (slot.canInteract(player)) {
                        if (slot.isButton()) {
                            ClickableItem button = (ClickableItem) slot;
                            button.triggerClick(player, event.getClick());
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        TradeMenu menu = TradeMenu.getByPlayer(player);
        if(menu != null) {
            TradeRequest request = TradeRequest.getByMenu(menu);
            if(request != null && request.getStatus() == RequestStatus.ACCEPTED) {
                request.stop();
            }
        }
    }
}