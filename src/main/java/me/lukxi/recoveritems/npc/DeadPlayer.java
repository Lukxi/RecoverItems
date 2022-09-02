package me.lukxi.recoveritems.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeadPlayer {

    private final EntityPlayer npc;
    private final GameProfile gameProfile;
    private final WorldServer world;
    private final String name;


    public DeadPlayer(EntityPlayer npc, GameProfile gameProfile, WorldServer world, String name, Player p){
        this.npc = npc;
        this.gameProfile = gameProfile;
        this.world = world;
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public WorldServer getWorld(){
        return world;
    }
    public GameProfile getGameProfile(){
        return gameProfile;
    }
    public EntityPlayer getEntityplayer(){
        return npc;
    }

}
