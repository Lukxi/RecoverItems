package me.lukxi.recoveritems.commands;


import me.lukxi.recoveritems.npc.DeadPlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (DeadPlayerManager.getDeadPlayer().containsKey(p.getUniqueId())){
            new DeadPlayerManager().removeNPC(p);
            new DeadPlayerManager().createNPC(p, p.getName(), p.getInventory().getContents(), p.getInventory().getArmorContents());
        }
        new DeadPlayerManager().createNPC(p, p.getName(), p.getInventory().getContents(), p.getInventory().getArmorContents());

        return false;
    }


}
