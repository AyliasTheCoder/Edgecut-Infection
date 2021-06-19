package me.aylias.minecraft.plugins.edgecut.infection;

import me.aylias.minecraft.plugins.aptlib.collections.GroupedCollection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class InfectionTagListener implements Listener {

    public static InfectionTagListener INSTANCE;

    final Main main;
    Location spawn;
    GroupedCollection<Player> players = new GroupedCollection<>(
            "player",
            "zombie",
            "spectator");
    boolean started = false;

    public InfectionTagListener(Main main) {
        INSTANCE = this;
        this.main = main;
        spawn = new Location(null, 142, 81, 374);
    }

    public void setWorld(World world) {
        spawn.setWorld(world);
    }

    public void addZombie(Player player) {
        players.addItem("zombie", player);
        players.removeItem("player", player);
        String name = ChatColor.GREEN + "[Zombie] " + ChatColor.DARK_GREEN + player.getName();

        //SkinGrabber.makeZombie(player);
        main.getServer().dispatchCommand(
                main.getServer().getConsoleSender(),
                "cmi skin zombie " + player.getName());

        main.getServer().dispatchCommand(
                main.getServer().getConsoleSender(),
                "cmi nick " + name + " " + player.getName());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!started) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Player) {
            if (!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onZombieAttack(EntityDamageByEntityEvent e) {
        if (!started) {
            e.setCancelled(true);
            return;
        }

        if ((!(e.getEntity() instanceof Player) || !(e.getEntity() instanceof Player))) {
            e.setCancelled(true);
            return;
        }

        Player damager = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();

        if (!players.isInGroup("zombie", damager) &&
                !players.isInGroup("zombie", victim)) {
            e.setCancelled(true);
            return;
        }

        if (players.isInGroup("zombie", damager) &&
            players.isInGroup("zombie", victim)) {
            e.setCancelled(true);
            return;
        }

        if (victim.getHealth() - e.getDamage() <= 0) {
            e.setCancelled(true);
            killPlayer((victim));
        }
    }

    private void killPlayer(Player player) {
        player.teleport(spawn);
        player.setHealth(20);
        if (!players.isInGroup("zombie", player))
            addZombie(player);
    }

    public boolean isZombie(Player player) {
        return players.isInGroup("zombie", player);
    }

    public void start() {
        main.getServer().getOnlinePlayers().forEach(p -> {
            if (!players.isInGroup("zombie", p) ||
                !players.isInGroup("spectator", p)) {
                players.addItem("players", p);
            }
        });

        AirdropTimer.INSTANCE.start();
        spawn.getWorld().getEntities().forEach(e -> {
            if (e instanceof ArmorStand) {
                e.remove();
            }
        });
        WorldBorder border = spawn.getWorld().getWorldBorder();
        border.setCenter(142, 374);
        border.setSize(1675);
        started = true;
    }

    public void stop() {
        AirdropTimer.INSTANCE.stop();
        started = false;
        players.clearGroups();
    }
}
