package me.bunnky.slimevision;

import fr.skytasul.glowingentities.GlowingBlocks;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.BlobBuildUpdater;
import me.bunnky.slimevision.items.slimeeyes.SlimeEye;
import me.bunnky.slimevision.slimefun.Setup;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

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
        tryUpdate();

        glowingBlocks = new GlowingBlocks(this);

        Setup.setup();
        setupMetrics();
    }

    public void setupMetrics (){
        Metrics metrics = new Metrics(this, 23251);

        AdvancedPie playersChart = new AdvancedPie("slimeeye_users", () -> {
            Map<String, Integer> vmap = new HashMap<>();
            SlimeEye.getUsers().forEach(users -> vmap.put(users, 1));
            return vmap;
        });

        AdvancedPie eyeChart = new AdvancedPie("slimeeye_types", SlimeEye::getSlimeEyeUsage);

        metrics.addCustomChart(playersChart);
        metrics.addCustomChart(eyeChart);
    }

    public void tryUpdate() {
        if (getConfig().getBoolean("options.auto-update", true)
            && getDescription().getVersion().startsWith("Dev - ")
        ) {
            new BlobBuildUpdater(this, getFile(), "SlimeVision", "Dev").start();
        }
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