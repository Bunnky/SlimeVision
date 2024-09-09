package me.bunnky.slimevision.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.bunnky.slimevision.SlimeVision;
import me.bunnky.slimevision.utility.Utilities;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


import static org.bukkit.Bukkit.getServer;

public class GoldenSlimeFish extends SlimefunItem implements Listener {
    public GoldenSlimeFish(ItemGroup itemGroup,
                           SlimefunItemStack item,
                           RecipeType recipeType,
                           ItemStack[] recipe
    ) {
        super(itemGroup, item, recipeType, recipe);
        this.hidden = true;
        Utilities.setGlow(item);
        getServer().getPluginManager().registerEvents(this, SlimeVision.getInstance());
    }


    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player p = e.getPlayer();
        ItemStack i = p.getInventory().getItemInMainHand();

        if (e.getAction() == Action.LEFT_CLICK_BLOCK && (Utilities.isGSF(i))) {
            Block b = e.getClickedBlock();

            if (b != null && BlockStorage.hasBlockInfo(b)) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        BlockBreakEvent e = new BlockBreakEvent(b, p);
                        Bukkit.getPluginManager().callEvent(e);

                        if (e.isCancelled()) {
                            return;
                        }
                        BlockStorage.clearBlockInfo(b);
                        b.setType(Material.AIR);

                    }
                }.runTaskLater(SlimeVision.getInstance(), 2);
            }
        }
    }
}