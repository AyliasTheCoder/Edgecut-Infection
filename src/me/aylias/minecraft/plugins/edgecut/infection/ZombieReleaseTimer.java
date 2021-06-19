package me.aylias.minecraft.plugins.edgecut.infection;

import me.aylias.minecraft.plugins.aptlib.MessageTools;
import me.aylias.minecraft.plugins.aptlib.timers.CountdownTimer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ZombieReleaseTimer extends CountdownTimer<Main> {

    public ZombieReleaseTimer(Main main) {
        super(main, 300, 60, 30, 10, 5, 4, 3, 2, 1);
    }

    @Override
    public void onFinished() {
        main.broadcast(MessageTools.greenAnnouncement("Zombies have been #v!", "released", ChatColor.RED));
        InfectionTagListener.INSTANCE.players.getGroup("zombie")
                .forEach(p -> {
                    p.teleport(InfectionTagListener.INSTANCE.spawn);
                    p.getInventory().clear();
                    p.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                    p.setGameMode(GameMode.ADVENTURE);
                });
    }

    @Override
    public String getMessage() {
        return "Zombies will be released in #v";
    }
}
