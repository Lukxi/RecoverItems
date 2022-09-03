package me.lukxi.recoveritems.listener;

import me.lukxi.recoveritems.npc.ClickPacketReader;
import me.lukxi.recoveritems.npc.DeadPlayer;
import me.lukxi.recoveritems.npc.DeadPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeath implements Listener {

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e){

        Player p = e.getEntity();
        if (DeadPlayerManager.getDeadPlayer().containsKey(p.getUniqueId())){
            new DeadPlayerManager().removeNPC(p);
            DeadPlayerManager.getDeadPlayer().remove(p.getUniqueId());
            ItemStack[] inv = p.getInventory().getContents();
            ItemStack[] armor = p.getInventory().getArmorContents();
            new DeadPlayerManager().createNPC(p, p.getName(), inv, armor);
        }else{
            ItemStack[] inv = p.getInventory().getContents();
            ItemStack[] armor = p.getInventory().getArmorContents();
            new DeadPlayerManager().createNPC(p, p.getName(), inv, armor);
        }
        e.getDrops().clear();

    }
}

