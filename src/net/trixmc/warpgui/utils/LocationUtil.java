package net.trixmc.warpgui.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil{
	
	public static String toString(Location loc){
		return loc.getWorld().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ() + "|" + loc.getYaw() + "|" + loc.getPitch();
	}
	
	public static Location toLocation(String s){
		String[] array = s.split("\\|");
		return new Location(Bukkit.getWorld(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]), Double.parseDouble(array[3]), Float.parseFloat(array[4]), Float.parseFloat(array[5]));
	}
}