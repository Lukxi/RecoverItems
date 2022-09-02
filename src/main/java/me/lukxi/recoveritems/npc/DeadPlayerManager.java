package me.lukxi.recoveritems.npc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import me.lukxi.recoveritems.RecoverItems;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class DeadPlayerManager {

    private static final HashMap<Player, DeadPlayer> DeadPlayer = new HashMap();

    public static HashMap<Player, me.lukxi.recoveritems.npc.DeadPlayer> getDeadPlayer() {
        return DeadPlayer;
    }

    public void createNPC(Player p, String n) {
        boolean exist = false;

        if (DeadPlayer.containsKey(p)) {
            exist = true;
        }

        if (!exist) {

            DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();

            WorldServer world = ((CraftWorld) Objects.requireNonNull(Bukkit.getWorld(p.getWorld().getName()))).getHandle();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "§4§lDead");

            EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile, null);
            entityPlayer.b(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(),p.getLocation().getPitch());

            DeadPlayer npc = new DeadPlayer(entityPlayer, gameProfile, world, n, p);
            DeadPlayer.put(p,npc);
            sentNPCPacket(p);

        }
    }

    private void sentNPCPacket(Player player) {
        DeadPlayer npc = DeadPlayer.get(player);
        String[] npcSkin = getSkin(player, player.getName());
        npc.getGameProfile().getProperties().clear();
        if (!(npcSkin[0].equalsIgnoreCase("") && npcSkin[1].equalsIgnoreCase(""))) {
            npc.getGameProfile().getProperties().put("textures", new Property("textures", npcSkin[0], npcSkin[1]));
        }
            //npc.getEntityplayer().b(EntityPose.c); //Pose
            //PacketPlayOutEntity.PacketPlayOutRelEntityMove move = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(npc.getEntityplayer().ae(), (byte) 0, (byte) ((player.getLocation().getY() - 1.7 - player.getLocation().getY())*32), (byte) 0, false); // Pose
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc.getEntityplayer()));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc.getEntityplayer()));
            connection.a(new PacketPlayOutEntityHeadRotation(npc.getEntityplayer(), (byte) (npc.getEntityplayer().getBukkitYaw() * 256 /360)));
            //connection.a(move);//Pose
            List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<>();
            ItemStack[] ac = player.getInventory().getArmorContents();
            if (ac[0] != null){
                list.add(new Pair<>(EnumItemSlot.c, CraftItemStack.asNMSCopy(player.getInventory().getBoots())));
            }
            if (ac[1] != null){
                list.add(new Pair<>(EnumItemSlot.d, CraftItemStack.asNMSCopy(player.getInventory().getLeggings())));
            }
            if (ac[2] != null){
                list.add(new Pair<>(EnumItemSlot.e, CraftItemStack.asNMSCopy(player.getInventory().getChestplate())));
            }
            if (player.getInventory().getItemInMainHand() != null || !player.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                list.add(new Pair<>(EnumItemSlot.a, CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand())));
            }
            if (player.getInventory().getItemInOffHand() != null || !player.getInventory().getItemInOffHand().getType().equals(Material.AIR)){
            list.add(new Pair<>(EnumItemSlot.b, CraftItemStack.asNMSCopy(player.getInventory().getItemInOffHand())));
            }

            ItemStack head = new ItemStack(Material.SKELETON_SKULL);

            list.add(new Pair<>(EnumItemSlot.f, CraftItemStack.asNMSCopy(head)));
            connection.a(new PacketPlayOutEntityEquipment(npc.getEntityplayer().getBukkitEntity().getEntityId(), list));
            fixSkin(connection, npc.getEntityplayer());

            removeTablist(connection, npc.getEntityplayer());
            //removeName(player);

    }

    public void sentJoinPacket(Player player) {
        DeadPlayer npc = DeadPlayer.get(player);
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc.getEntityplayer()));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc.getEntityplayer()));
            connection.a(new PacketPlayOutEntityHeadRotation(npc.getEntityplayer(), (byte) (npc.getEntityplayer().getBukkitYaw() * 256 /360)));

            fixSkin(connection, npc.getEntityplayer());

            removeTablist(connection, npc.getEntityplayer());

    }

    private void fixSkin(PlayerConnection connection, EntityPlayer npc){
        Byte secondOverlay = (0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40);

        DataWatcher watcher = npc.ai();
        watcher.b(new DataWatcherObject<Byte>(17, DataWatcherRegistry.a), secondOverlay);
        connection.a(new PacketPlayOutEntityMetadata(npc.ae(), watcher, true));
    }

    public void removeTablist(PlayerConnection connection, EntityPlayer npc){
        RecoverItems mainPlugin = RecoverItems.getPlugin(RecoverItems.class);
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                if (counter == 1){
                    connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, npc));
                    cancel();
                }
                counter++;
            }
        }.runTaskTimer(mainPlugin, 0, 20);
    }

    public void removeName(Player p){

        org.bukkit.scoreboard.Scoreboard scoreboard = p.getScoreboard();

        Team npcs = null;

        for(Team team : scoreboard.getTeams()) {
            if(team.getName().equals("npcs")) {
                npcs = team;
                break;
            }
        }

        if(npcs == null) {
            npcs = scoreboard.registerNewTeam("npcs");
        }

        npcs.setNameTagVisibility(NameTagVisibility.ALWAYS);
        npcs.addEntry(DeadPlayer.get(p).getName());

    }


    public void removeNPC(Player p) {
        EntityPlayer entityPlayer = DeadPlayer.get(p).getEntityplayer();;
        PlayerConnection connection = ((CraftPlayer) p).getHandle().b;
        connection.a(new PacketPlayOutEntityDestroy(entityPlayer.getBukkitEntity().getEntityId()));
        DeadPlayer.remove(p);
    }

    private String[] getSkin(Player p, String skin) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + skin);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = JsonParser.parseReader(reader).getAsJsonObject().get("id").getAsString();

            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = JsonParser.parseReader(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();
            return new String[]{texture, signature};
        } catch (Exception e) {
            p.sendMessage("Der Skin vom angegebenen Spieler konnte nicht geladen werden!");
            return new String[]{"", ""};
        }
    }

}
