package me.lukxi.recoveritems.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.lukxi.recoveritems.RecoverItems;
import me.lukxi.recoveritems.npc.DeadPlayer;
import me.lukxi.recoveritems.npc.DeadPlayerManager;
import me.lukxi.recoveritems.Menu.RecoverMenu;
import me.oxolotel.utils.bukkit.menuManager.InventoryMenuManager;
import org.bukkit.scheduler.BukkitRunnable;


public class ProtocolLibReader {

    public void readNPCClickPacket(ProtocolManager pm, RecoverItems main) {
        if (pm != null) {
            pm.addPacketListener(new PacketAdapter(main, PacketType.Play.Client.USE_ENTITY) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    PacketContainer packet = event.getPacket();
                        if (DeadPlayerManager.getDeadPlayer().containsKey(event.getPlayer().getUniqueId())) {
                            DeadPlayer npc = DeadPlayerManager.getDeadPlayer().get(event.getPlayer().getUniqueId());
                            if (packet.getIntegers().read(0) == npc.getEntityplayer().ae()) {
                                try {
                                    EnumWrappers.Hand hand = packet.getEnumEntityUseActions().read(0).getHand();
                                    EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();
                                    if (hand == EnumWrappers.Hand.MAIN_HAND && action == EnumWrappers.EntityUseAction.INTERACT) {
                                        new BukkitRunnable() {
                                            int counter = 0;
                                            @Override
                                            public void run() {
                                                if (counter == 1) {
                                                    InventoryMenuManager.getInstance().openMenu(event.getPlayer(), new RecoverMenu(54, event.getPlayer(), npc.initContent()));
                                                    cancel();
                                                }
                                                counter++;
                                            }
                                        }.runTaskTimer(main, 0, 1);
                                    }
                                } catch (IllegalArgumentException exception) {
                                    event.getPlayer().sendMessage("Left Click");
                                }
                            }
                        }
                }
            });
        }
    }




}