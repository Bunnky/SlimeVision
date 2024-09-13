package me.bunnky.slimevision.items.slimeeyes;

import fr.skytasul.glowingentities.GlowingBlocks;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.bunnky.slimevision.SlimeVision;
import me.bunnky.slimevision.utility.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class SlimeEye extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable, Listener {

    protected final int r;
    protected final int cd;
    protected int cIdx = 0;
    protected final Map<UUID, Integer> msgCds = new HashMap<>();
    protected final Map<UUID, Integer> prev = new HashMap<>();
    protected final Map<UUID, Set<Block>> cachedBlocks = new HashMap<>();
    protected static final Map<UUID, BukkitRunnable> activeTasks = new HashMap<>();
    protected static final Map<UUID, Long> lastActionTime = new HashMap<>();

    protected static final ChatColor[] cOpts = {ChatColor.RED, ChatColor.DARK_RED, ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE, ChatColor.DARK_BLUE, ChatColor.BLUE, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.BLACK, ChatColor.WHITE};

    public SlimeEye(ItemGroup itemGroup,
                    SlimefunItemStack item,
                    RecipeType recipeType,
                    ItemStack[] recipe,
                    int radius,
                    int cooldown
    ) {
        super(itemGroup, item, recipeType, recipe);
        this.r = radius;
        this.cd = cooldown;
        getServer().getPluginManager().registerEvents(this, SlimeVision.getInstance());
        getServer().getScheduler().runTaskTimer(SlimeVision.getInstance(), this::cleanCooldowns, 18000L, 18000L);
    }

    protected int getCooldown() {
        return cd;
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return this::onRightClick;
    }

    protected void onRightClick(@NotNull PlayerRightClickEvent e) {
        e.cancel();

        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player p = e.getPlayer();
        UUID pUUID = p.getUniqueId();
        cachedBlocks.putIfAbsent(pUUID, new HashSet<>());

        if (p.isSneaking()) {
            cycleColor(p);
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (lastActionTime.containsKey(pUUID) &&
            (currentTime - lastActionTime.get(pUUID)) < getCooldown()) {
            long remainingTime = (getCooldown() - (currentTime - lastActionTime.get(pUUID))) / 1000;
            p.sendMessage("§cYou must wait " + remainingTime + " seconds.");
            return;
        }

        lastActionTime.put(pUUID, currentTime);
        cachedBlocks.remove(pUUID,cachedBlocks);
        startHighlight(p);
    }

    protected void cleanCooldowns() {
        long now = System.currentTimeMillis();
        lastActionTime.entrySet().removeIf(entry -> (now - entry.getValue()) > cd);
    }

    protected void cycleColor(@NotNull Player p) {
        cIdx = (cIdx + 1) % (cOpts.length + 1);
        String colorName = cIdx == 0 ? "§eDefault" : Utilities.getColorName(cOpts[cIdx - 1]);
        p.sendMessage("§aGlow color set to: §e" + colorName);
    }

    protected void startHighlight(@NotNull Player p) {
        UUID pUUID = p.getUniqueId();
        if (activeTasks.containsKey(pUUID)) {
            return;
        }
        p.sendMessage("§6Slime Gaze enabled.");

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                checkBlocks(p, false);
            }
        };
        task.runTaskTimer(SlimeVision.getInstance(), 0L, 20L);
        activeTasks.put(pUUID, task);

        getServer().getScheduler().runTaskLater(SlimeVision.getInstance(), () -> {
            cancelHighlight(pUUID);
            p.sendMessage("§cSlime Gaze disabled.");
        }, 10 * 20L);
    }

    protected void cancelHighlight(UUID pUUID) {
        BukkitRunnable task = activeTasks.remove(pUUID);
        if (task != null) {
            task.cancel();
        }

        Player p = getServer().getPlayer(pUUID);
        if (p != null) {
            for (Block b : cachedBlocks.get(pUUID)) {
                updateGlowingBlock(b, p, null, false);
            }
            cachedBlocks.remove(pUUID, cachedBlocks);
        }
    }


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

        ChatColor currentColor = cIdx == 0 ? ChatColor.GREEN : cOpts[cIdx - 1];

        for (Block b : cached) {
            if (!isInRange(b, minX, minY, minZ, maxX, maxY, maxZ) || !Utilities.hasBlockStorage(b) || b.getType()
                .isAir()) {
                updateGlowingBlock(b, p, currentColor, false);
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {

                    Block b = w.getBlockAt(x, y, z);
                    boolean isAirOrLiquid = Utilities.isAirOrLiquid(b);

                    if ((inverted && !isAirOrLiquid) || (!inverted && isAirOrLiquid)) {
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
        sendCountMessage(p, cached.size(), inverted);
    }

    protected void updateGlowingBlock(Block b, Player p, ChatColor c, boolean glow) {
        GlowingBlocks glowingBlocks = SlimeVision.getInstance().getGlowingBlocks();
        try {
            if (glow) {
                glowingBlocks.setGlowing(b, p, c);
            } else {
                glowingBlocks.unsetGlowing(b, p);
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    protected boolean isInRange(Block b, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        int bX = b.getX();
        int bY = b.getY();
        int bZ = b.getZ();
        return (bX >= minX && bX <= maxX) && (bY >= minY && bY <= maxY) && (bZ >= minZ && bZ <= maxZ);
    }

    protected void sendCountMessage(Player p, int total, boolean inverted) {
        UUID pUUID = p.getUniqueId();

        int currCd = msgCds.getOrDefault(pUUID, 0);
        int prevCnt = prev.getOrDefault(pUUID, 0);

        if (currCd > 0 && total == prevCnt) {
            msgCds.put(pUUID, currCd - 1);
            return;
        }

        StringBuilder m = new StringBuilder();
        m.append("§nIn Range:\n");

        if (inverted) {
            m.append("§7Invisible: §e").append(total).append("\n");
        } else {
            m.append("§aValid: §e").append(total).append("\n");
        }
        m.append("§f┈┈┈┈┈┈┈┈┈┈┈┈┈┈\n");

        p.sendMessage(m.toString());

        msgCds.put(pUUID, 5);
        prev.put(pUUID, total);
    }

    @EventHandler
    protected void onPlayerQuit(PlayerQuitEvent e) {
        UUID pUUID = e.getPlayer().getUniqueId();

        if (activeTasks.containsKey(pUUID)) {
            cancelHighlight(pUUID);
            cachedBlocks.remove(pUUID, cachedBlocks);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        updateGlowingBlock(b, p, null, false);
    }
}