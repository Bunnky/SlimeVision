package me.bunnky.slimevision.items.slimeeyes;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.bunnky.slimevision.SlimeVision;
import me.bunnky.slimevision.utility.Utilities;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
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
    protected int msgCd = 0;
    protected int prevCount = 0;
    protected int colorIdx = 0;

    protected final Set<Block> cached = new HashSet<>();

    protected static final Map<UUID, BukkitTask> activeTasks = new HashMap<>();
    protected static final Map<UUID, Long> lastActionTime = new HashMap<>();

    protected static final Color[] colorOpts = {Color.RED, Color.AQUA, Color.YELLOW, Color.GREEN, Color.ORANGE, Color.PURPLE, Color.BLUE, Color.BLACK, Color.WHITE, Color.GRAY, Color.LIME, Color.FUCHSIA, Color.MAROON, Color.TEAL};

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
        UUID playerUUID = p.getUniqueId();

        if (p.isSneaking()) {
            cycleColor(p);
            return;
        }

        Long lastTime = lastActionTime.get(playerUUID);
        long currentTime = System.currentTimeMillis();
        int cooldown = getCooldown();

        if (lastTime != null && (currentTime - lastTime) < cooldown) {
            long remainingTime = (cooldown - (currentTime - lastTime)) / 1000;
            p.sendMessage("§cYou must wait " + remainingTime + " seconds.");
            return;
        }

        lastActionTime.put(playerUUID, currentTime);
        cached.clear();
        startHighlight(p);
    }

    protected void cleanCooldowns() {
        long now = System.currentTimeMillis();
        lastActionTime.entrySet().removeIf(entry -> (now - entry.getValue()) > cd);
    }


    protected void cycleColor(@NotNull Player p) {
        colorIdx = (colorIdx + 1) % (colorOpts.length + 1);
        String colorName = colorIdx == 0 ? "§eDefault" : Utilities.getColorName(colorOpts[colorIdx - 1]);
        p.sendMessage("§aParticle color set to: §e" + colorName);
    }

    protected void startHighlight(@NotNull Player p) {
        UUID playerUUID = p.getUniqueId();

        if (activeTasks.containsKey(playerUUID)) {
            return;
        }

        p.sendMessage("§6Slime Gaze enabled.");
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                checkBlocks(p, false);
            }
        }.runTaskTimer(SlimeVision.getInstance(), 0L, 40L);

        activeTasks.put(playerUUID, task);

        getServer().getScheduler().runTaskLater(SlimeVision.getInstance(), () -> {
            cancelHighlight(playerUUID);
            p.sendMessage("§cSlime Gaze disabled.");

        }, 10 * 20L);
    }

    protected void checkBlocks(@NotNull Player p, boolean inverted) {
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
                        addParticle(p, b);
                    } else if (Utilities.hasBlockStorage(b)) {
                        cached.add(b);
                        currBlocks.add(b);
                        addParticle(p, b);
                    }
                }
            }
        }
        cached.retainAll(currBlocks);
        sendCountMessage(p, currBlocks.size(), inverted);
    }

    protected void addParticle(Player p, Block b) {
        Vector blockLocation = b.getLocation().toVector();

        if (colorIdx == 0) {
            Utilities.particlesVanilla(p, blockLocation);
        } else {
            Utilities.particlesSingleColor(p, blockLocation, colorOpts[colorIdx - 1]);
        }
    }

    protected void sendCountMessage(Player p, int totalHighlightedMachines, boolean inverted) {
        if (msgCd <= 0 || totalHighlightedMachines != prevCount) {
            String message = inverted
                             ? "§e" + totalHighlightedMachines + " §aInvisible machines in range"
                             : "§e" + totalHighlightedMachines + " §aMachines in range";
            p.sendMessage(message);
            msgCd = 5;
        } else {
            msgCd--;
        }

        prevCount = totalHighlightedMachines;
    }

    @EventHandler
    protected void onItemHeld(@NotNull PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack newItem = p.getInventory().getItem(e.getNewSlot());
        UUID playerUUID = p.getUniqueId();

        if (activeTasks.containsKey(playerUUID) && !Utilities.isSlimeEye(newItem)) {
            cancelHighlight(playerUUID);
            cached.clear();
            p.sendMessage("§cSlime Gaze disabled.");
        }
    }

    protected void cancelHighlight(UUID playerUUID) {
        BukkitTask task = activeTasks.remove(playerUUID);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    @EventHandler
    protected void onPlayerQuit(PlayerQuitEvent e) {
        UUID playerUUID = e.getPlayer().getUniqueId();

        if (activeTasks.containsKey(playerUUID)) {
            cancelHighlight(playerUUID);
            cached.clear();
        }
    }
}