package me.lukxi.recoveritems.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;


public class DeadPlayer {

    private final EntityPlayer npc;
    private final GameProfile gameProfile;
    private final WorldServer world;
    private final String name;
    private final ItemStack[] inv;
    private final ItemStack[] armor;
    private final ItemStack mainHand;
    private final ItemStack offHand;


    public DeadPlayer(EntityPlayer npc, GameProfile gameProfile, WorldServer world, String name, ItemStack[] inv, ItemStack[] armor,
                      ItemStack mainHand, ItemStack offHand){
        this.npc = npc;
        this.gameProfile = gameProfile;
        this.world = world;
        this.name = name;
        this.inv = inv;
        this.armor = armor;
        this.mainHand = mainHand;
        this.offHand = offHand;
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
    public ItemStack[] getInv() {
        return inv;
    }
    public ItemStack[] getArmor() {
        return armor;
    }
    public ItemStack getMainHand() {
        return mainHand;
    }
    public ItemStack getOffHand() {
        return offHand;
    }
}
