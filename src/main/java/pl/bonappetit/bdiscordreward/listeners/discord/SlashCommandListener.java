package pl.bonappetit.bdiscordreward.listeners.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.interactions.commands.build.*;
import java.util.*;
import java.util.List;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.configuration.Configuration;
import pl.bonappetit.bdiscordreward.BDiscordReward;

public class SlashCommandListener extends ListenerAdapter {


    @Override
    public void onGuildReady(final GuildReadyEvent event) {
        final List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("rewardchannel", "Set this channel as the channel to claim rewards"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        final String name = event.getName();
        if ("rewardchannel".equals(name)) {
            if (event.getMember() != null && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                EmbedBuilder embed = createEmbedFromConfig();
                if (embed != null) {
                    event.getChannel().sendMessageEmbeds(embed.build()).setActionRow(Button.primary("receive:reward", BDiscordReward.getPlugin().getConfig().getString("button_name"))).queue();
                    event.reply(":)").setEphemeral(true).queue();
                }
            } else {
                event.reply("You do not have permission for this command!").setEphemeral(true).queue();
            }
        }
    }


    private EmbedBuilder createEmbedFromConfig() {
        Configuration config = BDiscordReward.getPlugin().getConfig();
        try {
            EmbedBuilder embed = new EmbedBuilder();

            String title = config.getString("embed.title", "Rewards");
            String description = config.getString("embed.description", "Click the button below to claim your reward.");
            String footer = config.getString("embed.footer", "Bot Minecraft");
            String thumbnailUrl = config.getString("embed.thumbnail_url", null);
            String colorHex = config.getString("embed.color", "#FFFFFF");

            embed.setTitle(title);
            embed.setDescription(description);
            embed.setColor(Color.decode(colorHex));
            embed.setFooter(footer);

            if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                embed.setThumbnail(thumbnailUrl);
            }
            if (config.contains("embed.fields")) {
                config.getMapList("embed.fields").forEach(field -> {
                    String name = (String) field.get("name");
                    String value = (String) field.get("value");
                    boolean inline = Boolean.parseBoolean(field.get("inline").toString());
                    embed.addField(name, value, inline);
                });
            }
            return embed;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}