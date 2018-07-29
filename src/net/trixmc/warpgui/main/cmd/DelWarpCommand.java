package net.trixmc.warpgui.main.cmd;

import net.trixmc.warpgui.main.Main;
import net.trixmc.warpgui.main.Warp;
import net.trixmc.warpgui.main.WarpType;
import net.trixmc.warpgui.utils.WarpUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.trixmc.warpgui.utils.ChatUtils.format;

/**
 * Created by Blok on 7/20/2018.
 */
public class DelWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player && sender.hasPermission("warpgui.admin") || sender instanceof ConsoleCommandSender) {
            if (args.length >= 1) {
                Warp warp = Warp.getByName(args[0]);
                if (warp == null) {
                    sender.sendMessage(format("&cWarp not found."));
                    return false;
                }
                if(warp.getType() != WarpType.PLAYER){
                    WarpUtil.updateAdminsGui();
                }

                Main.getDataConfig().set("Warp." + warp.getName(), null);
                Main.saveDataConfig();
                warp.delete();
                sender.sendMessage(format("&aWarp deleted!"));
                return true;
            }
            sender.sendMessage(format("&Usage: /delwarp <nameofwarp>"));
            return false;
        }
        return false;
    }
}
