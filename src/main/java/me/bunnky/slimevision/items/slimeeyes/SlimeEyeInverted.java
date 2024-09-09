package me.bunnky.slimevision.items.slimeeyes;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.bunnky.slimevision.utility.Utilities;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

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
    protected void checkBlocks(@NotNull Player p, boolean inverted) {
        super.checkBlocks(p, true);
    }

    @Override
    protected void addParticle(Player p, Block b) {
        Vector loc = b.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5));

        if (colorIdx == 0) {
            Utilities.particlesInverted(p, loc);
        } else {
            Utilities.particlesInverted(p, loc, colorOpts[colorIdx - 1]);
        }
    }
}