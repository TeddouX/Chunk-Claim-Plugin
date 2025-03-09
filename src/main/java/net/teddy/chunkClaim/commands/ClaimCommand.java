package net.teddy.chunkClaim.commands;

import net.teddy.chunkClaim.ChunkClaim;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ClaimCommand implements CommandExecutor {

    public NamespacedKey playerNameKey = new NamespacedKey(ChunkClaim.getInstance(), "player_name");
    public NamespacedKey claimedInstantKey = new NamespacedKey(ChunkClaim.getInstance(), "claimed_instant");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player))
            return true;

        Chunk chunk = player.getLocation().getChunk();
        Entity[] chunkEntities = chunk.getEntities();

        int chunkMiddleX = chunk.getX() * 16 + 8;
        int chunkMiddleZ = chunk.getZ() * 16 + 8;
        int spawnHeight = ChunkClaim.getInstance().getConfig().getInt("chunkclaim_armorstand_spawn_height");

        ArmorStand as = player.getWorld().spawn(new Location(player.getWorld(), chunkMiddleX, spawnHeight, chunkMiddleZ), ArmorStand.class);
        as.setCustomName("%s %s".formatted(chunkMiddleX, chunkMiddleZ));
        as.setGravity(false);
        as.setVisible(false);

        PersistentDataContainer pdc = as.getPersistentDataContainer();
        pdc.set(playerNameKey, PersistentDataType.STRING, player.getName());
        pdc.set(claimedInstantKey, PersistentDataType.LONG, System.currentTimeMillis());

        return true;
    }
}
