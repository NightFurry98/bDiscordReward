package pl.bonappetit.bdiscordreward.listeners.discord;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.bonappetit.bdiscordreward.BDiscordReward;
import pl.bonappetit.bdiscordreward.basic.User;
import pl.bonappetit.bdiscordreward.managers.UserManager;
import pl.bonappetit.bdiscordreward.utils.ParseItemStack;
import pl.bonappetit.bdiscordreward.utils.Utils;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(final ModalInteractionEvent event) {
        Configuration config = BDiscordReward.getPlugin().getConfig();
        if (!event.getModalId().equals("reward:modal")) return;
        final String nick = event.getValue("reward:modal:nick").getAsString();
        Player player = Bukkit.getPlayer(nick);
        User user = UserManager.getUser(player.getName());
        if (player == null || user == null) {
            event.reply(config.getString("reward.messages.discord.offline").replace("@player", nick)).setEphemeral(true).queue();
            return;
        }
        boolean canReceiveMultipleRewards = config.getBoolean("receiving_more_times") && user.canReceiveReward();
        boolean canReceiveSingleReward = !config.getBoolean("receiving_more_times") && user.getCount() <= 0;
        if (canReceiveMultipleRewards || canReceiveSingleReward) {
            if (config.getBoolean("reward.execute_commands")) {
                executeRewardCommands(config, player);
            }
            if (config.getBoolean("reward.give_items")) {
                giveRewardItems(config, player);
            }
            String successMessageSpigot = config.getString("reward.messages.spigot.success")
                    .replace("@player", player.getName())
                    .replace("@time", user.getFormattedRewardTime().toString());
            String successMessageDiscord = config.getString("reward.messages.discord.success")
                    .replace("@player", player.getName())
                    .replace("@time", user.getFormattedRewardTime().toString());
            user.setCount(user.getCount()+1);
            user.setNextRewardTime(config.getString("cooldown_claiming"));
            user.save();
            Utils.sendMessage(player, successMessageSpigot);
            event.reply(successMessageDiscord).setEphemeral(true).queue();
        } else {
            String failureMessage;
            if (config.getBoolean("receiving_more_times")) {
                failureMessage = config.getString("reward.messages.discord.wait")
                        .replace("@player", player.getName())
                        .replace("@time", user.getFormattedRewardTime().toString());
            } else {
                failureMessage = config.getString("reward.messages.discord.fault")
                        .replace("@player", player.getName())
                        .replace("@time", user.getFormattedRewardTime().toString());
            }
            event.reply(failureMessage).setEphemeral(true).queue();
        }
    }

    private void executeRewardCommands(Configuration config, Player player) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        for (String command : config.getStringList("reward.commands")) {
            Bukkit.getScheduler().runTask(BDiscordReward.getPlugin(), () -> {
                Bukkit.dispatchCommand(console, command.replace("@player", player.getName()));
            });
        }
    }

    private void giveRewardItems(Configuration config, Player player) {
        for (String item : config.getStringList("reward.items")) {
            ItemStack itemStack = ParseItemStack.parseItem(item);
            Utils.give(player, itemStack);
        }
    }
}
