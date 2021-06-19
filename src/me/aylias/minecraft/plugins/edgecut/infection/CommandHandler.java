package me.aylias.minecraft.plugins.edgecut.infection;

import me.aylias.minecraft.plugins.aptlib.MessageTools;
import me.aylias.minecraft.plugins.aptlib.PlayerTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    final Main main;

    public CommandHandler(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(MessageTools.greenAnnouncement("You do not have perms to do that!"));
        }

        if (command.getName().equalsIgnoreCase("zombie")) {
            if (args.length == 0) {
                sender.sendMessage(MessageTools.greenAnnouncement("You need to specify a player"));
                return true;
            }
            Player p = PlayerTools.byName(args[0]);
            if (p == null) {
                sender.sendMessage(MessageTools.greenAnnouncement(args[0] + " isn't a valid player name"));
                return true;
            }
            InfectionTagListener.INSTANCE.addZombie(p);
            sender.sendMessage(MessageTools.greenAnnouncement("Added " + args[0] +" as a zombie"));
            return true;
        }

        if (command.getName().equalsIgnoreCase("start")) {
            AirdropTimer.INSTANCE.setWorld(((Player) sender).getWorld());
            InfectionTagListener.INSTANCE.setWorld(((Player) sender).getWorld());
            InfectionTagListener.INSTANCE.start();
            sender.sendMessage(MessageTools.greenAnnouncement("Successfully started!"));
            return true;
        }

        if (command.getName().equalsIgnoreCase("reset")) {
            InfectionTagListener.INSTANCE.stop();
            AirdropTimer.INSTANCE.setWorld(((Player) sender).getWorld());
            InfectionTagListener.INSTANCE.setWorld(((Player) sender).getWorld());
            InfectionTagListener.INSTANCE.start();
            sender.sendMessage(MessageTools.greenAnnouncement("Successfully reset!"));

        }

        if (command.getName().equalsIgnoreCase("stop")) {
            InfectionTagListener.INSTANCE.stop();
            sender.sendMessage(MessageTools.greenAnnouncement("Successfully stopped!"));
        }
        return true;
    }
}
