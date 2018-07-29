package net.trixmc.warpgui.main.listener;

import net.trixmc.warpgui.main.Main;
import net.trixmc.warpgui.utils.SInventory;
import net.trixmc.warpgui.utils.WarpUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static net.trixmc.warpgui.utils.ChatUtils.format;

/**
 * Created by Blok on 7/20/2018.
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        int warps = Main.getCurrency().getOrDefault(p.getUniqueId(), 0);
        Main.getDataConfig().set("Warps." + p.getUniqueId().toString(), warps);
        Main.saveDataConfig();
        Main.getCurrency().remove(p.getUniqueId());
        Main.getCooldown().remove(p.getUniqueId());
        Main.getSInventory().remove(p.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getWhoClicked() instanceof Player){
            Player p = (Player) e.getWhoClicked();
            if (e.getClickedInventory() != null && e.getClickedInventory().getTitle() != null){
                if (e.getClickedInventory().equals(Main.getAdminsGUI())){
                    e.setCancelled(true);
                    if (e.getCurrentItem().getType() != Material.AIR){
                        ItemStack item = e.getCurrentItem();
                        p.closeInventory();
                        if (item.equals(Main.getPlayerGuiItem())){
                            WarpUtil.openWarpsMenu(p);
                        }else{
                            WarpUtil.warp(p, item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()));
                        }
                    }
                }else if (Main.getSInventory().containsKey(p.getUniqueId()) && e.getClickedInventory().getTitle().startsWith(ChatColor.GREEN + "Player Warps")){
                    SInventory inv = Main.getSInventory().get(p.getUniqueId());
                    e.setCancelled(true);
                    if (e.getCurrentItem().getType() != Material.AIR){
                        ItemStack item = e.getCurrentItem();
                        e.setCancelled(true);
                        if (item.equals(inv.getPreviousPageItem()))
                            p.openInventory(inv.getPage(inv.getPage(p) - 1));
                        else if (item.equals(inv.getNextPageItem()))
                            p.openInventory(inv.getPage(inv.getPage(p) + 1));
                        else
                            WarpUtil.warp(p, item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        int warps = Main.getDataConfig().getInt("Warps." + p.getUniqueId().toString());
        Main.getCurrency().put(p.getUniqueId(), warps);
        //This updates the caches heads when player joins
        if (Main.getPlayerHeads().containsKey(p.getUniqueId())){
            ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(p.getName());
            is.setItemMeta(im);
            Main.getPlayerHeads().put(p.getUniqueId(), is);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ()) {
            return;
        }

        if(WarpUtil.getMovement().contains(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(format("&cYou moved so your warp was cancelled!"));
            WarpUtil.getMovement().remove(e.getPlayer().getUniqueId());
        }
    }

}
