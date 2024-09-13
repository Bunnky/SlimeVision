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
        World world = p.getWorld();
        Vector playerLocation = p.getLocation().toVector();

        int minX = playerLocation.getBlockX() - 10;
        int minY = Math.max(playerLocation.getBlockY() - 10, world.getMinHeight());
        int minZ = playerLocation.getBlockZ() - 10;
        int maxX = playerLocation.getBlockX() + 10;
        int maxY = Math.min(playerLocation.getBlockY() + 10, world.getMaxHeight());
        int maxZ = playerLocation.getBlockZ() + 10;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);

                    if (BlockStorage.hasBlockInfo(block)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                BlockBreakEvent breakEvent = new BlockBreakEvent(block, p);
                                getPluginManager().callEvent(breakEvent);

                                if (!breakEvent.isCancelled()) {
                                    BlockStorage.clearBlockInfo(block);
                                    block.setType(Material.AIR);
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
