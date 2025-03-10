package net.teddy.chunkClaim.commands;

import com.jeff_media.morepersistentdatatypes.DataType;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClaimInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player))
            return true;

        Chunk chunk = player.getLocation().getChunk();

        if (!ClaimsUtils.isChunkClaimed(chunk)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    ChunkClaim.getInstance().getConfig().getString("chunkclaim_command_chunk_not_claimed")));

            return true;
        }

        Entity[] chunkEntities = chunk.getEntities();
        StringBuilder sb = new StringBuilder();
        for (Entity entity : chunkEntities) {
            if (!(entity instanceof ArmorStand armorStand))
                continue;

            PersistentDataContainer pdc = armorStand.getPersistentDataContainer();
            if (pdc.has(ClaimsUtils.playerKey)) {
                long claimedMilliseconds = pdc.get(ClaimsUtils.claimedInstantKey, PersistentDataType.LONG);
                long elapsed = System.currentTimeMillis() - claimedMilliseconds;

                long days = TimeUnit.MILLISECONDS.toDays(elapsed);
                elapsed -= TimeUnit.DAYS.toMillis(days);
                long hours = TimeUnit.MILLISECONDS.toHours(elapsed);
                elapsed -= TimeUnit.HOURS.toMillis(hours);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed);
                elapsed -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed);

                UUID playerUUID = pdc.get(ClaimsUtils.playerKey, DataType.UUID);
                String playerName = ChunkClaim.getInstance().getServer().getPlayer(playerUUID).getDisplayName();

                System.out.println(seconds);

                sb.append("------------------------------------------------").append("\n")
                        .append(ChatColor.GREEN).append("Chunk claim time: ")
                        .append(ChatColor.WHITE)
                        .append(days).append("d ")
                        .append(hours).append(":")
                        .append(minutes).append(":")
                        .append(seconds).append("\n")
                        .append(ChatColor.GREEN).append("Claimed by: ")
                        .append(ChatColor.YELLOW).append(playerName).append("\n")
                        .append(ChatColor.WHITE)
                        .append("------------------------------------------------");
            }
        }

        player.sendMessage(sb.toString());

        return true;
    }
}
