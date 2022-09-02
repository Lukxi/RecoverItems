package me.lukxi.recoveritems;

import me.lukxi.recoveritems.commands.Test;
import org.bukkit.plugin.java.JavaPlugin;

public class RecoverItems extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("Test").setExecutor(new Test());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
