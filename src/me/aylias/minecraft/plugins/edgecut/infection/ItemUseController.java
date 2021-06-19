package me.aylias.minecraft.plugins.edgecut.infection;

import me.aylias.minecraft.plugins.aptlib.listeners.ItemUseListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemUseController {

    public static ItemUseListener<Main> itemUseListener;
    private static Main main;
    static int flashlightId = 0;

    public static void init(Main main) {
        itemUseListener = new ItemUseListener<>(main);
        main.getServer().getPluginManager().registerEvents(itemUseListener, main);
        ItemUseController.main = main;

        itemUseListener.register("Flashlight | ON", ItemUseController::flashlight, ItemUseListener.UseType.RIGHT);
        itemUseListener.register("Flashlight | OFF", ItemUseController::flashlight, ItemUseListener.UseType.RIGHT);

        itemUseListener.register("L1 Medkit", ItemUseController::medkitL1, ItemUseListener.UseType.RIGHT_SHRINK);
        itemUseListener.register("L2 Medkit", ItemUseController::medkitL2, ItemUseListener.UseType.RIGHT_SHRINK);
        itemUseListener.register("L3 Medkit", ItemUseController::medkitL3, ItemUseListener.UseType.RIGHT_SHRINK);

        itemUseListener.register("L1 Adrenaline Shot", ItemUseController::adrenalineL1, ItemUseListener.UseType.RIGHT_SHRINK);
        itemUseListener.register("L2 Adrenaline Shot", ItemUseController::adrenalineL2, ItemUseListener.UseType.RIGHT_SHRINK);
        itemUseListener.register("L3 Adrenaline Shot", ItemUseController::adrenalineL3, ItemUseListener.UseType.RIGHT_SHRINK);
    }

    public static void flashlight(Player player, int slot) {
        ItemStack item = player.getInventory().getItem(slot);
        ItemMeta meta = item.getItemMeta();

        if (meta.getDisplayName().contains("OFF"))  {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 0));
            meta.setDisplayName(meta.getDisplayName().replace("OFF", "ON"));
        } else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            meta.setDisplayName(meta.getDisplayName().replace("ON", "OFF"));
        }

        item.setItemMeta(meta);
        player.getInventory().setItem(slot, item);
    }

    public static void medkitL1(Player player, int slot) {
        player.setHealth(player.getHealth() + 6);
    }

    public static void medkitL2(Player player, int slot) {
        player.setHealth(player.getHealth() + 10);
    }

    public static void medkitL3(Player player, int slot) {
        player.setHealth(player.getHealth() + 14);
    }

    public static void adrenalineL1(Player player, int slot) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60*20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 1));
    }

    public static void adrenalineL2(Player player, int slot) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60*20*2, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20*2, 2));
    }

    public static void adrenalineL3(Player player, int slot) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60*20*3, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20*3, 3));
    }
}
