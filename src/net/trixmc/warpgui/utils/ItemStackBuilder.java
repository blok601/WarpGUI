package net.trixmc.warpgui.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackBuilder
{

    private ItemStack itemStack;
    private ItemMeta itemMeta;
    
    public ItemStackBuilder(ItemStack itemstack)
    {
        itemStack = itemstack;
        itemMeta = itemstack.getItemMeta();
    }
    
    public ItemStackBuilder(Material material)
    {
        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
    }
    
    public ItemStackBuilder(Material material, int data)
    {
        itemStack = new ItemStack(material, 1, (byte)data);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemStackBuilder amount(int amount)
    {
        itemStack.setAmount(amount);
        return this;
    }
    
    public ItemStackBuilder durability(short amount)
    {
        itemStack.setDurability(amount);
        return this;
    }

    public ItemStackBuilder displayName(String displayName)
    {
        itemMeta.setDisplayName(ChatUtils.format(displayName));
        return this;
    }

    public ItemStackBuilder lore(String[] lore)
    {
        List<String> loreArray = new ArrayList<String>();

        for (String loreBit : lore)
        {
            loreArray.add(ChatColor.GRAY + ChatUtils.format(loreBit));
        }

        itemMeta.setLore(loreArray);
        return this;
    }
    
    public ItemStackBuilder lore(List<String> lore)
    {
        itemMeta.setLore(lore);
        return this;
    }

    public ItemStackBuilder enchant(Enchantment enchanement, int level, boolean ignoreLevelRestriction)
    {
    	if (enchanement != null)
    		itemMeta.addEnchant(enchanement, level, ignoreLevelRestriction);
        return this;
    }

    public ItemStackBuilder unbreakable(boolean unbreakable)
    {
        itemMeta.spigot().setUnbreakable(unbreakable);
        return this;
    }
    
    public ItemStack build()
    {
        ItemStack clonedStack = itemStack.clone();
        clonedStack.setItemMeta(itemMeta.clone());
        return clonedStack;
    }
    
}
