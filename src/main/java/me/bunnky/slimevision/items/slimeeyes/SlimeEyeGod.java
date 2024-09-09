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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SlimeEyeGod extends SlimeEye {

    private boolean isToggledOn = false;
    private boolean isInvisibleToggledOn = false;
    private int netMsgCooldown = 0, prevNetCount = 0, prevAirCount = 0, airMsgCooldown = 0, nullMsgCooldown = 0, prevNullCount = 0;

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

        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock == null || e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        e.setCancelled(true);

        Player p = e.getPlayer();
        if (!p.isOp()) {
            return;
        }

        if (p.isSneaking()) {
            placeBlock(clickedBlock, e.getBlockFace());
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    BlockBreakEvent e = new BlockBreakEvent(clickedBlock, p);
                    Bukkit.getPluginManager().callEvent(e);
                    if (e.isCancelled()) {
                        return;
                    }

                    BlockStorage.clearBlockInfo(clickedBlock);
                    clickedBlock.setType(Material.AIR);
                }
            }.runTaskLater(SlimeVision.getInstance(), 1);
        }
    }

    private void placeBlock(Block block, BlockFace face) {
        Utilities.placeBlock(block, face, Material.TARGET);
    }

    @Override
    protected void onRightClick(@NotNull PlayerRightClickEvent e) {
        e.cancel();
        Player p = e.getPlayer();

        if (!p.isOp() || e.getHand() == null) {
            return;
        }

        UUID playerUUID = p.getUniqueId();

        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            if (!cached.isEmpty()) {
                cached.clear();
            }

            checkInvisibleBlocks(p);

            if (!cached.isEmpty()) {
                placeBlocksAtInvisibleLocations();
                p.sendMessage("§6Blocks placed at invisible machine locations.");
            } else {
                p.sendMessage("§cNo invisible machines to place blocks at.");
            }
            return;
        }

        if (p.isSneaking()) {
            isInvisibleToggledOn = !isInvisibleToggledOn;
            if (isInvisibleToggledOn) {
                if (isToggledOn) {
                    cancelHighlight(playerUUID);
                    isToggledOn = false;
                    p.sendMessage("§cSlime Gaze disabled.");
                }
                cached.clear();
                startHighlightTask(p, true);
                p.sendMessage("§6Inverted Slime Gaze enabled.");
            } else {
                cancelHighlight(playerUUID);
                cached.clear();
                p.sendMessage("§cInverted Slime Gaze disabled.");
            }
            return;
        }

        if (isInvisibleToggledOn) {
            cancelHighlight(playerUUID);
            isInvisibleToggledOn = false;
            p.sendMessage("§cInverted Slime Gaze disabled.");
        }

        isToggledOn = !isToggledOn;
        if (isToggledOn) {
            cached.clear();
            startHighlightTask(p, false);
            p.sendMessage("§6Slime Gaze enabled.");
        } else {
            cancelHighlight(playerUUID);
            cached.clear();
            p.sendMessage("§cSlime Gaze disabled.");
        }
    }

    protected void startHighlightTask(@NotNull Player p, boolean isInvisible) {
        UUID playerUUID = p.getUniqueId();

        if (activeTasks.containsKey(playerUUID) || (isInvisible ? !isInvisibleToggledOn : !isToggledOn)) {
            return;
        }

        activeTasks.put(playerUUID, new BukkitRunnable() {
            @Override
            public void run() {
                if (isInvisible) {
                    checkInvisibleBlocks(p);
                } else {
                    checkBlocks(p);
                }
            }
        }.runTaskTimer(SlimeVision.getInstance(), 0L, 40L));
    }

    protected void checkInvisibleBlocks(@NotNull Player p) {
        checkBlocks(p, true);
    }

    protected void checkBlocks(@NotNull Player p) {
        checkBlocks(p, false);
    }

    private void placeBlocksAtInvisibleLocations() {
        for (Block invisibleBlock : cached) {
            if (Utilities.isAirOrLiquid(invisibleBlock)) {
                Utilities.placeBlock(invisibleBlock, BlockFace.SELF, Material.TARGET);
            }
        }
    }

    @Override
    protected void checkBlocks(@NotNull Player p, boolean checkInvisible) {
        World world = p.getWorld();
        Vector playerLocation = Utilities.getPlayerVector(p);
        int pX = playerLocation.getBlockX(), pY = playerLocation.getBlockY(), pZ = playerLocation.getBlockZ();
        int minX = pX - r, minY = Utilities.getClampedCoordinate(pY, -r, -64, 320), minZ = pZ - r;
        int maxX = pX + r, maxY = Utilities.getClampedCoordinate(pY, r, -64, 320), maxZ = pZ + r;

        Set<Block> networkBlocks = new HashSet<>();
        Set<Block> airBlocks = new HashSet<>();
        List<String> nullMachines = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = world.getBlockAt(x, y, z);

                    if (Utilities.hasBlockStorage(b)) {
                        SlimefunItem sfItem = BlockStorage.check(b);
                        if (sfItem != null) {
                            if (checkInvisible) {
                                if (Utilities.isAirOrLiquid(b)) {
                                    airBlocks.add(b);
                                    cached.add(b);
                                    addInvertedParticle(p, b);
                                }
                            } else {
                                if ("Networks".equals(sfItem.getAddon().getName())) {
                                    networkBlocks.add(b);
                                    cached.add(b);
                                    Utilities.particlesNetworks(p, b.getLocation().toVector());
                                } else if (Utilities.isAirOrLiquid(b)) {
                                    airBlocks.add(b);
                                    cached.add(b);
                                    addInvertedParticle(p, b);
                                } else {
                                    addParticle(p, b);
                                    cached.add(b);
                                }
                            }
                        } else {
                            addInvertedParticle(p, b);
                            nullMachines.add(b.getX() + ", " + b.getY() + ", " + b.getZ());
                            BlockStorage.clearBlockInfo(b);
                        }
                    }
                }
            }
        }
        sendCountMessage(p, networkBlocks.size(), cached.size(), airBlocks.size(), nullMachines);
    }

    @Override
    protected void addParticle(Player p, Block b) {
        Vector loc = b.getLocation().toVector();
        Utilities.particlesVanilla(p, loc);
    }

    protected void addInvertedParticle(Player p, Block b) {
        Vector blockCenter = b.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5));
        Utilities.particlesInverted(p, blockCenter);
    }

    private void sendCountMessage(Player p, int networkCount, int totalCount, int airCount, List<String> nullMachines) {
        if (isInvisibleToggledOn) {
            sendAirMsg(p, airCount);
        } else {
            super.sendCountMessage(p, totalCount, false);
            sendNetworksMsg(p, networkCount);
            sendAirMsg(p, airCount);
            sendNullMsg(p, nullMachines);
        }
    }

    private void sendNetworksMsg(Player p, int networkCount) {
        netMsgCooldown = Utilities.sendCountMessage(p, networkCount, "Network machines", netMsgCooldown, prevNetCount);
        prevNetCount = networkCount;
    }

    private void sendAirMsg(Player p, int airCount) {
        airMsgCooldown = Utilities.sendCountMessage(p, airCount, "Invisible machines", airMsgCooldown, prevAirCount);
        prevAirCount = airCount;
    }

    private void sendNullMsg(Player p, List<String> nullMachines) {
        nullMsgCooldown = Utilities.sendNullCountMessage(p, nullMachines, nullMsgCooldown, prevNullCount);
        prevNullCount = nullMachines.size();
    }

    @EventHandler
    @Override
    protected void onItemHeld(@NotNull PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack newItem = p.getInventory().getItem(e.getNewSlot());
        UUID playerUUID = p.getUniqueId();

        if (activeTasks.containsKey(playerUUID) && !Utilities.isSlimeEyeGod(newItem)) {
            cancelHighlight(playerUUID);
            cached.clear();
            isToggledOn = false;
            isInvisibleToggledOn = false;
            p.sendMessage("§cSlime Gaze disabled.");
        }
    }

    @Override
    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent e) {
        UUID playerUUID = e.getPlayer().getUniqueId();
        if (activeTasks.containsKey(playerUUID)) {
            cancelHighlight(playerUUID);
        }
    }
}