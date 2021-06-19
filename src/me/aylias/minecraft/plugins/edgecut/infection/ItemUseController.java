package me.aylias.minecraft.plugins.edgecut.infection;

import me.aylias.minecraft.plugins.aptlib.listeners.ItemUseListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

import static me.aylias.minecraft.plugins.aptlib.ListTools.toList;

public class ItemUseController {

    public static ItemUseListener<Main> itemUseListener;
    private static Main main;
    static Map<String, FlashlightTimer> timerMap = new HashMap<>();
    static int flashlightId = 0;

    public static void init(Main main) {
        itemUseListener = new ItemUseListener<>(main);
        main.getServer().getPluginManager().registerEvents(itemUseListener, main);
        ItemUseController.main = main;

        String flashlight = "Flashlight | #m | Battery: #l";

        for (int i = 1; i <= 100; i++) {
            itemUseListener.register(flashlight
                            .replace("#m", "ON")
                            .replace("#l", i + ""),
                    ItemUseController::flashlight, ItemUseListener.UseType.RIGHT);

            itemUseListener.register(flashlight
                            .replace("#m", "OFF")
                            .replace("#l", i + ""),
                    ItemUseController::flashlight, ItemUseListener.UseType.RIGHT);
        }
    }



    public static void flashlight(Player player, int slot) {
        ItemStack item = player.getInventory().getItem(slot);
        ItemMeta meta = item.getItemMeta();

        if (meta.getDisplayName().contains("OFF"))  {
            if (!meta.hasLore()) {
                meta.setLore(toList(flashlightId + ""));
                timerMap.put(flashlightId + "", new FlashlightTimer(main, flashlightId + "", player));
            }
            timerMap.get(flashlightId + "").start();
            meta.setDisplayName(meta.getDisplayName().replace("OFF", "ON"));
        } else {
            timerMap.get(meta.getLore().get(0)).stop();
            meta.setDisplayName(meta.getDisplayName().replace("ON", "OFF"));
        }

        item.setItemMeta(meta);
        player.getInventory().setItem(slot, item);
    }
}
