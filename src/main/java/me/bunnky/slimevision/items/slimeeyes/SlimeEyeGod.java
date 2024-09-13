package me.bunnky.slimevision.items.slimeeyes;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.bunnky.slimevision.SlimeVision;
import me.bunnky.slimevision.utility.Utilities;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class SlimeEyeGod extends SlimeEye {

    private final Map<UUID, Boolean> toggledOn = new HashMap<>();
    private final Map<UUID, Boolean> invisibleToggledOn = new HashMap<>();
    private final Map<UUID, Long> lastMessageSentMap = new HashMap<>();
    private final Map<UUID, Integer> lastNetworkCountMap = new HashMap<>();
    private final Map<UUID, Integer> lastTotalCountMap = new HashMap<>();
    private final Map<UUID, Integer> lastAirCountMap = new HashMap<>();
    private final Map<UUID, BukkitRunnable> activeTasks = new HashMap<>();

    public SlimeEyeGod(ItemGroup itemGroup,
                       SlimefunItemStack item,
                       RecipeType recipeType,
                       ItemStack[] recipe,
                       int radius
    ) {
        super(itemGroup, item, recipeType, recipe, radius, 0);
        hidden = true;
        Utilities.setGlow(item);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return this::onRightClick;
    }

    @EventHandler
    protected void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!(Utilities.isSlimeEyeGod(e.getItem()))) {
            return;
        }
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block b = e.getClickedBlock();
        if (b == null) {
            return;
        }

        e.setCancelled(true);

        Player p = e.getPlayer();
        if (!p.isOp()) {
            return;
        }

        if (p.isSneaking()) {
            placeBlock(b, e.getBlockFace());
        } else {
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
            }.runTaskLater(SlimeVision.getInstance(), 1);
        }
    }

    private void placeBlock(Block b, BlockFace f) {
        Utilities.placeBlock(b, f, Material.TARGET);
    }

    @Override
    protected void onRightClick(@NotNull PlayerRightClickEvent e) {
        e.cancel();
        Player p = e.getPlayer();

        if (!p.isOp()) {
            return;
        }

        UUID pUUID = p.getUniqueId();

        toggledOn.putIfAbsent(pUUID, false);
        invisibleToggledOn.putIfAbsent(pUUID, false);
        cachedBlocks.putIfAbsent(pUUID, new HashSet<>());

        Set<Block> playerCachedBlocks = cachedBlocks.get(pUUID);

        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            if (!playerCachedBlocks.isEmpty()) {
                playerCachedBlocks.clear();
            }

            checkInvisibleBlocks(p);

            if (!playerCachedBlocks.isEmpty()) {
                placeBlocksAtInvisibleLocations(pUUID);
                p.sendMessage("§6Blocks placed at invisible machine locations.");
            } else {
                p.sendMessage("§cNo invisible machines to place blocks at.");
            }
            return;
        }

        boolean isInvertedGazeOn = invisibleToggledOn.getOrDefault(pUUID, false);

        if (p.isSneaking()) {
            if (isInvertedGazeOn) {
                cancelHighlightTask(pUUID);
                playerCachedBlocks.clear();
                invisibleToggledOn.put(pUUID, false);
                p.sendMessage("§cInverted Slime Gaze disabled.");
            } else {
                cancelHighlightTask(pUUID);
                playerCachedBlocks.clear();
                toggledOn.put(pUUID, false);
                invisibleToggledOn.put(pUUID, true);
                startHighlightTask(p, true);
                p.sendMessage("§6Inverted Slime Gaze enabled.");
            }
            return;
        }

        if (isInvertedGazeOn) {
            cancelHighlightTask(pUUID);
            playerCachedBlocks.clear();
            invisibleToggledOn.put(pUUID, false);  // Disable inverted gaze
            p.sendMessage("§cInverted Slime Gaze disabled.");
        }

        boolean currentState = toggledOn.getOrDefault(pUUID, false);
        toggledOn.put(pUUID, !currentState);

        if (toggledOn.get(pUUID)) {
            playerCachedBlocks.clear();
            startHighlightTask(p, false);
            p.sendMessage("§6Slime Gaze enabled.");
        } else {
            cancelHighlightTask(pUUID);
            playerCachedBlocks.clear();
            p.sendMessage("§cSlime Gaze disabled.");
        }
    }

    protected void startHighlightTask(@NotNull Player p, boolean isInvisible) {
        UUID pUUID = p.getUniqueId();

        boolean currentToggle = isInvisible ? invisibleToggledOn.getOrDefault(pUUID, false) : toggledOn.getOrDefault(pUUID, false);

        if (activeTasks.containsKey(pUUID) || !currentToggle) {
            return;
        }

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (isInvisible) {
                    checkInvisibleBlocks(p);
                } else {
                    checkBlocks(p);
                }
            }
        };

        task.runTaskTimer(SlimeVision.getInstance(), 0L, 20L);
        activeTasks.put(pUUID, task);

    }

    protected void cancelHighlightTask(UUID pUUID) {
        BukkitRunnable task = activeTasks.remove(pUUID);
        if (task != null) {
            task.cancel();
        }

        Player p = getServer().getPlayer(pUUID);
        if (p != null) {
            Set<Block> blocksToUnset = cachedBlocks.get(pUUID);
            if (blocksToUnset != null) {
                for (Block b : blocksToUnset) {
                    updateGlowingBlock(b, p, null, false);
                }
            }
        }
        cachedBlocks.remove(pUUID, cachedBlocks);
    }

    protected void checkInvisibleBlocks(@NotNull Player p) {
        checkBlocks(p, true);
    }

    protected void checkBlocks(@NotNull Player p) {
        checkBlocks(p, false);
    }

    private void placeBlocksAtInvisibleLocations(UUID pUUID) {
        for (Block iBlock : cachedBlocks.get(pUUID)) {
            if (Utilities.isAirOrLiquid(iBlock)) {
                Utilities.placeBlock(iBlock, BlockFace.SELF, Material.TARGET);
            }
        }
    }

    @Override
    protected void checkBlocks(@NotNull Player p, boolean checkInvisible) {
        World w = p.getWorld();
        Vector loc = Utilities.getPlayerVector(p);

        int pX = loc.getBlockX(), pY = loc.getBlockY(), pZ = loc.getBlockZ();
        int minX = pX - r, minY = Utilities.getClampedCoordinate(pY, -r, -64, 320), minZ = pZ - r;
        int maxX = pX + r, maxY = Utilities.getClampedCoordinate(pY, r, -64, 320), maxZ = pZ + r;

        Set<Block> networkBlocks = new HashSet<>();
        Set<Block> airBlocks = new HashSet<>();
        List<String> nullMachines = new ArrayList<>();

        UUID pUUID = p.getUniqueId();

        Set<Block> playerCachedBlocks = cachedBlocks.computeIfAbsent(pUUID, k -> new HashSet<>());

        Iterator<Block> iterator = playerCachedBlocks.iterator();
        while (iterator.hasNext()) {
            Block b = iterator.next();
            if (!isInRange(b, minX, minY, minZ, maxX, maxY, maxZ)) {
                updateGlowingBlock(b, p, null, false);
                iterator.remove();
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = w.getBlockAt(x, y, z);

                    if (Utilities.hasBlockStorage(b)) {
                        SlimefunItem sfItem = BlockStorage.check(b);
                        if (sfItem != null) {
                            if (checkInvisible) {
                                if (Utilities.isAirOrLiquid(b)) {
                                    airBlocks.add(b);
                                    playerCachedBlocks.add(b);
                                    updateGlowingBlock(b, p, ChatColor.RED, true);
                                }
                            } else {
                                if ("Networks".equals(sfItem.getAddon().getName()) && !Utilities.isAirOrLiquid(b)) {
                                    networkBlocks.add(b);
                                    playerCachedBlocks.add(b);
                                    updateGlowingBlock(b, p, ChatColor.LIGHT_PURPLE, true);
                                } else if (Utilities.isAirOrLiquid(b)) {
                                    airBlocks.add(b);
                                    playerCachedBlocks.add(b);
                                    updateGlowingBlock(b, p, ChatColor.RED, true);
                                } else {
                                    playerCachedBlocks.add(b);
                                    updateGlowingBlock(b, p, ChatColor.GREEN, true);
                                }
                            }
                        } else {
                            playerCachedBlocks.add(b);
                            updateGlowingBlock(b, p, ChatColor.BLACK, true);
                            nullMachines.add(b.getX() + ", " + b.getY() + ", " + b.getZ());
                            BlockStorage.clearBlockInfo(b);
                        }
                    }
                }
            }
        }

        long currentTime = System.currentTimeMillis();
        long lastSent = lastMessageSentMap.getOrDefault(pUUID, 0L);
        int lastNetworkCount = lastNetworkCountMap.getOrDefault(pUUID, 0);
        int lastTotalCount = lastTotalCountMap.getOrDefault(pUUID, 0);
        int lastAirCount = lastAirCountMap.getOrDefault(pUUID, 0);

        boolean newMachinesFound = networkBlocks.size() != lastNetworkCount ||
            playerCachedBlocks.size() != lastTotalCount ||
            airBlocks.size() != lastAirCount;

        if (currentTime - lastSent >= 5000 || newMachinesFound) {
            sendCountMessage(p, networkBlocks.size(), playerCachedBlocks.size(), airBlocks.size(), nullMachines);

            lastMessageSentMap.put(pUUID, currentTime);
            lastNetworkCountMap.put(pUUID, networkBlocks.size());
            lastTotalCountMap.put(pUUID, playerCachedBlocks.size());
            lastAirCountMap.put(pUUID, airBlocks.size());
        }
    }


    private void sendCountMessage(Player p, int networkCount, int totalCount, int airCount, List<String> nullMachines) {
        UUID pUUID = p.getUniqueId();
        boolean isInvisibleToggled = invisibleToggledOn.getOrDefault(pUUID, false);

        StringBuilder m = new StringBuilder();
        m.append("§nIn Range:\n");

        if (isInvisibleToggled) {
            m.append("§7Invisible: §e").append(airCount).append("\n");
        } else {
            m.append("§5Networks: §e").append(networkCount).append("\n");
            m.append("§7Invisible: §e").append(airCount).append("\n");
            m.append("§aValid: §e").append(totalCount).append("\n");

            if (!nullMachines.isEmpty()) {
                m.append("§cNull:\n");
                for (String machine : nullMachines) {
                    m.append("§7").append(machine).append("\n");
                }
            }
        }
        m.append("§f┈┈┈┈┈┈┈┈┈┈┈┈┈┈\n");
        p.sendMessage(m.toString());
    }

    @EventHandler
    protected void onItemHeld(@NotNull PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack newItem = p.getInventory().getItem(e.getNewSlot());
        UUID pUUID = p.getUniqueId();

        if (Utilities.isSlimeEyeGod(p.getInventory().getItemInMainHand())) {
            if (activeTasks.containsKey(pUUID) && !Utilities.isSlimeEyeGod(newItem)) {
                cancelHighlightTask(pUUID);
                cachedBlocks.remove(pUUID, cachedBlocks);
                toggledOn.put(pUUID, false);
                invisibleToggledOn.put(pUUID, false);
                p.sendMessage("§cSlime Gaze disabled.");
            }
        }
    }
}