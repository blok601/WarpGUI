package net.trixmc.warpgui.utils;

import org.bukkit.ChatColor;

/**
 * Created by Blok on 7/26/2018.
 */
public class ChatUtils {

    public static String format(String input){
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
