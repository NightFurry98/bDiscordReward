package pl.bonappetit.bdiscordreward.listeners.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.bonappetit.bdiscordreward.basic.User;
import pl.bonappetit.bdiscordreward.managers.UserManager;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final User user = UserManager.getUser(p.getName());
        if (user == null) {
            UserManager.createUserData(p.getName());
        }
    }
}
