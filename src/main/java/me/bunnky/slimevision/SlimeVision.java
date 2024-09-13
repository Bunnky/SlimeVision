package me.bunnky.slimevision;

import fr.skytasul.glowingentities.GlowingBlocks;
import me.bunnky.slimevision.slimefun.Setup;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import javax.annotation.Nonnull;
import java.text.MessageFormat;

public class SlimeVision extends JavaPlugin implements SlimefunAddon {
    private static SlimeVision instance;
    private final String username;
    private final String repo;
    private GlowingBlocks glowingBlocks;

    public SlimeVision() {
        this.username = "Bunnky";
        this.repo = "SlimeVision";
    }

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("    .-=-.    .-=-.     ");
        getLogger().info("   ( 0   )  ( 0   )    ");
        getLogger().info("    `-=-'    `-=-'     ");
        getLogger().info("     Slime Vision      ");
        getLogger().info("       by Bunnky       ");

        saveDefaultConfig();

        glowingBlocks = new GlowingBlocks(this);

        Setup.setup();
        new Metrics(this, 23251);

    }

    public static void consoleMsg(@Nonnull String string) {
        instance.getLogger().info(string);
    }


    public static SlimeVision getInstance() {
        return instance;
    }

    public GlowingBlocks getGlowingBlocks() {
        return glowingBlocks;
    }

    @Override
    public void onDisable() {
        if (glowingBlocks != null) {
            glowingBlocks.disable();
        }
    }

    @Override
    public String getBugTrackerURL() {
        return MessageFormat.format("https://github.com/{0}/{1}/issues", this.username, this.repo);
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
}