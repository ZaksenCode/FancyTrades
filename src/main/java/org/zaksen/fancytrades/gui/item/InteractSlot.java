package org.zaksen.fancytrades.gui.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InteractSlot {

    protected final Player canInteract;
    protected final int slot;
    protected final ItemStack stack;
    protected boolean blocked = false;
    protected final boolean isBtn;

    public InteractSlot(int slot, ItemStack stack, Player canInteract, boolean isBtn) {
        this.canInteract = canInteract;
        this.slot = slot;
        this.stack = stack;
        this.isBtn = isBtn;
    }

    public InteractSlot(int slot, ItemStack stack, Player canInteract) {
        this.canInteract = canInteract;
        this.slot = slot;
        this.stack = stack;
        this.isBtn = false;
    }

    public boolean isButton() {
        return isBtn;
    }

    public boolean canInteract(Player player) {
        return (player == canInteract) && (!isBlocked());
    }

    public int getSlot() {
        return slot;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public ItemStack getStack() {
        return stack;
    }
}