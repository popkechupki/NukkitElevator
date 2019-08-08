package net.comorevi.nukkitplugin;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.plugin.PluginBase;

public class Elevator extends PluginBase implements Listener {

    public static int elevatorBlock = Block.RED_SANDSTONE_SLAB;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDataPacketReceive(DataPacketReceiveEvent event) {
        if (event.getPacket() instanceof PlayerActionPacket) {
            Player player = event.getPlayer();
            if (((PlayerActionPacket) event.getPacket()).action == PlayerActionPacket.ACTION_JUMP) {
                if (player.getLevel().getBlockIdAt(player.getFloorX(), player.getFloorY() - getAdjustmentCount(elevatorBlock), player.getFloorZ()) == elevatorBlock) {
                    for (int i = player.getFloorY() + 2; i < 256; i++) {
                        if (player.getLevel().getBlockIdAt(player.getFloorX(), i, player.getFloorZ()) == elevatorBlock) {
                            player.teleport(new Vector3(player.getFloorX() + 0.5, i + 1, player.getFloorZ() + 0.5));
                            break;
                        }
                    }
                }
            } else if (((PlayerActionPacket) event.getPacket()).action == PlayerActionPacket.ACTION_START_SNEAK) {
                if (player.getLevel().getBlockIdAt(player.getFloorX(), player.getFloorY() - getAdjustmentCount(elevatorBlock), player.getFloorZ()) == elevatorBlock) {
                    for (int i = player.getFloorY() - 2; i > 0; i--) {
                        if (player.getLevel().getBlockIdAt(player.getFloorX(), i, player.getFloorZ()) == elevatorBlock) {
                            player.teleport(new Vector3(player.getFloorX() + 0.5, i + 1, player.getFloorZ() + 0.5));
                            break;
                        }
                    }
                }
            }
        }
    }

    public int getAdjustmentCount(int blockId) {
        switch (blockId) {
            case Block.SLAB:
            case Block.RED_SANDSTONE_SLAB:
            case Block.WOOD_SLAB:
                return 0;

                default:
                    return 1;
        }
    }

}
