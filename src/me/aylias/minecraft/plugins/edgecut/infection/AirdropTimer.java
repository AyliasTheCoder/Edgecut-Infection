package me.aylias.minecraft.plugins.edgecut.infection;

import me.aylias.minecraft.plugins.aptlib.ListeningTimer;
import me.aylias.minecraft.plugins.aptlib.MessageTools;
import me.aylias.minecraft.plugins.aptlib.WorldTools;
import me.aylias.minecraft.plugins.aptlib.collections.WeightedCollection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AirdropTimer extends ListeningTimer<Main> {

    public static AirdropTimer INSTANCE;

    World world;

    List<ItemStack> nextAirdrop = new ArrayList<>();
    ItemStack helmet;
    WeightedCollection<ItemStack> possible = new WeightedCollection<>();
    int cmd = 250;
    Random random = new Random();
    public AirdropTimer(Main main, World world) {
        super(main, 0, 20*30, "Airdrop");
        INSTANCE = this;
        this.world = world;
        helmet = new ItemStack(Material.STICK);
        ItemMeta helmetMeta = helmet.getItemMeta();
        helmetMeta.setCustomModelData(1000);
        helmet.setItemMeta(helmetMeta);

        addPossible(Material.CARROT_ON_A_STICK, 1, 50, "Flashlight | OFF | Battery: 100");
        addPossible(1, 20, "L1 Medkit");
        addPossible(1, 8, "L2 Medkit");
        addPossible(1, 2, "L3 Medkit");
        addPossible(1, 7, "L1 Adrenaline Shot");
        addPossible(1, 3, "L2 Adrenaline Shot");
        addPossible(1, 1, "L3 Adrenaline Shot");
        addPossible(1, 1, "Pistol");
        addPossible(1, 1, "Shotgun");
        addPossible(1, 1, "Sniper");
        addPossible(64, 1, "Bullet");
        //addPossible(1, 2, "Glider");
    }


    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void run() {
        nextAirdrop = new ArrayList<>();
        Location location = WorldTools.getHighestGround(InfectionTagListener.INSTANCE.spawn.clone()
                .add(random.nextInt(1000), 1, random.nextInt(1000)));
        ArmorStand stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        //stand.setGravity(true);
        stand.setCustomNameVisible(false);
        stand.getEquipment().setHelmet(helmet);
        stand.setVisible(false);
        main.getServer().getOnlinePlayers().forEach(p -> {
            if (!InfectionTagListener.INSTANCE.isZombie(p)) {
                p.sendMessage(MessageTools.greenAnnouncement(
                        "Airdrop at X: " + location.getBlockX()
                                + " Y: " + location.getBlockY()
                                + " Z: " + location.getBlockZ()));
            }
        });
    }

    @EventHandler
    public void playerHitStand(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) ||
        !(e.getEntity() instanceof ArmorStand)) return;

        Player damager = (Player) e.getDamager();
        Random random = new Random();

        int count = random.nextInt(8) + 1;
        for (int i = 0; i < count; i++) {
            damager.getInventory().addItem(possible.next());
        }

        e.getEntity().remove();
    }

    void addPossible(Material material, int count, int weight, String name) {
        ItemStack toAdd = new ItemStack(material, count);
        ItemMeta meta = toAdd.getItemMeta();
        meta.setCustomModelData(cmd);
        cmd++;
        meta.setDisplayName(name);
        toAdd.setItemMeta(meta);

        possible.add(weight, toAdd);
    }

    void addPossible(int count, int weight, String name) {
        ItemStack toAdd = new ItemStack(Material.STICK, count);
        ItemMeta meta = toAdd.getItemMeta();
        meta.setCustomModelData(cmd);
        cmd++;
        meta.setDisplayName(name);
        toAdd.setItemMeta(meta);

        possible.add(weight, toAdd);
    }

    void addPossible(Material material, int count, int weight) {
        possible.add(weight, new ItemStack(material, count));
    }
}