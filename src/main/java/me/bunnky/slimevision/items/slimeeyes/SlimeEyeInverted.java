package me.bunnky.slimevision.items.slimeeyes;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.bunnky.slimevision.utility.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SlimeEyeInverted extends SlimeEye {

    public SlimeEyeInverted(ItemGroup itemGroup,
                            SlimefunItemStack item,
                            RecipeType recipeType,
                            ItemStack[] recipe,
                            int radius,
                            int cooldown
    ) {
        super(itemGroup, item, recipeType, recipe, radius, cooldown);
    }

    @Override
    public String getTypeName() {
        return "Inverted Slime Eye";
    }

    @Override
    protected void checkBlocks(@NotNull Player p, boolean inverted) {
        UUID pUUID = p.getUniqueId();
        Set<Block> cached = cachedBlocks.get(pUUID);
        World w = p.getWorld();
        Vector loc = Utilities.getPlayerVector(p);

        int pX = loc.getBlockX();
        int pY = loc.getBlockY();
        int pZ = loc.getBlockZ();

        int minX = pX - r;
        int minY = Utilities.getClampedCoordinate(pY, -r, -64, 320);
        int minZ = pZ - r;

        int maxX = pX + r;
        int maxY = Utilities.getClampedCoordinate(pY, r, -64, 320);
        int maxZ = pZ + r;

        Set<Block> currBlocks = new HashSet<>();
        ChatColor currentColor = cIdx == 0 ? ChatColor.RED : cOpts[cIdx - 1];

        for (Block b : cached) {
            if (!isInRange(b, minX, minY, minZ, maxX, maxY, maxZ) || !Utilities.hasBlockStorage(b)) {
                updateGlowingBlock(b, p, currentColor, false);
            }
        }
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = w.getBlockAt(x, y, z);

                    if (!b.getType().isAir()) {
                        continue;
                    }

                    if (cached.contains(b)) {
                        currBlocks.add(b);
                        updateGlowingBlock(b, p, currentColor, true);
                    } else if (Utilities.hasBlockStorage(b)) {
                        cached.add(b);
                        currBlocks.add(b);
                        updateGlowingBlock(b, p, currentColor, true);
                    }
                }
            }
        }
        cached.retainAll(currBlocks);
        sendCountMessage(p, currBlocks.size(), true);
    }
}