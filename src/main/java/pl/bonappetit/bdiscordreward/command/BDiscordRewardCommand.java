package pl.bonappetit.bdiscordreward.command;


import org.bukkit.command.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import pl.bonappetit.bdiscordreward.BDiscordReward;
import pl.bonappetit.bdiscordreward.utils.ParseItemStack;
import pl.bonappetit.bdiscordreward.utils.Utils;

import java.util.*;

public class BDiscordRewardCommand implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        final Player p = (Player) sender;
        Configuration config = BDiscordReward.getPlugin().getConfig();
        if (args.length == 0) {
            Utils.sendMessage(p, config.getString("command.messages.usage_base"));
            return true;
        }
        if (args[0].equalsIgnoreCase("reward")) {
            if (!p.hasPermission(config.getString("command.permission"))) {
                Utils.sendMessage(p, config.getString("command.messages.no_perm"));
                return true;
            }
            if (args.length < 2) {
                Utils.sendMessage(p, config.getString("command.messages.usage_item"));
                return true;
            }
            if (args[1].equalsIgnoreCase("item")) {
                if (args.length < 3) {
                    Utils.sendMessage(p, config.getString("command.messages.usage_item"));
                    return true;
                }
                if (args[2].equalsIgnoreCase("add")) {
                    ItemStack itemInHand = p.getInventory().getItemInMainHand();
                    if (itemInHand == null || itemInHand.getType().isAir()) {
                        Utils.sendMessage(p, config.getString("command.messages.no_item_in_hand"));
                        return true;
                    }
                    String item = ParseItemStack.toString(itemInHand);
                    List<String> configList = config.getStringList("reward.items");
                    configList.add(item);
                    config.set("reward.items", configList);
                    BDiscordReward.getPlugin().saveConfig();
                    BDiscordReward.getPlugin().reloadConfig();
                    Utils.sendMessage(p, config.getString("command.messages.item_added"));
                    return true;
                }
                Utils.sendMessage(p, config.getString("command.messages.usage_item"));
                return true;
            }
            Utils.sendMessage(p, config.getString("command.messages.usage_item"));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.hasPermission(config.getString("command.permission"))) {
                Utils.sendMessage(p, config.getString("command.messages.no_perm"));
                return true;
            }
            BDiscordReward.getPlugin().saveConfig();
            BDiscordReward.getPlugin().reloadConfig();
            Utils.sendMessage(p, config.getString("command.messages.config_reloaded"));
            return true;
        }
        Utils.sendMessage(p, config.getString("command.messages.usage_base"));
        return true;
    }



    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        final Player p = (Player) sender;
        Configuration config = BDiscordReward.getPlugin().getConfig();
        final List<String> suggestions = new ArrayList<>();

        if (!p.hasPermission(config.getString("command.permission"))) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            suggestions.add("reward");
            suggestions.add("reload");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reward")) {
            suggestions.add("item");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("reward") && args[1].equalsIgnoreCase("item")) {
            suggestions.add("add");
        }

        return suggestions;
    }
}
