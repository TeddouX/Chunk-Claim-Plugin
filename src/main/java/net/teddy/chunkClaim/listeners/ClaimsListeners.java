package net.teddy.chunkClaim.listeners;

import net.teddy.chunkClaim.ChunkClaim;
import net.teddy.chunkClaim.utils.ClaimsUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class ClaimsListeners implements Listener {

    @EventHandler
    public void onBlockBroken(BlockBreakEvent event) {
        Player player = event.getPlayer();

        checkIfClaimed(player, event);
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        checkIfClaimed(player, event);
    }

    void checkIfClaimed(Player player, Cancellable event) {
        UUID claimedUUID = ClaimsUtils.getPlayerClaimed(player.getLocation().getChunk());

        // The chunk isn't claimed
        if (claimedUUID == null)
            return;

        // If the player that claimed the chunk isn't the same as the one that broke the block
        if (!claimedUUID.equals(player.getUniqueId()) && !player.hasPermission("edit_claims"))
            event.setCancelled(true);
    }
}
