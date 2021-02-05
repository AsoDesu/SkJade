package com.ankoki.skjade.nms.v1_15_R1;

import com.ankoki.skjade.api.NMS;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSHandler implements NMS {

    @Override
    public void sendDemo(Player player) {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(5, 0);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void playFakeDamage(Player[] of, Player[] to) {
        for (Player p : of) {
            EntityPlayer handle = ((CraftPlayer) p).getHandle();
            PacketPlayOutAnimation packet = new PacketPlayOutAnimation(handle, 1);
            for (Player p1 : to) {
                ((CraftPlayer) p1).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
}
