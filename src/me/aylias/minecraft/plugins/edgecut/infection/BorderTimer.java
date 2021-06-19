package me.aylias.minecraft.plugins.edgecut.infection;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderTimer extends BukkitRunnable {

    final World world;

    public BorderTimer(World world, Main main) {
        this.world = world;
        runTaskTimer(main, 20*60, 20*60);
    }

    @Override
    public void run() {
        world.getWorldBorder().setSize(world.getWorldBorder().getSize() - 23.75, 20*60);
        if (world.getWorldBorder().getSize() <= 250)
            cancel();
    }
}
