package net.teddy.chunkClaim.commands;

import com.jeff_media.morepersistentdatatypes.DataType;
import net.teddy.chunkClaim.ChunkClaim;
import net.teddy.chunkClaim.utils.ClaimsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ClaimCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player))
            return true;

        Chunk chunk = player.getLocation().getChunk();

        if (ClaimsUtils.isChunkClaimed(chunk)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    ChunkClaim.getInstance().getConfig().getString("chunkclaim_command_chunk_already_claimed")));

            return true;
        }

        int chunkMiddleX = chunk.getX() * 16 + 8;
        int chunkMiddleZ = chunk.getZ() * 16 + 8;
        int spawnHeight = ChunkClaim.getInstance().getConfig().getInt("chunkclaim_armorstand_spawn_height");

        ArmorStand armorStand = player.getWorld().spawn(new Location(player.getWorld(), chunkMiddleX, spawnHeight, chunkMiddleZ), ArmorStand.class);
        armorStand.setCustomName("%s %s, %s".formatted(chunkMiddleX, chunkMiddleZ, player.getUniqueId()));
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setInvulnerable(true);

        PersistentDataContainer pdc = armorStand.getPersistentDataContainer();
        pdc.set(ClaimsUtils.playerKey, DataType.UUID, player.getUniqueId());
        pdc.set(ClaimsUtils.claimedInstantKey, PersistentDataType.LONG, System.currentTimeMillis());

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                ChunkClaim.getInstance().getConfig().getString("chunkclaim_command_chunk_claimed")));

        return true;
    }
}
