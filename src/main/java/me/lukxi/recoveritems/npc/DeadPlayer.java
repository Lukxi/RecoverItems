package me.lukxi.recoveritems.npc;

import com.mojang.authlib.GameProfile;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryContent;
import me.oxolotel.utils.bukkit.menuManager.menus.content.InventoryItem;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class DeadPlayer {

    private final EntityPlayer npc;
    private final GameProfile gameProfile;
    private final WorldServer world;
    private final String name;
    private  ItemStack[] inv;
    private  ItemStack[] armor;
    private final ItemStack mainHand;
    private final ItemStack offHand;
    private final Location location;
    private InventoryContent content;
    private Player p;


    public DeadPlayer(EntityPlayer npc, GameProfile gameProfile, WorldServer world, String name, ItemStack[] inv, ItemStack[] armor,
                      ItemStack mainHand, ItemStack offHand, Location location, Player p){
        this.npc = npc;
        this.gameProfile = gameProfile;
        this.world = world;
        this.name = name;
        this.inv = inv;
        this.armor = armor;
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.location = location;
        this.p = p;
        this.content = new InventoryContent();
        initContent();

    }

    public InventoryContent initContent(){
        int counter = 0;
        content.clear();
        for (ItemStack i:inv) {
            content.addGuiItem(counter, new InventoryItem(i, null));
            counter++;
        }
        return content;
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
    public Location getLocation() {
        return location;
    }
    public void setInv(ItemStack[] inv) {
        this.inv = inv;
    }
    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }

    public InventoryContent getContent() {
        return content;
    }

    public void setContent(InventoryContent content) {
        this.content = content;
    }
}
