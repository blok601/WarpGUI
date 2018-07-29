package net.trixmc.warpgui.main;

import net.trixmc.warpgui.utils.ItemStackBuilder;
import net.trixmc.warpgui.utils.WarpUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Warp {
	
	Main main = Main.getInstance();
	private static List<Warp> warps = new ArrayList<Warp>();
	String name;
	String owner;
	String category;
	WarpType type;
	Location location;
	Material material;
	String head;
	ItemStack icon;
	
	public Warp(String name, String owner, WarpType type, Location location){
		this.name = type == WarpType.ADMIN ? ChatColor.translateAlternateColorCodes('&', name) : name;
		this.owner = owner;
		this.type = type;
		this.location = location;
		this.category = "Unknown";
		warps.add(this);
	}
	
	public String getName(){return name;}
	public String getOwner(){return owner;}
	public WarpType getType(){return type;}
	public Location getLocation(){return location;}
	public Material getMaterial(){return material;}
	public String getHead(){return head;}
	
	public static Warp getByName(String name){for (Warp w : warps){if (w.getName().equalsIgnoreCase(name))return w;}return null;}
	public static List<Warp> getAll(){return warps;}
	
	public void setMaterial(Material material){this.material = material;createIcon();}
	public void setHead(String head){this.head = head;createIcon();}
	public void setCategory(String category){this.category = category;}

	public String getCategory() {
		return category;
	}

	public ItemStack getIcon(){
		return icon;
	}
	
	private void createIcon(){
		ItemStackBuilder builder;
		if (material != null)
			builder = new ItemStackBuilder(material);
		else
			builder = new ItemStackBuilder(WarpUtil.getPlayerHead(head));
		builder.displayName("&a" + name);
		if (type == WarpType.PLAYER)
			builder.lore(new String[]{"&7Category: &e" + category, "&7Owner: &e" + owner});
		icon = builder.build();
	}
	
	public void delete(){
		warps.remove(this);
		name = null;
		type = null;
		location = null;
	}
	
}
