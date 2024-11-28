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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        Configuration config = BDiscordReward.getPlugin().getConfig();

        if (args.length == 0) {
            Utils.sendMessage(player, config.getString("command.messages.usage_base"));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reward":
                handleRewardCommand(player, config, args);
                break;
            case "reload":
                handleReloadCommand(player, config);
                break;
            default:
                Utils.sendMessage(player, config.getString("command.messages.usage_base"));
                break;
        }
        return true;
    }

    private void handleRewardCommand(Player player, Configuration config, String[] args) {
        if (!player.hasPermission(config.getString("command.permission"))) {
            Utils.sendMessage(player, config.getString("command.messages.no_perm"));
            return;
        }

        if (args.length < 2) {
            Utils.sendMessage(player, config.getString("command.messages.usage_item"));
            return;
        }

        String rewardType = args[1].toLowerCase();
        switch (rewardType) {
            case "item":
                handleItemSubCommand(player, config, args);
                break;
            case "command":
                handleCommandSubCommand(player, config, args);
                break;
            default:
                Utils.sendMessage(player, config.getString("command.messages.usage_item"));
                break;
        }
    }

    private void handleItemSubCommand(Player player, Configuration config, String[] args) {
        if (args.length < 3) {
            Utils.sendMessage(player, config.getString("command.messages.usage_item"));
            return;
        }

        String itemAction = args[2].toLowerCase();
        switch (itemAction) {
            case "add":
                addItemToConfig(player, config);
                break;
            case "remove":
                Utils.openRewardGUI(player);
                break;
            default:
                Utils.sendMessage(player, config.getString("command.messages.usage_item"));
                break;
        }
    }

    private void addItemToConfig(Player player, Configuration config) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType().isAir()) {
            Utils.sendMessage(player, config.getString("command.messages.no_item_in_hand"));
            return;
        }

        String item = ParseItemStack.toString(itemInHand);
        List<String> items = config.getStringList("reward.items");
        items.add(item);
        config.set("reward.items", items);
        saveAndReloadConfig(config);
        Utils.sendMessage(player, config.getString("command.messages.item_added"));
    }

    private void handleCommandSubCommand(Player player, Configuration config, String[] args) {
        if (args.length < 3) {
            Utils.sendMessage(player, config.getString("command.messages.usage_command"));
            return;
        }

        String commandAction = args[2].toLowerCase();
        switch (commandAction) {
            case "add":
                addCommandToConfig(player, config, args);
                break;
            case "remove":
                Utils.openRewardGUICommand(player);
                break;
            default:
                Utils.sendMessage(player, config.getString("command.messages.usage_command"));
                break;
        }
    }

    private void addCommandToConfig(Player player, Configuration config, String[] args) {
        String command = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
        List<String> commands = config.getStringList("reward.commands");
        commands.add(command);
        config.set("reward.commands", commands);
        saveAndReloadConfig(config);
        Utils.sendMessage(player, config.getString("command.messages.command_added"));
    }

    private void handleReloadCommand(Player player, Configuration config) {
        if (!player.hasPermission(config.getString("command.permission"))) {
            Utils.sendMessage(player, config.getString("command.messages.no_perm"));
            return;
        }

        saveAndReloadConfig(config);
        Utils.sendMessage(player, config.getString("command.messages.config_reloaded"));
    }

    private void saveAndReloadConfig(Configuration config) {
        BDiscordReward plugin = BDiscordReward.getPlugin();
        plugin.saveConfig();
        plugin.reloadConfig();
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
            suggestions.add("command");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("reward") && (args[1].equalsIgnoreCase("item") || args[1].equalsIgnoreCase("command"))) {
            suggestions.add("add");
            suggestions.add("remove");
        }
        return suggestions;
    }
}
