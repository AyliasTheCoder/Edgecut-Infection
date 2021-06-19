package me.aylias.minecraft.plugins.edgecut.infection;

import me.aylias.minecraft.plugins.aptlib.MessageTools;
import me.aylias.minecraft.plugins.aptlib.collections.GroupedCollection;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfectionTagListener implements Listener {

    public static InfectionTagListener INSTANCE;

    final Main main;
    Location spawn;
    public GroupedCollection<Player> players = new GroupedCollection<>(
            "player",
            "zombie",
            "spectator");
    boolean started = false;
    ZombieReleaseTimer zTimer;
    Objective objective;
    List<String> scores = new ArrayList<>();

    public InfectionTagListener(Main main) {
        INSTANCE = this;
        this.main = main;
        spawn = new Location(null, 142, 81, 374);
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        objective = board.registerNewObjective("dummy", "counter", ChatColor.DARK_GREEN + "Edgecut" + ChatColor.DARK_RED + "Infection");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        updateScoreboard();
        main.getServer().getOnlinePlayers().forEach(p -> p.setScoreboard(board));
    }

    public void updateScoreboard() {
        Collections.reverse(scores);
        for (String score : scores) {
            objective.getScoreboard().resetScores(score);
        }

        scores.clear();
        scores.add(ChatColor.GREEN + "Humans: " + ChatColor.BLUE + players.getGroup("player").size());
        scores.add(ChatColor.RED + "Infected: " + ChatColor.BLUE + players.getGroup("zombie").size());
        scores.add("");
        scores.add("");
        scores.add(ChatColor.GOLD + "Partnered with" + ChatColor.BLUE + " Bisect Hosting");
        scores.add(ChatColor.AQUA + "Twitter:" + ChatColor.DARK_BLUE + " @EdgecutEvents");

        Collections.reverse(scores);

        int i = 0;
        for (String score : scores) {
            objective.getScore(score).setScore(i);
            i++;
        }
    }

    public void setWorld(World world) {
        spawn.setWorld(world);
    }

    public void addZombie(Player player) {
        players.addItem("zombie", player);
        players.removeItem("player", player);
        players.removeItem("spectator", player);

        //SkinGrabber.makeZombie(player);
        main.getServer().dispatchCommand(
                main.getServer().getConsoleSender(),
                "cmi skin zombie " + player.getName());

        updateScoreboard();
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
        if (started) {
            int remaining = players.getGroup("player").size();
            if (remaining == 2)
                main.broadcast(MessageTools.greenAnnouncement("#v has died and is in 3rd place!", player));
            else if (remaining == 1)
                main.broadcast(MessageTools.greenAnnouncement("#v has died and is in 2nd place!", player));
            else if (remaining == 0) {
                main.broadcast(MessageTools.greenAnnouncement("#v has won!", player));
                stop();
            } else
                main.broadcast(MessageTools.greenAnnouncement("#v has died!", player));
        }
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
    public void onPlayerUse(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Material clicked = e.getClickedBlock().getType();
            if (clicked == Material.LEVER ||
                    clicked == Material.CHEST) {
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

        if ((!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))) {
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

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String text = e.getMessage();
        Player sender = e.getPlayer();
        e.setCancelled(true);

        String pre = "";
        if (players.isInGroup("player", sender))
            pre += "[" + ChatColor.GREEN + "Human" + ChatColor.WHITE + "]";
        if (players.isInGroup("zombie", sender))
            pre += "[" + ChatColor.RED + "Infected" + ChatColor.WHITE + "]";
        if (players.isInGroup("spectator", sender))
            pre += "[" + ChatColor.GRAY + "Staff" + ChatColor.WHITE + "]";

        pre += "<" + sender.getName() + ">";
        final String send = pre + text;

        main.getServer().getOnlinePlayers().forEach(p ->
                p.sendMessage(send));
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        main.getServer().dispatchCommand(
                main.getServer().getConsoleSender(),
                "cmi skin off " + e.getPlayer().getName());
        if (started) {
            players.removeItem("player", e.getPlayer());
            players.removeItem("zombie", e.getPlayer());
        }

        updateScoreboard();
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        if (started) {
            players.removeItem("player", e.getPlayer());
            players.removeItem("zombie", e.getPlayer());
        }

        updateScoreboard();
    }

    private void killPlayer(Player player) {
        player.teleport(spawn);
        player.setHealth(20);
        player.getInventory().clear();
        players.removeItem("player", player);
        if (!players.isInGroup("zombie", player))
            addZombie(player);
    }

    public boolean isZombie(Player player) {
        return players.isInGroup("zombie", player);
    }

    public void start() {
        main.getServer().getOnlinePlayers().forEach(p -> {
            if (!isZombie(p) &&
                !players.isInGroup("spectator", p)) {
                players.addItem("player", p);
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

        zTimer = new ZombieReleaseTimer(main);
        zTimer.start();

        players.getGroup("player").forEach(p -> {
            if (!isZombie(p)) {
                p.teleport(spawn);
                p.getInventory().clear();
                p.setGameMode(GameMode.ADVENTURE);
                p.setScoreboard(objective.getScoreboard());
            }
        });

        updateScoreboard();
        new BorderTimer(spawn.getWorld(), main);

        started = true;
    }

    public void stop() {
        AirdropTimer.INSTANCE.stop();
        started = false;
        players.clearGroups();
        if (!zTimer.isCancelled()) {
            zTimer.terminate();
        }
    }

    public void addSpec(Player player) {
        players.removeItem("zombie", player);
        players.removeItem("player", player);
        players.addItem("spectator", player);
        updateScoreboard();
    }

    public void addHuman(Player player) {
        players.removeItem("zombie", player);
        players.removeItem("spectator", player);
        players.addItem("player", player);
        updateScoreboard();
    }
}
