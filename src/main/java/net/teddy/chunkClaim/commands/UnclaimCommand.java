package net.teddy.chunkClaim.commands;

import net.teddy.chunkClaim.ChunkClaim;
import net.teddy.chunkClaim.utils.ClaimsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UnclaimCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player player))
            return true;

        Chunk chunk = player.getLocation().getChunk();
        UUID claimedUUID = ClaimsUtils.getPlayerClaimed(chunk);

        if (claimedUUID == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    ChunkClaim.getInstance().getConfig().getString("chunkclaim_command_chunk_not_claimed")));

            return true;
        }

        if (!claimedUUID.equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    ChunkClaim.getInstance().getConfig().getString("chunkclaim_command_chunk_isnt_yours")));

            return true;
        }

        Entity[] chunkEntities = chunk.getEntities();
        for (Entity entity : chunkEntities) {
            if (!(entity instanceof ArmorStand armorStand))
                continue;

            if (armorStand.getPersistentDataContainer().has(ClaimsUtils.playerKey))
                entity.remove();
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                ChunkClaim.getInstance().getConfig().getString("chunkclaim_command_chunk_unclaimed")));

        return true;
    }
}
