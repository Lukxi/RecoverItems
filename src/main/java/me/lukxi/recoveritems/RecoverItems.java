package me.lukxi.recoveritems;

import me.lukxi.recoveritems.commands.Test;
import me.lukxi.recoveritems.listener.PlayerDeath;
import me.lukxi.recoveritems.listener.PlayerJoin;
import me.lukxi.recoveritems.listener.PlayerQuit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RecoverItems extends JavaPlugin {

    private static RecoverItems instance;

    @Override
    public void onLoad() {
        instance = this;
    }



    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("Test").setExecutor(new Test());
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static RecoverItems getInstance() {
        return instance;
    }
}
