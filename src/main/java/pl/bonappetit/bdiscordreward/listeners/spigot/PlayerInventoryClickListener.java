package pl.bonappetit.bdiscordreward.listeners.spigot;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pl.bonappetit.bdiscordreward.BDiscordReward;
import pl.bonappetit.bdiscordreward.utils.ParseItemStack;
import pl.bonappetit.bdiscordreward.utils.Utils;

import java.util.List;

public class PlayerInventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player p = (Player) event.getWhoClicked();
        Configuration config = BDiscordReward.getPlugin().getConfig();
        if (event.getView().getTitle().equals(Utils.fixColor(config.getString("gui.name")))) {
            event.setCancelled(true);
            ItemStack itemClicked = event.getCurrentItem();
            int clickedSlot = event.getSlot();
            if (itemClicked.getType() == Material.PAPER) {
                List<String> configList = config.getStringList("reward.commands");
                if (clickedSlot >= configList.size()) {
                    return;
                }
                if (!itemClicked.getItemMeta().getDisplayName().equals(Utils.fixColor("&b"+configList.get(clickedSlot)))) {
                    return;
                }
                configList.remove(clickedSlot);
                config.set("reward.commands", configList);
                BDiscordReward.getPlugin().saveConfig();
                BDiscordReward.getPlugin().reloadConfig();
                Utils.openRewardGUICommand(p);
                Utils.sendMessage(p, config.getString("command.messages.command_removed"));
            }
            if (itemClicked == null || itemClicked.getType() == Material.AIR) {
                return;
            }
            List<String> configList = config.getStringList("reward.items");
            if (clickedSlot >= configList.size()) {
                return;
            }
            ItemStack itemRewardList = ParseItemStack.parseItem(configList.get(clickedSlot));
            if (itemClicked.getType() == itemRewardList.getType() &&
                    itemClicked.getItemMeta().getCustomModelData() == itemRewardList.getItemMeta().getCustomModelData() &&
                    itemClicked.getItemMeta().getDisplayName().equals(itemRewardList.getItemMeta().getDisplayName())) {
                configList.remove(clickedSlot);
                config.set("reward.items", configList);
                BDiscordReward.getPlugin().saveConfig();
                BDiscordReward.getPlugin().reloadConfig();
                Utils.openRewardGUI(p);
                Utils.sendMessage(p, config.getString("command.messages.item_removed"));
            }
        }
    }
}
