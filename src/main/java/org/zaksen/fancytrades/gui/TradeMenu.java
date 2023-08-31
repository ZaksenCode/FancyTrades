package org.zaksen.fancytrades.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zaksen.fancytrades.FancyTrades;
import org.zaksen.fancytrades.gui.item.ClickableItem;
import org.zaksen.fancytrades.gui.item.InteractSlot;
import org.zaksen.fancytrades.trade.AcceptStatus;
import org.zaksen.fancytrades.trade.TradeRequest;

import java.util.ArrayList;
import java.util.List;

public class TradeMenu {
    public static List<TradeMenu> menus = new ArrayList<>();
    private final List<InteractSlot> activeSlots = new ArrayList<>();
    public static TradeMenu getByPlayer(Player player) {
        for(TradeMenu menu : menus) {
            if(menu.player1 == player || menu.player2 == player) {
                return menu;
            }
        }
        return null;
    }
    private final Player player1, player2;
    private final Inventory tradeInv;
    private AcceptStatus acceptStatus = AcceptStatus.DONT;
    private final List<Player> accepted = new ArrayList<>();
    private final List<ItemStack> player1Items = new ArrayList<>();
    private final List<ItemStack> player2Items = new ArrayList<>();

    public TradeMenu(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.tradeInv = Bukkit.createInventory(null, 54, "Trade");
        FancyTrades.getInstance().getConfig().getIntegerList("trade-gui.fill_panel").forEach((slot) -> {
            addActSlot(new InteractSlot(slot, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE), null));
        });
        ItemStack btnAcp1 = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta btn1Meta = btnAcp1.getItemMeta();
        btn1Meta.setDisplayName("Принять");
        btnAcp1.setItemMeta(btn1Meta);
        addActSlot(new ClickableItem(
                FancyTrades.getInstance().getConfig().getInt("trade-gui.accept_btn-1"),
                btnAcp1,
                player1) {
                      @Override
                      public void onClick(Player player, ClickType clickType) {
                          accept(player1);
                      }
                  }
        );
        addActSlot(new ClickableItem(
                FancyTrades.getInstance().getConfig().getInt("trade-gui.accept_btn-2"),
                btnAcp1,
                player2) {
                      @Override
                      public void onClick(Player player, ClickType clickType) {
                          accept(player2);
                      }
                  }
        );
        ItemStack profile1 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta profile1Meta = profile1.getItemMeta();
        profile1Meta.setDisplayName(player1.getName());
        profile1.setItemMeta(profile1Meta);
        addActSlot(new InteractSlot(FancyTrades.getInstance().getConfig().getInt("trade-gui.profile_slot-1"), profile1, null));
        ItemStack profile2 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta profile2Meta = profile1.getItemMeta();
        profile2Meta.setDisplayName(player2.getName());
        profile2.setItemMeta(profile2Meta);
        addActSlot(new InteractSlot(FancyTrades.getInstance().getConfig().getInt("trade-gui.profile_slot-2"), profile2, null));
        FancyTrades.getInstance().getConfig().getIntegerList("trade-gui.interact_slot-1").forEach((slot) -> {
            addActSlot(new InteractSlot(slot, new ItemStack(Material.AIR), player1));
        });
        FancyTrades.getInstance().getConfig().getIntegerList("trade-gui.interact_slot-2").forEach((slot) -> {
            addActSlot(new InteractSlot(slot, new ItemStack(Material.AIR), player2));
        });
        menus.add(this);
    }

    public boolean isInventory(Inventory inv) {
        return inv == tradeInv;
    }

    public void stopMenu() {
        player1.closeInventory();
        player2.closeInventory();
        menus.remove(this);
    }

    public void openMenu() {
        player1.openInventory(tradeInv);
        player2.openInventory(tradeInv);
    }

    public void addActSlot(InteractSlot actSlot) {
        activeSlots.add(actSlot);
        tradeInv.setItem(actSlot.getSlot(), actSlot.getStack());
    }

    public InteractSlot getActSlot(int slot) {
        for(InteractSlot actSlot : activeSlots) {
            if(actSlot.getSlot() == slot) {
                return actSlot;
            }
        }
        return null;
    }

    public void breakTrade() {
        activeSlots.forEach((slot) -> {
            slot.setBlocked(true);
        });
        recountItems();
        player1Items.forEach((item) -> {
            player1.getInventory().addItem(item);
        });
        player2Items.forEach((item) -> {
            player2.getInventory().addItem(item);
        });
    }

    public void makeTrade() {
        activeSlots.forEach((slot) -> {
            slot.setBlocked(true);
        });
        recountItems();
        player1Items.forEach((item) -> {
            player2.getInventory().addItem(item);
        });
        player2Items.forEach((item) -> {
            player1.getInventory().addItem(item);
        });
    }

    public void recountItems() {
        player1Items.clear();
        FancyTrades.getInstance().getConfig().getIntegerList("trade-gui.interact_slot-1").forEach((slot) -> {
            ItemStack newItem = tradeInv.getItem(slot);
            if(newItem != null && !newItem.getType().isAir()) {
                player1Items.add(newItem);
            }
        });
        player2Items.clear();
        FancyTrades.getInstance().getConfig().getIntegerList("trade-gui.interact_slot-2").forEach((slot) -> {
            ItemStack newItem = tradeInv.getItem(slot);
            if(newItem != null && !newItem.getType().isAir()) {
                player2Items.add(newItem);
            }
        });
    }

    public void accept(Player player) {
        switch (acceptStatus){
            case DONT: {
                if(accepted.size() < 2 && !accepted.contains(player)) {
                    accepted.add(player);
                } else if (accepted.contains(player)) {
                    accepted.remove(player);
                } else if (accepted.size() == 2) {
                    accepted.clear();
                    acceptStatus = AcceptStatus.FIRST;
                }
            }
            case FIRST: {
                if(accepted.size() < 2 && !accepted.contains(player)) {
                    accepted.add(player);
                } else if (accepted.size() == 2) {
                    accepted.clear();
                    TradeRequest request = TradeRequest.getByMenu(this);
                    if(request != null) {
                        request.end();
                    }
                }
            }
        }
    }
}