package net.trixmc.warpgui.main;

import net.trixmc.warpgui.main.cmd.DelWarpCommand;
import net.trixmc.warpgui.main.cmd.SetWarpCommand;
import net.trixmc.warpgui.main.cmd.WarpCommand;
import net.trixmc.warpgui.main.handler.WarpHandler;
import net.trixmc.warpgui.main.listener.PlayerListener;
import net.trixmc.warpgui.utils.ItemStackBuilder;
import net.trixmc.warpgui.utils.SInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.trixmc.warpgui.utils.ChatUtils.format;

public class Main extends JavaPlugin{
	
	private static Main instance;
	private static Map<UUID, Integer> currency;
	private static Map<UUID, ItemStack> playerHeads;
	private static Map<UUID, Long> cooldown;
	private static Map<UUID, SInventory> sInventory;
	private static Inventory adminsGUI;
	private static ItemStack playerGuiItem;
	private static File dataConfigFile;
	private static FileConfiguration dataConfig;
	
	public void onEnable(){
		instance = this;
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		createDataConfig();
		
		currency = new HashMap<>();
		playerHeads = new HashMap<>();
		cooldown = new HashMap<>();
		sInventory = new HashMap<>();
		adminsGUI = Bukkit.createInventory(null, 27, format("&aServer Warps"));
		playerGuiItem = new ItemStackBuilder(Material.SKULL_ITEM, 3).displayName("&aPlayer Warps").build();
		registerCommands();
		registerListeners();
		WarpHandler.getInstance().setup();


		for (Player p : Bukkit.getOnlinePlayers()) {
			if(getDataConfig() == null || !getDataConfig().contains(p.getUniqueId().toString())) continue;
			currency.put(p.getUniqueId(), getDataConfig().getInt("Warps." + p.getUniqueId().toString()));
		}
	}
	
	public void onDisable(){
		WarpHandler.getInstance().save();
	}
	
	public static Main getInstance(){return instance;}
	
	public static FileConfiguration getDataConfig(){
		return dataConfig;
	}
	
	public void createDataConfig(){
		dataConfigFile = new File(getDataFolder(), "data.yml");

        if (!dataConfigFile.exists()) {
        	dataConfigFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
         }

        dataConfig= new YamlConfiguration();
        try {
        	dataConfig.load(dataConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
	}
	
	public static void saveDataConfig(){
		try {
			dataConfig.save(dataConfigFile);
			dataConfig.load(dataConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}


	public static Map<UUID, Integer> getCurrency() {
		return currency;
	}

	public static Map<UUID, ItemStack> getPlayerHeads() {
		return playerHeads;
	}

	public static Map<UUID, Long> getCooldown() {
		return cooldown;
	}

	public static Map<UUID, SInventory> getSInventory() {
		return sInventory;
	}

	public static Inventory getAdminsGUI() {
		return adminsGUI;
	}

	public static ItemStack getPlayerGuiItem() {
		return playerGuiItem;
	}

	public static File getDataConfigFile() {
		return dataConfigFile;
	}

	private void registerCommands(){
		getCommand("warp").setExecutor(new WarpCommand());
		getCommand("setwarp").setExecutor(new SetWarpCommand());
		getCommand("delwarp").setExecutor(new DelWarpCommand());
	}
	private void registerListeners(){
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}
}
