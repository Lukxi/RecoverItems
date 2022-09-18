package me.lukxi.recoveritems.Menu;

import me.lukxi.recoveritems.npc.DeadPlayer;
import me.lukxi.recoveritems.npc.DeadPlayerManager;
import me.oxolotel.utils.bukkit.menuManager.menus.*;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RecoverMenu extends CustomMenu implements Closeable, Modifyable, SlotCondition, ShiftClickable {

    private DeadPlayer dp;
    private InventoryContent c;

    public RecoverMenu(int size, Player p, InventoryContent c) {
        super(size);
        this.c = c;
        this.dp = DeadPlayerManager.getDeadPlayer().get(p.getUniqueId());
    }

    @Override
    public InventoryContent getContents(Player player) {
        return c;
    }

    @Override
    public void onClose(Player player, ItemStack[] itemStacks, CloseReason closeReason) {
        int counter = 0;
        if (!checkInvEmpty(itemStacks)) {
            DeadPlayerManager.getDeadPlayer().get(player.getUniqueId()).setInv(itemStacks);
        }else {
            new DeadPlayerManager().removeNPC(player);
        }


    }

    private void initContent(){
        int counter = 0;
        for (ItemStack i:dp.getInv()) {
            c.addGuiItem(counter, new InventoryItem(i, null));
            counter++;
        }
    }
    private boolean checkInvEmpty(ItemStack[] itemStacks){
        for(int i = 0; i<size; i++){
            if (itemStacks[i] != null && !itemStacks[i].getType().equals(Material.AIR)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isClickAllowed(Player player, int i) {
        return true;
    }
}
