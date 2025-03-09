package net.teddy.chunkClaim;

import net.teddy.chunkClaim.commands.ClaimCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChunkClaim extends JavaPlugin {

    private static ChunkClaim instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        getCommand("claim").setExecutor(new ClaimCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ChunkClaim getInstance() {
        return instance;
    }
}
