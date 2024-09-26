package me.bunnky.slimevision.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;

public class VersionHandler {

    private static String minecraftVersion;

    public VersionHandler() {
        minecraftVersion = Bukkit.getVersion();
    }

    public static boolean isVersion(String prefix) {
        return minecraftVersion.startsWith(prefix);
    }


    public Material getTurtleScute() {
        if (!isVersion("1.21")) {
            return Material.valueOf("SCUTE");
        } else {
            return Material.valueOf("TURTLE_SCUTE");
        }
    }

    public Particle getRedstoneParticle() {
        if (!isVersion("1.21")) {
            return getParticle("REDSTONE");
        } else {
            return getParticle("DUST");
        }
    }

    private Particle getParticle(String particleName) {
        return Particle.valueOf(particleName);
    }

}
