package me.lukxi.recoveritems.listener;

import me.lukxi.recoveritems.npc.ClickPacketReader;
import me.lukxi.recoveritems.npc.DeadPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void joinEvent(PlayerJoinEvent e){
        if (DeadPlayerManager.getDeadPlayer().containsKey(e.getPlayer().getUniqueId())) {
            new DeadPlayerManager().sentNPCPacket(e.getPlayer());
            new ClickPacketReader(e.getPlayer()).inject();
        }
    }

}
