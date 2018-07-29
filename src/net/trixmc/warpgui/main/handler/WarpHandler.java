package net.trixmc.warpgui.main.handler;

import net.trixmc.warpgui.main.Main;
import net.trixmc.warpgui.main.Warp;
import net.trixmc.warpgui.main.WarpType;
import net.trixmc.warpgui.utils.WarpUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Blok on 7/26/2018.
 */
public class WarpHandler {
    private static WarpHandler ourInstance = new WarpHandler();

    public static WarpHandler getInstance() {
        return ourInstance;
    }

    private WarpHandler() {
    }

    private ArrayList<Warp> warps;

    public void setup() {
        this.warps = new ArrayList<>();
        if (Main.getDataConfig().getConfigurationSection("Warp") != null) {
            Warp warp;
            ConfigurationSection section = Main.getDataConfig().getConfigurationSection("Warp");
            for (String name : section.getKeys(false)) {
                String owner = section.getString(name + ".Owner");
                String category = section.getString(name + ".Category");
                String typeStr = section.getString(name + ".Type");
                String materialStr = section.getString(name + ".Material");
                String headStr = section.getString(name + ".Head");

                double x,y,z;
                x = section.getDouble(name + ".Location.x");
                y = section.getDouble(name + ".Location.y");
                z = section.getDouble(name + ".Location.z");
                String world = section.getString(name + ".Location.world");
                if(world == null) continue;
                if(Bukkit.getWorld(world) == null){
                    Bukkit.getLogger().log(Level.SEVERE, "There was a problem trying to load the world: " + world + " for WarpsGUI! Make sure it is loaded and working!");
                    continue;
                }

                warp = new Warp(name, owner, WarpType.valueOf(typeStr), new Location(Bukkit.getWorld(world), x, y, z));
                if (materialStr != null)
                    warp.setMaterial(Material.valueOf(materialStr));
                else
                    warp.setHead(headStr);
                if (category != null)
                    warp.setCategory(category);
            }
        }

        WarpUtil.updateAdminsGui();
    }

    public void save() {
        FileConfiguration config = Main.getDataConfig();

        for (UUID uuid : Main.getCurrency().keySet()) {
            config.set("Warps." + uuid.toString(), Main.getCurrency().get(uuid));
        }

        for (Warp warp : warps) {
            config.set("Warp." + warp.getName() + ".Owner", warp.getOwner());
            if (warp.getCategory() != null) {
                config.set("Warp." + warp.getName() + ".Category", warp.getCategory());
            }
            config.set("Warp." + warp.getName() + ".Type", warp.getType());
            config.set("Warp." + warp.getName() + ".Material", warp.getMaterial());
            config.set("Warp." + warp.getName() + ".Head", warp.getHead());
            config.set("Warp." + warp.getName() + ".Location.world", warp.getLocation().getWorld());
            config.set("Warp." + warp.getName() + ".Location.x", warp.getLocation().getX());
            config.set("Warp." + warp.getName() + ".Location.y", warp.getLocation().getY());
            config.set("Warp." + warp.getName() + ".Location.z", warp.getLocation().getZ());
        }

        Main.saveDataConfig();

    }

}
