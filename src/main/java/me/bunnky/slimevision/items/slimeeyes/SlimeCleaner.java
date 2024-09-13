package me.bunnky.slimevision.items.slimeeyes;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.bunnky.slimevision.SlimeVision;
import me.bunnky.slimevision.utility.Utilities;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;


import static org.bukkit.Bukkit.getPluginManager;

public class SlimeCleaner extends SlimeEye {

    public SlimeCleaner(ItemGroup itemGroup,
                        SlimefunItemStack item,
                        RecipeType recipeType,
                        ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 10, 0);
        Utilities.setGlow(item);
    }

    @Override
    protected void onRightClick(@NotNull PlayerRightClickEvent e) {
        e.cancel();

        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player p = e.getPlayer();
        if (!p.isOp()) {
            return;
        }

        removeBlocksInRadius(p);
    }

    protected void removeBlocksInRadius(Player p) {
        World w = p.getWorld();
        Vector loc = p.getLocation().toVector();

        int minX = loc.getBlockX() - 10;
        int minY = Math.max(loc.getBlockY() - 10, w.getMinHeight());
        int minZ = loc.getBlockZ() - 10;
        int maxX = loc.getBlockX() + 10;
        int maxY = Math.min(loc.getBlockY() + 10, w.getMaxHeight());
        int maxZ = loc.getBlockZ() + 10;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = w.getBlockAt(x, y, z);

                    if (BlockStorage.hasBlockInfo(b)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                BlockBreakEvent e = new BlockBreakEvent(b, p);
                                getPluginManager().callEvent(e);

                                if (!e.isCancelled()) {
                                    BlockStorage.clearBlockInfo(b);
                                    b.setType(Material.AIR);
                                }
                            }
                        }.runTaskLater(SlimeVision.getInstance(), 1);
                    }
                }
            }
        }
    }


    @Override
    protected void checkBlocks(@NotNull Player p, boolean inverted) {
        //lazy
    }
}
