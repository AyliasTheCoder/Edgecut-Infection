package me.aylias.minecraft.plugins.edgecut.infection;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SkinGrabber {

    public static void makeZombie(Player player) {
        GameProfile profile = ((CraftPlayer)player).getHandle().getProfile();

        Main.getPlugin(Main.class).getServer().getOnlinePlayers().forEach(p -> {
            PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                    ((CraftPlayer)player).getHandle()));
        });

        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", getSkin());

        Main.getPlugin(Main.class).getServer().getOnlinePlayers().forEach(p -> {
            PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                    ((CraftPlayer)player).getHandle()));
        });

    }

    private static Property getSkin() {
        return new Property("textures", "ewogICJ0aW1lc3R" +
                "hbXAiIDogMTYyMjExMTY4MjEwNiwKICAicHJvZmlsZUlkIiA6" +
                "ICI0ZWQ4MjMzNzFhMmU0YmI3YTVlYWJmY2ZmZGE4NDk1NyIsC" +
                "iAgInByb2ZpbGVOYW1lIiA6ICJGaXJlYnlyZDg4IiwKICAic2l" +
                "nbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiI" +
                "DogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHR" +
                "wOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzc5Y" +
                "zNmYzY3ZWUzY2UwOWZmN2E5YzllMWM3MWE4NDU1Zjk5YzQxZjB" +
                "iOGY1ZGRiZTJiOTliMTE0OTMwN2JlYjciCiAgICB9CiAgfQp9",
                "fMFeKjs4OgURSFMYq1r+XGhakBtxvAT2e2J7jbgl4" +
                        "aSh+ZUPDTd3G1ZIQeyvZqTzFbK6GQ5864zSnpNyW0" +
                        "FoISua/vaO7UjjhaU/xFGON6BhWVmyVbN6NvIBbhu" +
                        "lt0Z+xMbCwiVL1YFx+5twJfDlWX898f0y4BIimJV" +
                        "aLhCsoW5eGNPKFAFEC0M9d1ebuxsu6BHj4jH/Q3f" +
                        "wM5de8y1LjS2pQdAEdA+qTczPV8bl+U6uBbimL8qy" +
                        "Frsx0ESM3DBqf/CAGTm1KAY1SGN7331FWzj6Y+B8d" +
                        "ZeNNGc4Z60IKGioOoBrUtf6Vx2PuOSpljBQkzRG8b" +
                        "s3Wz8xj9nUoB7YYgfFGM01P0EZ0H5cuMtdltptibgz" +
                        "52X1l4K1Kl6KZRgcNPFup1i9wNvPY+0JVg6PX8B1Rg" +
                        "RrOyhv7SIaIP1aZsdnFnIxbr2/c9kk4i0i5xeAkJmQ" +
                        "YIZ0B5Qb0M/JIHB7iYzwWvmsxSf2rxm/iyPwgua2Zl" +
                        "2/Qx4IAMOnW3t29XMyo6aqiRbRq+WF2kc7stF54yfN" +
                        "AQnmLCOtEI3G3nuL7Z3K+8PP4oGRrZnVxhaZRnhibB" +
                        "70kK22vZZnsbXmUW9fbdRo+f9DMqoi6IdkJxfkTiXo" +
                        "DljxjArSo1UA0g++Y2DFSLu/gXdPe3SqzRVULVqZOU" +
                        "b38IY1P6N7p8eIDYj21+s=");
    }
}
