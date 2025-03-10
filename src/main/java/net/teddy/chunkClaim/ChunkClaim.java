package net.teddy.chunkClaim;

import net.teddy.chunkClaim.commands.ClaimCommand;
import net.teddy.chunkClaim.commands.ClaimInfoCommand;
import net.teddy.chunkClaim.commands.UnclaimCommand;
import net.teddy.chunkClaim.listeners.ClaimsListeners;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChunkClaim extends JavaPlugin {

    private static ChunkClaim instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        getCommand("claim").setExecutor(new ClaimCommand());
        getCommand("unclaim").setExecutor(new UnclaimCommand());
        getCommand("claiminfo").setExecutor(new ClaimInfoCommand());

        getServer().getPluginManager().registerEvents(new ClaimsListeners(), instance);

    }

    public static ChunkClaim getInstance() {
        return instance;
    }
}
