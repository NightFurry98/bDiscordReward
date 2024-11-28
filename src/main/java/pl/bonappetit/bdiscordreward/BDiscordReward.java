package pl.bonappetit.bdiscordreward;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bonappetit.bdiscordreward.command.BDiscordRewardCommand;
import pl.bonappetit.bdiscordreward.database.MySQL;
import pl.bonappetit.bdiscordreward.listeners.spigot.PlayerJoinListener;
import pl.bonappetit.bdiscordreward.managers.UserManager;

import java.sql.SQLException;

public final class BDiscordReward extends JavaPlugin {

    private static BDiscordReward plugin;

    @Override
    public void onEnable() {
        final PluginManager pm = Bukkit.getPluginManager();
        (BDiscordReward.plugin = this).saveDefaultConfig();
        MySQL.connect();
        new DiscordBot(getPlugin().getConfig().getString("discord_bot_token"), getConfig());
        MySQL.update("CREATE TABLE IF NOT EXISTS `bdiscordreward` ("
                + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, "
                + "`nick` TEXT NOT NULL, "
                + "`next_reward_time` DATETIME NOT NULL, "
                + "`reward_count` INT(11) NOT NULL DEFAULT 0"
                + ");");
        UserManager.loadUsers();
        pm.registerEvents(new PlayerJoinListener(), this);
        this.getCommand("bdsicordreward").setExecutor(new BDiscordRewardCommand());
    }

    public static BDiscordReward getPlugin() {
        return BDiscordReward.plugin;
    }

    public void onDisable() {
        DiscordBot.shutdownBot();
        try {
            MySQL.disconnect();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
