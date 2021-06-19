package me.aylias.minecraft.plugins.edgecut.infection;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class FlashlightTimer extends BukkitRunnable {

    final Main main;
    final String id;
    final Player player;
    public boolean running = false;

    public FlashlightTimer(Main main, String id, Player player) {
        this.main = main;
        this.id = id;
        this.player = player;
        runTaskTimer(main, 15, 15);
    }

    public void stop() {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        running = false;
    }

    public void start() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000, 1, false));
        running = true;
    }

    @Override
    public void run() {
        if (!running) return;
    }
}
