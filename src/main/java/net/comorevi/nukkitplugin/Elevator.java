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
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.LinkedHashMap;

public class Elevator extends PluginBase implements Listener {

    public static int elevatorBlock;

    @Override
    public void onEnable() {
        initialize();
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

    public void initialize() {
        if (!this.getDataFolder().exists()) getDataFolder().mkdirs();
        Config config = new Config(
                new File(this.getDataFolder(), "config.yml"),
                Config.YAML,
                new LinkedHashMap<String, Object>() {
                    {
                        put("ElevatorBlock", Block.RED_SANDSTONE_SLAB);
                    }
                });
        config.save();
        int id = config.getInt("ElevatorBlock", Block.RED_SANDSTONE_SLAB);
        try {
            Block.get(id);
            elevatorBlock = id;
        } catch (ArrayIndexOutOfBoundsException e) {
            elevatorBlock = Block.RED_SANDSTONE_SLAB;
        }
    }

}
