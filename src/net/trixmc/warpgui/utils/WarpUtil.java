package net.trixmc.warpgui.utils;

import net.trixmc.warpgui.main.Main;
import net.trixmc.warpgui.main.Warp;
import net.trixmc.warpgui.main.WarpType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

import static net.trixmc.warpgui.utils.ChatUtils.format;

/**
 * Created by Blok on 7/20/2018.
 */
public class WarpUtil {

    private static HashSet<UUID> movement = new HashSet<>();

    public static HashSet<UUID> getMovement() {
        return movement;
    }

    public static boolean isMaterial(String s) {
        try {
            Material.valueOf(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static int getWarps(Player p) {
        return Main.getCurrency().getOrDefault(p.getUniqueId(), 0);
    }

    public static void addWarp(Player p) {
        Main.getCurrency().put(p.getUniqueId(), Main.getCurrency().get(p.getUniqueId()) + 1);
    }

    public static void removeWarp(Player p) {
        Main.getCurrency().put(p.getUniqueId(), Main.getCurrency().get(p.getUniqueId()) - 1);
    }

    public static ItemStack getPlayerHead(Player p) {
        if (Main.getPlayerHeads().containsKey(p.getUniqueId()))
            return Main.getPlayerHeads().get(p.getUniqueId());
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.setOwner(p.getName());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getPlayerHead(String name) {
        Player p = Bukkit.getPlayer(name);
        if (p != null)
            return getPlayerHead(p);
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.setOwner(name);
        is.setItemMeta(im);
        return is;
    }

    public static void updateAdminsGui() {
        Main.getAdminsGUI().clear();
        for (Warp warp : Warp.getAll()) {
            if (warp.getType() == WarpType.ADMIN)
                Main.getAdminsGUI().addItem(warp.getIcon());
        }
        Main.getAdminsGUI().setItem(26, Main.getPlayerGuiItem());
    }

    public static void openWarpsMenu(Player p) {
        SInventory inv = new SInventory(format("&aPlayer Warps"));
        for (Warp warp : Warp.getAll()) {
            if (warp.getType() == WarpType.PLAYER)
                inv.addItem(warp.getIcon());
        }
        Main.getSInventory().put(p.getUniqueId(), inv);
        p.openInventory(inv.getPage(1));
    }

    public static void warp(Player p, String s) {
        if (Main.getCooldown().containsKey(p.getUniqueId()) && (Main.getCooldown().get(p.getUniqueId()) - System.currentTimeMillis()) > 0) {
            p.sendMessage(format("&aPlease wait before teleporting again!"));
            return;
        }
        Warp warp = Warp.getByName(s);
        if (warp == null) {
            p.sendMessage(format("&cWarp not found!"));
            return;
        }
        Main.getCooldown().put(p.getUniqueId(), System.currentTimeMillis() + 3000);

        p.sendMessage(format("&aWarping to " + warp.getName() + " in 5 seconds...don't move."));
        getMovement().add(p.getUniqueId());
        new BukkitRunnable() {
            int counter = 5;

            @Override
            public void run() {
                if (counter == 0) {
                    p.teleport(warp.getLocation());
                    p.sendMessage(format("&aWarped to " + warp.getName()));
                    getMovement().remove(p.getUniqueId());
                    cancel();
                    return;
                }

                if (!WarpUtil.getMovement().contains(p.getUniqueId())) {
                    cancel();
                    return;
                }

                counter--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);

    }

}
