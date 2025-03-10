package net.teddy.chunkClaim.listeners;

import net.teddy.chunkClaim.utils.ClaimsUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class ClaimsListeners implements Listener {

    @EventHandler
    public void onBlockBroken(BlockBreakEvent event) {

        Player player = event.getPlayer();
        UUID claimedUUID = ClaimsUtils.getPlayerClaimed(player.getLocation().getChunk());

        // The chunk isn't claimed
        if (claimedUUID == null)
            return;

        System.out.println("%s %s".formatted(claimedUUID, player.getUniqueId()));

        // If the player that claimed the chunk isn't the same as the one that broke the block
        if (!claimedUUID.equals(player.getUniqueId()))
            event.setCancelled(true);
    }
}
