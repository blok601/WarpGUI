package net.trixmc.warpgui.main.cmd;

import net.trixmc.warpgui.main.Main;
import net.trixmc.warpgui.main.Warp;
import net.trixmc.warpgui.utils.WarpUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.trixmc.warpgui.utils.ChatUtils.format;

/**
 * Created by Blok on 7/20/2018.
 */
public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0){
            if (sender instanceof Player){
                Player p = (Player) sender;
                p.openInventory(Main.getAdminsGUI());
            }
        }else {
            if (args[0].equalsIgnoreCase("give")){
                if (sender instanceof Player && sender.hasPermission("warpgui.admin") || sender instanceof ConsoleCommandSender){
                    if (args.length >= 2){
                        Player p = Bukkit.getPlayer(args[1]);
                        if (p == null){
                            sender.sendMessage(format("&cPlayer not found."));
                            return false;
                        }
                        WarpUtil.addWarp(p);
                        sender.sendMessage(format("&aSuccessfully added warp currency to " + p.getName()));
                        return true;
                    }
                    sender.sendMessage(format("&aUsage: /warp give <player>"));
                    return false;
                }
            }else if (args[0].equalsIgnoreCase("currency")){
                if (sender instanceof Player){
                    Player p = (Player) sender;
                    p.sendMessage(format("&aYou have " + WarpUtil.getWarps(p) + " warps left!"));
                    return true;
                }
            }else{
                if (sender instanceof Player){
                    Player p = (Player) sender;
                    Warp warp = Warp.getByName(args[0]);
                    if (warp == null){
                        p.sendMessage(format("&cWarp not found."));
                        return false;
                    }
                    WarpUtil.warp(p, warp.getName());
                    return true;
                }
            }
        }
        return false;
    }
}
