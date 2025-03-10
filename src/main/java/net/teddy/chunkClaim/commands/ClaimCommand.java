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

        // Summon armor stand
        ArmorStand armorStand = player.getWorld().spawn(new Location(player.getWorld(), chunkMiddleX, spawnHeight, chunkMiddleZ), ArmorStand.class);
        armorStand.setCustomName("%s %s, %s".formatted(chunkMiddleX, chunkMiddleZ, player.getUniqueId()));
        armorStand.setGravity(false);
        armorStand.setVisible(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setInvulnerable(true);

        // Set armor stand data
        PersistentDataContainer armorStandPdc = armorStand.getPersistentDataContainer();
        armorStandPdc.set(ClaimsUtils.playerKey, DataType.UUID, player.getUniqueId());
        armorStandPdc.set(ClaimsUtils.claimedInstantKey, PersistentDataType.LONG, System.currentTimeMillis());

        // Set the player's claim amount
        PersistentDataContainer playerPdc = player.getPersistentDataContainer();
        int playerClaimsAmount = playerPdc.get(ClaimsUtils.claimsAmountKey, PersistentDataType.INTEGER);
        int maxClaims = ChunkClaim.getInstance().getConfig().getInt("chunkclaim_max_claims");

        if (playerClaimsAmount + 1 > maxClaims) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    ChunkClaim.getInstance().getConfig().getString("chunkclaim_command_max_claims_exceeded")));

            return true;
        }

        playerPdc.set(ClaimsUtils.claimsAmountKey, PersistentDataType.INTEGER, playerClaimsAmount);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                ChunkClaim.getInstance().getConfig().getString("chunkclaim_command_chunk_claimed")));

        return true;
    }
}
