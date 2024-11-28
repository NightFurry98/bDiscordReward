package pl.bonappetit.bdiscordreward.utils;


import org.bukkit.enchantments.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public class ParseItemStack {

    public static Map<Enchantment, String> enchantmentMap = new LinkedHashMap<>();
    public static Map<String, Enchantment> stringMap = new LinkedHashMap<>();

    public static String toString(final ItemStack itemStack) {
        if (itemStack == null) {
            return "empty";
        }
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("type=" + itemStack.getType() + " ");
        stringBuilder.append("amount=" + itemStack.getAmount() + " ");
        stringBuilder.append("data=" + itemStack.getDurability() + " ");
        if (itemStack.hasItemMeta()) {
            final ItemMeta meta = itemStack.getItemMeta();
            if (meta.hasDisplayName()) {
                stringBuilder.append("name=" + meta.getDisplayName().replace(" ", "_") + " ");
            }
            if (meta.hasLore()) {
                String lore = "";
                for (final String line : meta.getLore()) {
                    lore = lore + line.replace(" ", "_") + "%nl%";
                }
                lore = lore.substring(0, lore.length() - 4);
                stringBuilder.append("lore=" + lore + " ");
            }
            if (meta.hasCustomModelData()) {
                stringBuilder.append("customd=" + meta.getCustomModelData() + " ");
            }
            if (meta instanceof LeatherArmorMeta) {
                final LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                final Color color = armorMeta.getColor();
                if (!color.equals(Bukkit.getItemFactory().getDefaultLeatherColor())) {
                    final String colorHex = String.format("#%06x", color.asRGB() & 0xFFFFFF);
                    stringBuilder.append("color=" + colorHex + " ");
                }
            }
        }
        if (itemStack.getEnchantments() != null && !itemStack.getEnchantments().isEmpty()) {
            for (final Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
                stringBuilder.append(enchantmentMap.get(entry.getKey()) + "=" + entry.getValue() + " ");
            }
        }
        return stringBuilder.toString();
    }

    public static ItemStack parseItem(final String itemString) {
        final String[] itemParts = itemString.split(" ");
        String type = null;
        int amount = 1;
        final Map<Enchantment, Integer> enchantments = new HashMap<>();
        String displayName = null;
        String lore = null;
        Color color = null;
        int customd = 0;
        for (final String part : itemParts) {
            final String[] keyValue = part.split("=");
            if (keyValue.length == 2) {
                final String key = keyValue[0];
                final String value = keyValue[1];
                final String s = key;
                switch (s) {
                    case "type": {
                        type = value;
                        break;
                    }
                    case "amount": {
                        amount = Integer.parseInt(value);
                        break;
                    }
                    case "name": {
                        displayName = Utils.fixColor(value.replace("_", " "));
                        break;
                    }
                    case "lore": {
                        lore = value;
                        break;
                    }
                    case "customd": {
                        customd = Integer.parseInt(value);
                        break;
                    }
                    case "color": {
                        color = Color.fromRGB(Integer.parseInt(value.replace("#", ""), 16));
                        break;
                    }
                    default: {
                        final Enchantment enchantment = Enchantment.getByName(key);
                        if (enchantment != null) {
                            final int level = Integer.parseInt(value);
                            enchantments.put(enchantment, level);
                            break;
                        }
                        break;
                    }
                }
            }
        }
        final ItemStack item = new ItemStack(Material.getMaterial(type), amount);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(Integer.valueOf(customd));
        if (itemMeta != null) {
            if (displayName != null) {
                itemMeta.setDisplayName(displayName);
            }
            if (lore != null) {
                final String[] lines = lore.split("%nl%");
                final List<String> lores = new ArrayList<>();
                for (final String line : lines) {
                    lores.add(Utils.fixColor(line.replace("_", " ")));
                }
                itemMeta.setLore(lores);
            }
            if (itemMeta instanceof LeatherArmorMeta && color != null) {
                final LeatherArmorMeta armorMeta = (LeatherArmorMeta)itemMeta;
                armorMeta.setColor(color);
            }
            for (final Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
            item.setItemMeta(itemMeta);
        }
        return item;
    }

    static{
        Arrays.asList(Enchantment.values()).stream().forEach(enchant -> enchantmentMap.put(enchant, enchant.getName()));
        Arrays.asList(Enchantment.values()).stream().forEach(enchant -> stringMap.put(enchant.getName(), enchant));
    }
}
