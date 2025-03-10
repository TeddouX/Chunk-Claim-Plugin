package net.teddy.chunkClaim.utils;

import com.jeff_media.morepersistentdatatypes.DataType;
import net.teddy.chunkClaim.ChunkClaim;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ClaimsUtils {

    public static NamespacedKey playerKey = new NamespacedKey(ChunkClaim.getInstance(), "player");
    public static NamespacedKey claimedInstantKey = new NamespacedKey(ChunkClaim.getInstance(), "claimed_instant");

    public static boolean isChunkClaimed(Chunk chunk) {

        Entity[] chunkEntities = chunk.getEntities();

        for (Entity entity : chunkEntities) {
            if (!(entity instanceof ArmorStand armorStand))
                continue;

            if (armorStand.getPersistentDataContainer().has(playerKey))
                return true;
        }

        return false;
    }

    @Nullable
    public static UUID getPlayerClaimed(Chunk chunk) {
        Entity[] chunkEntities = chunk.getEntities();

        for (Entity entity : chunkEntities) {
            if (!(entity instanceof ArmorStand armorStand))
                continue;

            PersistentDataContainer pdc = armorStand.getPersistentDataContainer();
            if (pdc.has(playerKey))
                return pdc.get(playerKey, DataType.UUID);
        }

        return null;
    }
}
