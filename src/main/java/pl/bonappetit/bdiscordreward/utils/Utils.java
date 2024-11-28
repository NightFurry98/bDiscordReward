package pl.bonappetit.bdiscordreward.utils;

import java.util.regex.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import java.util.*;
import net.md_5.bungee.api.*;

public class Utils
{
    public static String fixColor(String text) {
        final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");
        for (Matcher matcher = pattern.matcher(text); matcher.find(); matcher = pattern.matcher(text)) {
            final String color = text.substring(matcher.start(), matcher.end());
            text = text.replace("&" + color, ChatColor.of(color) + "");
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> fixColor(final List<String> texts) {
        final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        final List<String> result = new ArrayList<String>();
        for (String text : texts) {
            for (Matcher matcher = pattern.matcher(text); matcher.find(); matcher = pattern.matcher(text)) {
                final String color = text.substring(matcher.start(), matcher.end());
                text = text.replace("&" + color, ChatColor.of(color) + "");
            }
            result.add(ChatColor.translateAlternateColorCodes('&', text));
        }
        return result;
    }

    public static boolean sendMessage(final Player p, final String text) {
        p.sendMessage(fixColor(text));
        return true;
    }

    public static void give(final Player player, final ItemStack itemStack) {
        final HashMap<Integer, ItemStack> playerItemsIneq = player.getInventory().addItem(new ItemStack[] { itemStack });
        if (!playerItemsIneq.isEmpty()) {
            for (final ItemStack leftover : playerItemsIneq.values()) {
                player.getWorld().dropItem(player.getLocation(), leftover);
            }
        }
    }
}
