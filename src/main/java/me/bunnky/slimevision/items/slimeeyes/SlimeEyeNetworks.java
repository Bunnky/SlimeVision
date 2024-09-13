package me.bunnky.slimevision.items.slimeeyes;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.bunnky.slimevision.utility.Utilities;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
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
        ChatColor currentColor = cIdx == 0 ? ChatColor.DARK_PURPLE : cOpts[cIdx - 1];

        for (Block b : cached) {
            if (!super.isInRange(b, minX, minY, minZ, maxX, maxY, maxZ)) {
                updateGlowingBlock(b, p, currentColor, false);
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = w.getBlockAt(x, y, z);

                    if (Utilities.isAirOrLiquid(b)) {
                        continue;
                    }

                    if (cached.contains(b) || (Utilities.hasBlockStorage(b) && "Networks".equals(BlockStorage.check(b).getAddon().getName()))) {
                        cached.add(b);
                        currBlocks.add(b);
                        updateGlowingBlock(b, p, currentColor, true);

                        if (!cached.contains(b)) {
                            cached.add(b);
                        }
                    }
                }
            }
        }
        cached.retainAll(currBlocks);
        sendCountMessage(p, cached.size(), false);
    }

    @Override
    protected void sendCountMessage(Player p, int total, boolean inverted) {
        UUID pUUID = p.getUniqueId();

        int currentMsgCd = msgCds.getOrDefault(pUUID, 0);
        int currentPrevCount = prev.getOrDefault(pUUID, 0);

        if (currentMsgCd > 0 && total == currentPrevCount) {
            msgCds.put(pUUID, currentMsgCd - 1);
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("§nIn Range:\n");
        message.append("§5Networks: §e").append(total).append("\n");
        message.append("§f┈┈┈┈┈┈┈┈┈┈┈┈┈┈\n");

        p.sendMessage(message.toString());

        msgCds.put(pUUID, 5);
        prev.put(pUUID, total);
    }
}