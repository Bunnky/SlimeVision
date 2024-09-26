package me.bunnky.slimevision.items;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.bunnky.slimevision.SlimeVision;
import me.bunnky.slimevision.handlers.VersionHandler;
import me.bunnky.slimevision.utility.Utilities;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ParticleItem extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    private final Color particleColor;
    private final VersionHandler versionHandler;

    public ParticleItem(ItemGroup itemGroup,
                        SlimefunItemStack item,
                        RecipeType recipeType,
                        ItemStack[] recipe,
                        Color color
    ) {
        super(itemGroup, item, recipeType, recipe);
        this.particleColor = color;
        this.versionHandler = new VersionHandler();
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return this::onRightClick;
    }

    private void onRightClick(@NotNull PlayerRightClickEvent e) {
        e.cancel();

        Player p = e.getPlayer();
        ItemStack i = p.getInventory().getItemInMainHand();
        ItemStack o = p.getInventory().getItemInOffHand();

        if (Utilities.isParticleItem(i) || Utilities.isParticleItem(o)) {
            Location start = p.getEyeLocation();
            Vector dir = start.getDirection();

            p.getWorld().playSound(start, Sound.ENTITY_ENDER_EYE_DEATH, 1.0f, 1.0f);

            new BukkitRunnable() {
                Location loc = start.clone();
                static final double DISTANCE = 5;
                static final double SPEED = 0.5;
                double traveled = 0;

                @Override
                public void run() {
                    if (traveled >= DISTANCE) {
                        this.cancel();
                        return;
                    }

                    loc.add(dir.clone().multiply(SPEED));

                    p.getWorld().spawnParticle(
                        versionHandler.getRedstoneParticle(),
                        loc,
                        0,
                        new Particle.DustOptions(particleColor, 1)
                    );
                    traveled += SPEED;
                }
            }.runTaskTimer(SlimeVision.getInstance(), 0L, 1L);
        }
    }
}
