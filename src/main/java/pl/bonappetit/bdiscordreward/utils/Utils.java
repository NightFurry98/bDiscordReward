package pl.bonappetit.bdiscordreward.utils;

import java.util.regex.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import java.util.*;
import net.md_5.bungee.api.*;
import org.bukkit.inventory.meta.ItemMeta;
import pl.bonappetit.bdiscordreward.BDiscordReward;

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

    public static void openRewardGUI(Player player) {
        Configuration config = BDiscordReward.getPlugin().getConfig();
        Inventory gui = Bukkit.createInventory(null, 54, fixColor(config.getString("gui.name")));
        for (String item : config.getStringList("reward.items")) {
            ItemStack itemStack = ParseItemStack.parseItem(item);
            gui.addItem(itemStack);
        }
        player.openInventory(gui);
    }

    public static void openRewardGUICommand(Player player) {
        Configuration config = BDiscordReward.getPlugin().getConfig();
        Inventory gui = Bukkit.createInventory(null, 54, fixColor(config.getString("gui.name")));
        for (String command : config.getStringList("reward.commands")) {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(fixColor("&b") + command);
            itemStack.setItemMeta(itemMeta);
            gui.addItem(itemStack);
        }
        player.openInventory(gui);
    }
}
