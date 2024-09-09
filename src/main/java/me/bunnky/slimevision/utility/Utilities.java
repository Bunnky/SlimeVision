package me.bunnky.slimevision.utility;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import me.bunnky.slimevision.items.GoldenSlimeFish;
import me.bunnky.slimevision.items.ParticleItem;
import me.bunnky.slimevision.items.slimeeyes.SlimeEye;
import me.bunnky.slimevision.items.slimeeyes.SlimeEyeGod;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Utilities {

    private static final float PARTICLE_SIZE = 2;

    public static void setGlow(@NotNull SlimefunItemStack i) {
        ItemMeta m = i.getItemMeta();
        if (m != null) {
            m.addEnchant(Enchantment.LURE, 1, true);
            m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            i.setItemMeta(m);
        }
    }

    private static final Vector[] OFFSETS = {
        new Vector(0.5, 1, 0.5),    // Top
        new Vector(0.5, 0, 0.5),    // Bottom
        new Vector(1, 0.5, 0.5),    // East
        new Vector(0, 0.5, 0.5),    // West
        new Vector(0.5, 0.5, 1),    // South
        new Vector(0.5, 0.5, 0)     // North
    };

    public static void particlesVanilla(Player p, Vector blockLocation) {
        Color[] c = {
            Color.RED,
            Color.AQUA,
            Color.YELLOW,
            Color.GREEN,
            Color.ORANGE,
            Color.PURPLE
        };
        spawnParticles(p, blockLocation, c);
    }

    public static void particlesNetworks(Player p, Vector blockLocation) {
        Color[] c = {
            Color.FUCHSIA,
            Color.FUCHSIA,
            Color.PURPLE,
            Color.PURPLE,
            Color.PURPLE,
            Color.PURPLE
        };
        spawnParticles(p, blockLocation, c);
    }

    public static void particlesInverted(Player p, Vector blockLocation) {
        Color[] c = {
            Color.RED,
            Color.AQUA,
            Color.YELLOW,
            Color.GREEN,
            Color.ORANGE,
            Color.PURPLE
        };
        spawnInvertedParticle(p, blockLocation, c[0]);
    }

    public static void particlesInverted(Player p, Vector loc, Color c) {
        spawnInvertedParticle(p, loc, c);
    }

    public static @NotNull String getColorName(@NotNull Color c) {
        if (c.equals(Color.RED)) {
            return "Red";
        } else if (c.equals(Color.AQUA)) {
            return "Aqua";
        } else if (c.equals(Color.YELLOW)) {
            return "Yellow";
        } else if (c.equals(Color.GREEN)) {
            return "Green";
        } else if (c.equals(Color.ORANGE)) {
            return "Orange";
        } else if (c.equals(Color.PURPLE)) {
            return "Purple";
        } else if (c.equals(Color.BLUE)) {
            return "Blue";
        } else if (c.equals(Color.BLACK)) {
            return "Black";
        } else if (c.equals(Color.WHITE)) {
            return "White";
        } else if (c.equals(Color.GRAY)) {
            return "Gray";
        } else if (c.equals(Color.LIME)) {
            return "Lime";
        } else if (c.equals(Color.FUCHSIA)) {
            return "Pink";
        } else if (c.equals(Color.MAROON)) {
            return "Maroon";
        } else if (c.equals(Color.TEAL)) {
            return "Light Blue";
        } else {
            return "Unknown";
        }
    }


    public static void particlesSingleColor(Player p, Vector loc, Color c) {
        Color[] colors = {c, c, c, c, c, c};
        spawnParticles(p, loc, colors);
    }

    private static void spawnParticles(Player p, Vector loc, Color[] colors) {
        for (int i = 0; i < OFFSETS.length; i++) {
            p.spawnParticle(
                Particle.REDSTONE, loc.clone().add(OFFSETS[i]).toLocation(p.getWorld()),
                1, 0, 0, 0, 1, new Particle.DustOptions(colors[i], PARTICLE_SIZE)
            );
        }
    }

    private static void spawnInvertedParticle(Player p, Vector loc, Color color) {
        loc = loc.clone().add(new Vector(0, 0, 0));

        p.spawnParticle(
            Particle.REDSTONE, loc.toLocation(p.getWorld()),
            1, 0, 0, 0, 1, new Particle.DustOptions(color, 4)
        );
    }

    public static void placeBlock(@NotNull Block block, @NotNull BlockFace face, @NotNull Material material) {
        Block placeBlock = block.getRelative(face);
        placeBlock.setType(material);
    }

    public static int sendCountMessage(Player p, int count, String messageType, int msgCooldown, int prevCount) {
        if (msgCooldown <= 0 || count != prevCount) {
            p.sendMessage("§e" + count + " §a" + messageType + " in range");
            msgCooldown = 5;
        } else {
            msgCooldown--;
        }
        return msgCooldown;
    }

    public static int sendNullCountMessage(Player p, List<String> nullMachines, int msgCooldown, int prevCount) {
        if (msgCooldown <= 0 || nullMachines.size() != prevCount) {
            if (!nullMachines.isEmpty()) {
                StringBuilder message = new StringBuilder("§cCleared Null Machines:\n");
                for (String coords : nullMachines) {
                    message.append("§7").append(coords).append("\n");
                }
                p.sendMessage(message.toString());
            }
            msgCooldown = 5;
        } else {
            msgCooldown--;
        }
        return msgCooldown;
    }

    public static boolean isGSF(ItemStack i) {
        SlimefunItem sfItem = SlimefunItem.getByItem(i);
        return sfItem instanceof GoldenSlimeFish;
    }

    public static boolean isSlimeEye(ItemStack i) {
        SlimefunItem sfItem = SlimefunItem.getByItem(i);
        return sfItem instanceof SlimeEye;
    }

    public static boolean isSlimeEyeGod(ItemStack i) {
        SlimefunItem sfItem = SlimefunItem.getByItem(i);
        return sfItem instanceof SlimeEyeGod;
    }

    public static boolean isParticleItem(ItemStack i) {
        SlimefunItem sfItem = SlimefunItem.getByItem(i);
        return sfItem instanceof ParticleItem;
    }

    public static Vector getPlayerVector(Player player) {
        return player.getLocation().toVector();
    }

    public static boolean isAirOrLiquid(Block block) {
        return block.getType().isAir() || block.isLiquid();
    }

    public static boolean hasBlockStorage(Block block) {
        return BlockStorage.hasBlockInfo(block);
    }

    public static int getClampedCoordinate(int coordinate, int radius, int min, int max) {
        return Math.min(max, Math.max(min, coordinate + radius));
    }
}
