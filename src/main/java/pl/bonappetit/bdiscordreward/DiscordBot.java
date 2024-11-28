package pl.bonappetit.bdiscordreward;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.configuration.Configuration;
import pl.bonappetit.bdiscordreward.listeners.discord.ButtonListener;
import pl.bonappetit.bdiscordreward.listeners.discord.ModalListener;
import pl.bonappetit.bdiscordreward.listeners.discord.SlashCommandListener;

import java.util.Collections;

public class DiscordBot {
    private static JDA jda;

    public DiscordBot(final String token, Configuration config) {
        jda = this.createUser(token, config);
    }

    private JDA createUser(final String token, Configuration config) {
        String activityType = config.getString("activity.type");
        String activityMessage = config.getString("activity.message");
        Activity activity;
        switch (activityType.toLowerCase()) {
            case "playing":
                activity = Activity.playing(activityMessage);
                break;
            case "streaming":
                activity = Activity.streaming(activityMessage, config.getString("activity.link"));
                break;
            case "listening":
                activity = Activity.listening(activityMessage);
                break;
            case "watching":
                activity = Activity.watching(activityMessage);
                break;
            case "competing":
                activity = Activity.competing(activityMessage);
                break;
            case "custom":
                activity = Activity.customStatus(activityMessage);
                break;
            default:
                throw new IllegalArgumentException("Invalid activity type: " + activityType);
        }
        JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                .setActivity(activity)
                .addEventListeners(new SlashCommandListener(), new ButtonListener(), new ModalListener())
                .build();
        return jda;
    }

    public static void shutdownBot() {
        if (jda != null) {
            jda.shutdown();
        }
    }
}
