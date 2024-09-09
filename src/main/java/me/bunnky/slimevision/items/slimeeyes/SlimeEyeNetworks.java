package me.bunnky.slimevision.items.slimeeyes;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.bunnky.slimevision.utility.Utilities;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class SlimeEyeNetworks extends SlimeEye {

    public SlimeEyeNetworks(ItemGroup itemGroup,
                            SlimefunItemStack item,
                            RecipeType recipeType,
                            ItemStack[] recipe,
                            int radius,
                            int cooldown
    ) {
        super(itemGroup, item, recipeType, recipe, radius, cooldown);
        Utilities.setGlow(item);
    }

    @Override
    protected void startHighlight(@NotNull Player p) {
        super.startHighlight(p);
        checkBlocks(p, false);
    }

    @Override
    protected void checkBlocks(@NotNull Player p, boolean inverted) {
        World world = p.getWorld();
        Vector playerLocation = Utilities.getPlayerVector(p);
        int pX = playerLocation.getBlockX();
        int pY = playerLocation.getBlockY();
        int pZ = playerLocation.getBlockZ();

        int minX = pX - r;
        int minY = Utilities.getClampedCoordinate(pY, -r, -64, 320);
        int minZ = pZ - r;

        int maxX = pX + r;
        int maxY = Utilities.getClampedCoordinate(pY, r, -64, 320);
        int maxZ = pZ + r;

        Set<Block> currBlocks = new HashSet<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = world.getBlockAt(x, y, z);

                    if (Utilities.isAirOrLiquid(b)) {
                        continue;
                    }

                    if (cached.contains(b)) {
                        currBlocks.add(b);
                        addParticle(p, b);
                        continue;
                    }

                    if (Utilities.hasBlockStorage(b) && "Networks".equals(BlockStorage.check(b).getAddon().getName())) {
                        cached.add(b);
                        currBlocks.add(b);
                        addParticle(p, b);
                    }
                }
            }
        }
        cached.retainAll(currBlocks);
        sendCountMessage(p, cached.size(), false);
    }

    @Override
    protected void addParticle(Player p, Block b) {
        if (colorIdx == 0) {
            Utilities.particlesNetworks(p, b.getLocation().toVector());
        } else {
            Utilities.particlesSingleColor(p, b.getLocation().toVector(), colorOpts[colorIdx - 1]);
        }
    }
}