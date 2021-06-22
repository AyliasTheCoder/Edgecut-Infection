package me.aylias.minecraft.plugins.edgecut.infection;

import me.aylias.minecraft.plugins.aptlib.AbstractMain;
import org.bukkit.GameRule;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;

import static me.aylias.minecraft.plugins.aptlib.ListTools.*;

public class Main extends AbstractMain {

    public static Main INSTANCE;
//    AirdropTimer airdropTimer;

    @Override
    public void onEnableCallback() {
        getServer().getWorlds().forEach(world -> world.setGameRule(GameRule.NATURAL_REGENERATION, false));
        INSTANCE = this;
        ItemUseController.init(this);
    }

    @Override
    public List<Listener> getListeners() {
//        airdropTimer = new AirdropTimer(this, getServer().getWorld("world"));
        return toList(new InfectionTagListener(this));
    }

    @Override
    public Map<String, CommandExecutor> getExecutors() {
        CommandHandler commandHandler = new CommandHandler(this);


        return toMap(toPair("start", commandHandler),
                toPair("zombie", commandHandler),
                toPair("reset", commandHandler),
                toPair("staff", commandHandler),
                toPair("human", commandHandler));
    }
}
