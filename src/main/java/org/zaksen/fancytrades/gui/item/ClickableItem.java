package org.zaksen.fancytrades.gui.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ClickableItem extends InteractSlot {

    public ClickableItem(int slot, ItemStack stack, Player owner) {
        super(slot, stack, owner, true);
    }

    public void triggerClick(Player player, ClickType clickType) {
        if(canInteract(player)) {
            onClick(player, clickType);
        }
    }

    public void onClick(Player player, ClickType clickType) {}
}