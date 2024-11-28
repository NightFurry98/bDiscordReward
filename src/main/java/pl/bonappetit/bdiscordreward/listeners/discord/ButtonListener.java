package pl.bonappetit.bdiscordreward.listeners.discord;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.bukkit.configuration.Configuration;
import pl.bonappetit.bdiscordreward.BDiscordReward;

import java.util.HashMap;

public class ButtonListener extends ListenerAdapter {

    private HashMap<String, Long> cooldown = new HashMap<>();

    @Override
    public void onButtonInteraction(final ButtonInteractionEvent event) {
        Configuration config = BDiscordReward.getPlugin().getConfig();
        if (event.getComponentId().equalsIgnoreCase("receive:reward")) {
            if (this.cooldown.containsKey(event.getGuild().getName())) {
                final long time = this.cooldown.get(event.getGuild().getName());
                if (time > System.currentTimeMillis()) {
                    return;
                }
            }
            this.cooldown.put(event.getGuild().getName(), System.currentTimeMillis() + 6000L);
            final TextInput lider = TextInput.create("reward:modal:nick", config.getString("modal.label"), TextInputStyle.SHORT).setMinLength(1)
                    .setRequired(true).setPlaceholder(config.getString("modal.placeholder")).build();
            final Modal modal = Modal.create("reward:modal", config.getString("modal.title")).addActionRow(lider).build();
            event.replyModal(modal).queue();
        }
    }

}
