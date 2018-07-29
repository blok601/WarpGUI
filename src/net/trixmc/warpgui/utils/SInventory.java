package net.trixmc.warpgui.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.trixmc.warpgui.utils.ChatUtils.format;

public class SInventory {
	
	String title;
	List<Inventory> pages;
	ItemStack previousPageItem;
	ItemStack nextPageItem;
	
	public SInventory(String title){
		this.title = title;
		pages = new ArrayList<Inventory>();
		previousPageItem = new ItemStackBuilder(Material.ARROW).displayName(format("&aPrevious page")).build();
		nextPageItem = new ItemStackBuilder(Material.ARROW).displayName(format("&aNext page")).build();
		createNewPage();
	}
	
	public ItemStack getPreviousPageItem(){return previousPageItem;}
	public ItemStack getNextPageItem(){return nextPageItem;}
	
	public void addItem(ItemStack item){
		boolean added = false;
		for (Inventory page : pages){
			if (page.firstEmpty() != -1){
				page.addItem(item);
				added = true;
				break;
			}
		}
		if (!added){
			createNewPage();
			pages.get(pages.size() - 1).addItem(item);
		}
	}
	
	private void createNewPage(){
		Inventory page = Bukkit.createInventory(null, 54, title + " Page " + (totalPages() + 1));
		page.setItem(45, previousPageItem);
		page.setItem(53, nextPageItem);
		pages.add(page);
	}
	
	public Inventory getPage(int i){
		if (i > pages.size())
			i = pages.size();
		else if (i < 1)
			i = 1;
		return pages.get(i - 1);
	}
	
	public int getPage(Player p){
		int i = 0;
		for (Inventory page : pages){
			i++;
			if (page.getViewers().contains(p))
				return i;
		}
		return i;
	}
	
	public int totalPages(){return pages.size();}
	
}
