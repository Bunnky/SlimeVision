package me.bunnky.slimevision.items;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.bunnky.slimevision.SlimeVision;
import me.bunnky.slimevision.utility.Utilities;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.bukkit.Bukkit.getServer;

public class SFS extends SimpleSlimefunItem<ItemUseHandler> {

    private final Plugin slimefun;
    private Field asField;
    private Method savePlayers;
    private Method saveBlocks;

    public SFS(ItemGroup itemGroup,
               SlimefunItemStack item,
               RecipeType recipeType,
               ItemStack[] recipe
    ) {
        super(itemGroup, item, recipeType, recipe);
        this.hidden = true;
        Utilities.setGlow(item);
        this.slimefun = getServer().getPluginManager().getPlugin("Slimefun");
        init();
    }

    private void init() {
        if (slimefun != null) {
            try {
                asField = slimefun.getClass().getDeclaredField("autoSavingService");
                asField.setAccessible(true);

                Object autoSavingServiceInstance = asField.get(slimefun);
                Class<?> autoSavingServiceClass = autoSavingServiceInstance.getClass();

                savePlayers = autoSavingServiceClass.getDeclaredMethod("saveAllPlayers");
                savePlayers.setAccessible(true);

                saveBlocks = autoSavingServiceClass.getDeclaredMethod("saveAllBlocks");
                saveBlocks.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return this::onRightClick;
    }

    protected void onRightClick(@NotNull PlayerRightClickEvent e) {
        e.cancel();
        Player p = e.getPlayer();

        if (!p.isOp()) {
            return;
        }

        BlockStorage.saveChunks();
        save(p);
    }

    private void save(@NotNull Player p) {
        p.sendMessage("§e[SlimeVision] Saving Slimefun players and blocks...");
        SlimeVision.consoleMsg("MANUAL SAVE INITIATED");

        try {
            if (slimefun != null && asField != null) {
                Object autoSavingServiceInstance = asField.get(slimefun);
                if (savePlayers != null) {
                    savePlayers.invoke(autoSavingServiceInstance);
                }
                if (saveBlocks != null) {
                    saveBlocks.invoke(autoSavingServiceInstance);
                }
                p.sendMessage("§a§l[SlimeVision] Slimefun Saved!");
                SlimeVision.consoleMsg("MANUAL SAVE FINISHED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            p.sendMessage("§c[SlimeVision] An error occurred while saving.");
        }
    }
}