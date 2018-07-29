package net.trixmc.warpgui.main.cmd;

import net.trixmc.warpgui.main.Main;
import net.trixmc.warpgui.main.Warp;
import net.trixmc.warpgui.main.WarpType;
import net.trixmc.warpgui.utils.LocationUtil;
import net.trixmc.warpgui.utils.WarpUtil;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.trixmc.warpgui.utils.ChatUtils.format;

/**
 * Created by Blok on 7/20/2018.
 */
public class SetWarpCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("warpgui.admin")) {
                if (args.length >= 3) {
                    if (!EnumUtils.isValidEnum(WarpType.class, args[0].toUpperCase())) {
                        p.sendMessage(format("&cInvalid warp type."));
                        return false;
                    }
                    if (!WarpUtil.isMaterial(args[1].toUpperCase())) {
                        p.sendMessage(format("&cInvalid material"));
                        return false;
                    }
                    String name = args[2];
                    WarpType type = WarpType.valueOf(args[0].toUpperCase());
                    Warp warp = Warp.getByName(name);
                    if (warp != null) {
                        p.sendMessage(format("&cThis name is already taken!"));
                        return false;
                    }
                    Location loc = p.getLocation();
                    Material material = Material.valueOf(args[1].toUpperCase());
                    warp = new Warp(name, p.getName(), type, loc);
                    warp.setMaterial(material);

                    Main.getDataConfig().set("Warp." + name + ".Owner", p.getName());
                    Main.getDataConfig().set("Warp." + name + ".Type", type.toString());
                    /*Main.getDataConfig().set("Warp." + name + ".Location", LocationUtil.toString(loc));*/
                    Main.getDataConfig().set("Warp." + name + ".Location.world", p.getWorld().getName());
                    Main.getDataConfig().set("Warp." + name + ".Location.x", p.getLocation().getX());
                    Main.getDataConfig().set("Warp." + name + ".Location.y", p.getLocation().getY());
                    Main.getDataConfig().set("Warp." + name + ".Location.z", p.getLocation().getZ());
                    Main.getDataConfig().set("Warp." + name + ".Material", material.toString());
                    Main.saveDataConfig();

                    if (warp.getType() == WarpType.ADMIN)
                        WarpUtil.updateAdminsGui();
                    p.sendMessage(format("&aCreated warp '" + name + "'"));
                    return true;
                }
                p.sendMessage(format("&aUsage: /setwarp player/admin <material> <nameofwarp>"));
                return false;
            }
            if (args.length >= 3) {
                if (WarpUtil.getWarps(p) == 0) {
                    p.sendMessage(format("&cYou don't have enough warps"));
                    return false;
                }
                String name = args[2];
                Warp warp = Warp.getByName(name);
                if (warp != null) {
                    p.sendMessage(format("&cThis name is already taken!"));
                    return false;
                }
                if (!WarpUtil.isMaterial(args[0].toUpperCase()) && !args[0].equalsIgnoreCase("playerhead")) {
                    p.sendMessage(format("&cInvalid type!"));
                    return false;
                }
                WarpUtil.removeWarp(p);
                Location loc = p.getLocation();
                warp = new Warp(name, p.getName(), WarpType.PLAYER, loc);
                warp.setCategory(args[1]);

                Main.getDataConfig().set("Warp." + name + ".Owner", p.getName());
                Main.getDataConfig().set("Warp." + name + ".Category", args[1]);
                Main.getDataConfig().set("Warp." + name + ".Type", warp.getType().toString());
                Main.getDataConfig().set("Warp." + name + ".Location", LocationUtil.toString(loc));

                if (WarpUtil.isMaterial(args[0].toUpperCase())) {
                    Material material = Material.valueOf(args[0].toUpperCase());
                    warp.setMaterial(material);
                    Main.getDataConfig().set("Warp." + name + ".Material", material.toString());
                } else {
                    warp.setHead(p.getName());
                    Main.getDataConfig().set("Warp." + name + ".Head", p.getName());
                }

                Main.saveDataConfig();

                p.sendMessage(format("&aCreated warp '" + name + "'"));
                return true;
            }
            p.sendMessage(format("&a/setwarp <material/playerhead> <category> <name of warp>"));
            return false;
        }

        return false;
    }
}
