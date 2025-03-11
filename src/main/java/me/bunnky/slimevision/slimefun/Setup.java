package me.bunnky.slimevision.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.experimental.UtilityClass;
import me.bunnky.slimevision.SlimeVision;
import me.bunnky.slimevision.items.GoldenSlimeFish;
import me.bunnky.slimevision.items.ParticleItem;
import me.bunnky.slimevision.items.SFS;
import me.bunnky.slimevision.items.slimeeyes.SlimeEye;
import me.bunnky.slimevision.items.slimeeyes.SlimeCleaner;
import me.bunnky.slimevision.items.slimeeyes.SlimeEyeGod;
import me.bunnky.slimevision.items.slimeeyes.SlimeEyeInverted;
import me.bunnky.slimevision.items.slimeeyes.SlimeEyeNetworks;
import me.bunnky.slimevision.items.SlimeScribe;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import net.guizhanss.guizhanlib.minecraft.utils.compatibility.MaterialX;

@UtilityClass
public class Setup {


    public static void setup() {

        SlimeVision plugin = SlimeVision.getInstance();

        ItemStack groupItem = CustomItemStack.create(Material.ENDER_EYE, "&bSlime Vision", "", "I wish this worked!");
        NamespacedKey groupId = new NamespacedKey(SlimeVision.getInstance(), "slime_vision");
        ItemGroup group = new ItemGroup(groupId, groupItem);

        //########################
        //  ITEMS
        //########################

        //////////////////////////////////////////////
        /////////////// SLIME EYES ///////////////////
        //////////////////////////////////////////////

        SlimefunItemStack slimeEye = new SlimefunItemStack(
            "SV_SLIMEEYE",
            Material.ENDER_EYE,
            "&2Slime Eye",
            "",
            "&eR-Click: &7Activate Slime Gaze",
            "&7for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &760 Seconds",
            "&eRange: &720"
        );
        SlimefunItemStack slimeOrb = new SlimefunItemStack(
            "SV_SLIMEORB",
            Material.SLIME_BALL,
            "&aSlime Orb",
            "",
            "&eR-Click: &7Activate Slime Gaze",
            "&7for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &740 Seconds",
            "&eRange: &712"
        );
        SlimefunItemStack slimeGlob = new SlimefunItemStack(
            "SV_SLIMEGLOB",
            Material.LIME_DYE,
            "&bSlime Glob",
            "",
            "&eR-Click: &7Activate Slime Gaze",
            "&7for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &725 Seconds",
            "&eRange: &77"
        );
        SlimefunItemStack slimeChunk = new SlimefunItemStack(
            "SV_SLIMECHUNK",
            MaterialX.TURTLE_SCUTE,
            "&3Slime Chunk",
            "",
            "&eR-Click: &7Activate Slime Gaze",
            "&7for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &710 Seconds",
            "&eRange: &73"
        );
        SlimefunItemStack slimeEyeNetworks = new SlimefunItemStack(
            "SV_SLIMEEYE_NETWORKS",
            Material.HONEYCOMB,
            "&dNetworks &2Slime Eye",
            "",
            "&eR-Click: &7Activate Networks ",
            "&7Slime Gaze for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &760 Seconds",
            "&eRange: &720"
        );
        SlimefunItemStack slimeOrbNetworks = new SlimefunItemStack(
            "SV_SLIMEORB_NETWORKS",
            Material.SUNFLOWER,
            "&dNetworks &aSlime Orb",
            "",
            "&eR-Click: &7Activate Networks ",
            "&7Slime Gaze for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &740 Seconds",
            "&eRange: &712"
        );
        SlimefunItemStack slimeGlobNetworks = new SlimefunItemStack(
            "SV_SLIMEGLOB_NETWORKS",
            Material.HORN_CORAL,
            "&dNetworks &bSlime Glob",
            "",
            "&eR-Click: &7Activate Networks ",
            "&7Slime Gaze for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &725 Seconds",
            "&eRange: &77"
        );
        SlimefunItemStack slimeChunkNetworks = new SlimefunItemStack(
            "SV_SLIMECHUNK_NETWORKS",
            Material.YELLOW_DYE,
            "&dNetworks &3Slime Chunk",
            "",
            "&eR-Click: &7Activate Networks ",
            "&7Slime Gaze for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &710 Seconds",
            "&eRange: &73"
        );
        SlimefunItemStack slimeEyeInverted = new SlimefunItemStack(
            "SV_SLIMEEYE_INVERTED",
            Material.SNOWBALL,
            "&fInverted &2Slime Eye",
            "",
            "&eR-Click: &7Activate Inverted",
            "&7Slime Gaze for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &760 Seconds",
            "&eRange: &720"
        );
        SlimefunItemStack slimeOrbInverted = new SlimefunItemStack(
            "SV_SLIMEORB_INVERTED",
            Material.CLAY_BALL,
            "&fInverted &aSlime Orb",
            "",
            "&eR-Click: &7Activate Inverted",
            "&7Slime Gaze for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &740 Seconds",
            "&eRange: &712"
        );
        SlimefunItemStack slimeGlobInverted = new SlimefunItemStack(
            "SV_SLIMEGLOB_INVERTED",
            Material.WHITE_DYE,
            "&fInverted &bSlime Glob",
            "",
            "&eR-Click: &7Activate Inverted",
            "&7Slime Gaze for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &725 Seconds",
            "&eRange: &77"
        );
        SlimefunItemStack slimeChunkInverted = new SlimefunItemStack(
            "SV_SLIMECHUNK_INVERTED",
            Material.LIGHT_GRAY_DYE,
            "&fInverted &3Slime Chunk",
            "",
            "&eR-Click: &7Activate Inverted",
            "&7Slime Gaze for 10 seconds",
            "&eSneak + R-Click: &7Change",
            "&7glow color",
            "",
            "&eCooldown: &710 Seconds",
            "&eRange: &73"
        );

        //////////////////////////////////////////////
        //////////////// MATERIALS ///////////////////
        //////////////////////////////////////////////

        SlimefunItemStack particleBase = new SlimefunItemStack(
            "SV_PARTICLE_BASE",
            Material.CANDLE,
            "&fParticle Base"
        );
        SlimefunItemStack particleTop = new SlimefunItemStack(
            "SV_PARTICLE_TOP",
            Material.RED_CANDLE,
            "&cParticle: Top"
        );
        SlimefunItemStack particleBottom = new SlimefunItemStack(
            "SV_PARTICLE_BOTTOM",
            Material.CYAN_CANDLE,
            "&3Particle: Bottom"
        );
        SlimefunItemStack particleEast = new SlimefunItemStack(
            "SV_PARTICLE_EAST",
            Material.YELLOW_CANDLE,
            "&eParticle: East"
        );
        SlimefunItemStack particleWest = new SlimefunItemStack(
            "SV_PARTICLE_WEST",
            Material.GREEN_CANDLE,
            "&aParticle: West"
        );
        SlimefunItemStack particleSouth = new SlimefunItemStack(
            "SV_PARTICLE_SOUTH",
            Material.ORANGE_CANDLE,
            "&6Particle: South"
        );
        SlimefunItemStack particleNorth = new SlimefunItemStack(
            "SV_PARTICLE_NORTH",
            Material.PURPLE_CANDLE,
            "&5Particle: North"
        );

        //////////////////////////////////////////////
        /////////////////// OTHER ////////////////////
        //////////////////////////////////////////////

        SlimefunItemStack slimeEyeCleaner = new SlimefunItemStack(
            "SV_SLIMECLEANER",
            Material.FIRE_CHARGE,
            "&4Slime Cleaner",
            "",
            "&eR-Click: &7Break nearby machines",
            "&7& clear BlockStorage data",
            "",
            "&eRange: &710",
            "",
            "&c\"&oI should be careful with this!\""
        );

        SlimefunItemStack slimeScribe = new SlimefunItemStack(
            "SV_SLIMESCRIBE",
            Material.BRUSH,
            "&fSlime Scribe",
            "",
            "&eR-Click: &7Check area and log",
            "&7invisible machine locations to",
            "&7a book",
            "",
            "&eCooldown: &730 Seconds",
            "&eRange: &730"
        );

        SlimefunItemStack slimeEyeGod = new SlimefunItemStack(
            "SV_SLIMEEYE_GOD",
            Material.SLIME_SPAWN_EGG,
            "&4&lSlime God",
            "",
            "&eR-Click: &7Toggle Slime God Gaze",
            "&eSneak + R-Click: &7Toggle Inverted",
            "&7Slime God Gaze",
            "&eL-Click: &7Clear BlockStorage",
            "&eSneak + L-Click: &7Place block",
            "",
            "&e[&aSlime God Gaze automatically",
            "&aremoves null machines&e]",
            "&e[&aOffhand use places a block on",
            "&ainvisible machine locations&e]"

        );
        SlimefunItemStack goldenSlimeFish = new SlimefunItemStack(
            "SV_GOLDEN_SLIMEFISH",
            Material.PUFFERFISH,
            "&6Golden Slime Fish",
            "",
            "&eL-Click: &7Instantly break a",
            "&7Slimefun block and clear its",
            "&7BlockStorage data",
            "",
            "&c\"&oI should be careful with this!\""
        );
        SlimefunItemStack sfsItem = new SlimefunItemStack(
            "SV_SFS",
            Material.BOOK,
            "&4SFS (╯°□°)╯︵ ┻━┻",
            "",
            "&eR-Click: &7Instantly perform",
            "&7a Slimefun save",
            "",
            "&c\"&oI should be careful with this!\""
        );


        //////////////////////////////////////////////
        ////////////////// GUIDES ////////////////////
        //////////////////////////////////////////////

        SlimefunItemStack basicsGuide = new SlimefunItemStack(
            "SV_GUIDE_BASIC",
            Material.BOOK,
            "&bSlime Vision Help",
            "",
            "&bSlime Vision&7 gives players and admins a way",
            "&7to visualize Slimefun setups. Slime Eyes do",
            "&7this by using an effect we call &fSlime Gaze&7.",
            "",
            "&f&nSlime Gaze&7 causes all Slimefun machines",
            "&7within a radius to glow. &oThe glow can even be",
            "&7&oseen through walls or obstacles!",
            ""
        );

        SlimefunItemStack basicsGuide2 = new SlimefunItemStack(
            "SV_GUIDE_BASIC2",
            Material.BOOK,
            "&bSlime Vision Help",
            "",
            "&f&nInverted&7 Slime Gaze looks for air blocks that",
            "&7contain Slimefun Block Storage data, and will",
            "&7mark that location with a red glow. &oThis",
            "&7&ois really helpful if your server crashes.",
            "",
            "&f&nNetworks&7 Slime Gaze is exclusive to Network",
            "&7Slime Eyes, and will only be enabled if Networks",
            "&7is installed. &oWorks similar to &f&oSlime Gaze&7&o, but",
            "&7&oonly looks for Networks machines/blocks.",
            ""
        );

        SlimefunItemStack basicsGuide3 = new SlimefunItemStack(
            "SV_GUIDE_BASIC3",
            Material.BOOK,
            "&bSlime Vision Help",
            "",
            "&7The &f&nSlime Scribe&7 will check for invisible",
            "&7machines and fill a book with location data if",
            "&7any are found. It's a good way to save the",
            "&7&olocations for yourself, or for reporting.",
            "",
            "&f&nParticle&7 items are mostly used for crafting",
            "&7recipes, but can also fire short particle beams.",
            "&7&oReally useful for pointing out machines, or",
            "&7&ojust for fun!",
            ""
        );

        SlimefunItemStack basicsGuide4 = new SlimefunItemStack(
            "SV_GUIDE_BASIC4",
            Material.BOOK,
            "&bSlime Vision Help",
            "",
            "&7Most of &bSlime Vision&7's true power comes in",
            "&7the form of some of the hidden items, which",
            "&7are only available to admins with /sf give.",
            "&7These include &f&nSlime God&7, &f&nSlime Cleaner&7,",
            "&7&f&nSFS&7, and the &f&nGolden Slime Fish&7. The Golden",
            "&7Slime Fish is the only admin item that doesn't",
            "&7require OP to use. &oThese tools were designed",
            "&7&ofor server admins to help diagnose and fix",
            "&7&oBlock Storage issues for players.",
            "",
            "&c&nAll admin only items come with risks. Be sure",
            "&c&nto use with caution! Descriptions for these",
            "&c&ncan be found on each item.",
            ""
        );
        //########################
        //  RECIPES
        //########################

        ItemStack[] slimeScribeRecipe = {
            null, null, null,
            null, slimeEyeInverted.item(), null,
            null, null, null
        };
        ItemStack[] slimeEyeInvertedRecipe = {
            null, SlimefunItems.HEAVY_CREAM.item(), null,
            null, slimeEye.item(), null,
            null, null, null
        };
        ItemStack[] slimeOrbInvertedRecipe = {
            null, SlimefunItems.HEAVY_CREAM.item(), null,
            null, slimeOrb.item(), null,
            null, null, null
        };
        ItemStack[] slimeGlobInvertedRecipe = {
            null, SlimefunItems.HEAVY_CREAM.item(), null,
            null, slimeGlob.item(), null,
            null, null, null
        };
        ItemStack[] slimeChunkInvertedRecipe = {
            null, SlimefunItems.HEAVY_CREAM.item(), null,
            null, slimeChunk.item(), null,
            null, null, null
        };
        ItemStack[] slimeEyeNetworksRecipe = {
            null, null, null,
            null, slimeEye.item(), null,
            null, null, null
        };
        ItemStack[] slimeOrbNetworksRecipe = {
            null, null, null,
            null, slimeOrb.item(), null,
            null, null, null
        };
        ItemStack[] slimeGlobNetworksRecipe = {
            null, null, null,
            null, slimeGlob.item(), null,
            null, null, null
        };
        ItemStack[] slimeChunkNetworksRecipe = {
            null, null, null,
            null, slimeChunk.item(), null,
            null, null, null
        };
        ItemStack[] slimeEyeRecipe = {
            particleNorth.item(), particleTop.item(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(),
            particleWest.item(), slimeOrb.item(), particleEast.item(),
            SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(), particleBottom.item(), particleSouth.item()
        };
        ItemStack[] slimeOrbRecipe = {
            particleNorth.item(), particleTop.item(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(),
            particleWest.item(), slimeGlob.item(), particleEast.item(),
            SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(), particleBottom.item(), particleSouth.item()
        };
        ItemStack[] slimeGlobRecipe = {
            particleNorth.item(), particleTop.item(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(),
            particleWest.item(), slimeChunk.item(), particleEast.item(),
            SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(), particleBottom.item(), particleSouth.item()
        };
        ItemStack[] slimeChunkRecipe = {
            particleNorth.item(), particleTop.item(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(),
            particleWest.item(), SlimefunItems.NETHER_ICE.item(), particleEast.item(),
            SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(), particleBottom.item(), particleSouth.item()
        };
        ItemStack[] particleBaseRecipe = {
            SlimefunItems.AIR_RUNE.item(), SlimefunItems.EARTH_RUNE.item(), SlimefunItems.FIRE_RUNE.item(),
            SlimefunItems.WATER_RUNE.item(), new ItemStack(Material.CANDLE), SlimefunItems.LIGHTNING_RUNE.item(),
            SlimefunItems.ESSENCE_OF_AFTERLIFE.item(), SlimefunItems.RAINBOW_RUNE.item(), SlimefunItems.SMALL_URANIUM.item()
        };
        ItemStack[] particleTopRecipe = {
            null, new ItemStack(Material.RED_DYE), null,
            null, particleBase.item(), null,
            SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.FUEL_BUCKET.item(), SlimefunItems.STEEL_THRUSTER.item()
        };
        ItemStack[] particleBottomRecipe = {
            SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.FUEL_BUCKET.item(), SlimefunItems.STEEL_THRUSTER.item(),
            null, particleBase.item(), null,
            null, new ItemStack(Material.CYAN_DYE), null
        };
        ItemStack[] particleEastRecipe = {
            SlimefunItems.STEEL_THRUSTER.item(), null, null,
            SlimefunItems.FUEL_BUCKET.item(), particleBase.item(), new ItemStack(Material.YELLOW_DYE),
            SlimefunItems.STEEL_THRUSTER.item(), null, null
        };
        ItemStack[] particleWestRecipe = {
            null, null, SlimefunItems.STEEL_THRUSTER.item(),
            new ItemStack(Material.GREEN_DYE), particleBase.item(), SlimefunItems.FUEL_BUCKET.item(),
            null, null, SlimefunItems.STEEL_THRUSTER.item()
        };
        ItemStack[] particleSouthRecipe = {
            SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.FUEL_BUCKET.item(), SlimefunItems.STEEL_THRUSTER.item(),
            null, particleBase.item(), null,
            null, new ItemStack(Material.ORANGE_DYE), null
        };
        ItemStack[] particleNorthRecipe = {
            null, new ItemStack(Material.PURPLE_DYE), null,
            null, particleBase.item(), null,
            SlimefunItems.STEEL_THRUSTER.item(), SlimefunItems.FUEL_BUCKET.item(), SlimefunItems.STEEL_THRUSTER.item()
        };

        //########################
        //  INSTANTIATE
        //########################

        SlimeEye slimeeyegod = new SlimeEyeGod(group, slimeEyeGod, RecipeType.NULL, null, 30);
        GoldenSlimeFish goldenslimefish = new GoldenSlimeFish(group, goldenSlimeFish, RecipeType.NULL, null);
        SFS sfs = new SFS(group, sfsItem, RecipeType.NULL, null);
        SlimeCleaner slimeeyecleaner = new SlimeCleaner(group, slimeEyeCleaner, RecipeType.NULL, null);


        SlimefunItem basicguide = new SlimefunItem(group, basicsGuide, RecipeType.NULL, null);
        SlimefunItem basicguide2 = new SlimefunItem(group, basicsGuide2, RecipeType.NULL, null);
        SlimefunItem basicguide3 = new SlimefunItem(group, basicsGuide3, RecipeType.NULL, null);
        SlimefunItem basicguide4 = new SlimefunItem(group, basicsGuide4, RecipeType.NULL, null);

        SlimeEye slimeeye = new SlimeEye(group, slimeEye, RecipeType.MAGIC_WORKBENCH, slimeEyeRecipe, 20, 60000);
        SlimeEye slimeorb = new SlimeEye(group, slimeOrb, RecipeType.MAGIC_WORKBENCH, slimeOrbRecipe, 12, 40000);
        SlimeEye slimeglob = new SlimeEye(group, slimeGlob, RecipeType.MAGIC_WORKBENCH, slimeGlobRecipe, 7, 25000);
        SlimeEye slimechunk = new SlimeEye(group, slimeChunk, RecipeType.ENHANCED_CRAFTING_TABLE, slimeChunkRecipe, 3, 10000);

        SlimeEyeInverted slimeeyeinverted = new SlimeEyeInverted(group, slimeEyeInverted, RecipeType.MAGIC_WORKBENCH, slimeEyeInvertedRecipe, 20, 60000);
        SlimeEyeInverted slimeorbinverted = new SlimeEyeInverted(group, slimeOrbInverted, RecipeType.MAGIC_WORKBENCH, slimeOrbInvertedRecipe, 12, 40000);
        SlimeEyeInverted slimeglobinverted = new SlimeEyeInverted(group, slimeGlobInverted, RecipeType.MAGIC_WORKBENCH, slimeGlobInvertedRecipe, 7, 25000);
        SlimeEyeInverted slimechunkinverted = new SlimeEyeInverted(group, slimeChunkInverted, RecipeType.MAGIC_WORKBENCH, slimeChunkInvertedRecipe, 3, 10000);

        SlimeScribe slimescribe = new SlimeScribe(group, slimeScribe, RecipeType.MAGIC_WORKBENCH, slimeScribeRecipe, 30, 30000);

        SlimefunItem particlebase = new ParticleItem(group, particleBase, RecipeType.ENHANCED_CRAFTING_TABLE, particleBaseRecipe, Color.WHITE);
        SlimefunItem particletop = new ParticleItem(group, particleTop, RecipeType.MAGIC_WORKBENCH, particleTopRecipe, Color.RED);
        SlimefunItem particlebottom = new ParticleItem(group, particleBottom, RecipeType.MAGIC_WORKBENCH, particleBottomRecipe,  Color.AQUA);
        SlimefunItem particleeast = new ParticleItem(group, particleEast, RecipeType.MAGIC_WORKBENCH, particleEastRecipe,  Color.YELLOW);
        SlimefunItem particlewest = new ParticleItem(group, particleWest, RecipeType.MAGIC_WORKBENCH, particleWestRecipe,  Color.LIME);
        SlimefunItem particlesouth = new ParticleItem(group, particleSouth, RecipeType.MAGIC_WORKBENCH, particleSouthRecipe,  Color.ORANGE);
        SlimefunItem particlenorth = new ParticleItem(group, particleNorth, RecipeType.MAGIC_WORKBENCH, particleNorthRecipe,  Color.PURPLE);

        //########################
        //  REGISTER
        //########################

        basicguide.register(plugin);
        basicguide2.register(plugin);
        basicguide3.register(plugin);
        basicguide4.register(plugin);
        particlebase.register(plugin);
        particletop.register(plugin);
        particlebottom.register(plugin);
        particleeast.register(plugin);
        particlewest.register(plugin);
        particlesouth.register(plugin);
        particlenorth.register(plugin);
        slimechunk.register(plugin);
        slimeglob.register(plugin);
        slimeorb.register(plugin);
        slimeeye.register(plugin);
        slimechunkinverted.register(plugin);
        slimeglobinverted.register(plugin);
        slimeorbinverted.register(plugin);
        slimeeyeinverted.register(plugin);
        slimescribe.register(plugin);
        slimeeyegod.register(plugin);
        slimeeyegod.setHidden(true);
        goldenslimefish.register(plugin);
        goldenslimefish.setHidden(true);
        sfs.register(plugin);
        sfs.setHidden(true);
        slimeeyecleaner.register(plugin);
        slimeeyecleaner.setHidden(true);

        //########################
        //  NETWORKS STUFF
        //########################

        if (Bukkit.getPluginManager().isPluginEnabled("Networks")) {
            SlimeEyeNetworks slimeeyenetworks = new SlimeEyeNetworks(group, slimeEyeNetworks, RecipeType.MAGIC_WORKBENCH, slimeEyeNetworksRecipe, 20, 60000);
            SlimeEyeNetworks slimeorbnetworks = new SlimeEyeNetworks(group, slimeOrbNetworks, RecipeType.MAGIC_WORKBENCH, slimeOrbNetworksRecipe, 12, 40000);
            SlimeEyeNetworks slimeglobnetworks = new SlimeEyeNetworks(group, slimeGlobNetworks, RecipeType.MAGIC_WORKBENCH, slimeGlobNetworksRecipe, 7, 25000);
            SlimeEyeNetworks slimechunknetworks = new SlimeEyeNetworks(group, slimeChunkNetworks, RecipeType.MAGIC_WORKBENCH, slimeChunkNetworksRecipe, 3, 10000);

            slimechunknetworks.register(plugin);
            slimeglobnetworks.register(plugin);
            slimeorbnetworks.register(plugin);
            slimeeyenetworks.register(plugin);

            NamespacedKey slimeChunkNetworksResearch = new NamespacedKey(plugin, "slime_chunk_networks");
            NamespacedKey slimeGlobNetworksResearch = new NamespacedKey(plugin, "slime_glob_networks");
            NamespacedKey slimeOrbNetworksResearch = new NamespacedKey(plugin, "slime_orb_networks");
            NamespacedKey slimeEyeNetworksResearch = new NamespacedKey(plugin, "slime_eye_networks");

            Research slimechunknetworksresearch = new Research(slimeChunkNetworksResearch, 9, "It's yellow? A yellow Slime?", 20);
            Research slimeglobnetworksresearch = new Research(slimeGlobNetworksResearch, 8, "This thing is still growing...", 40);
            Research slimeorbnetworksresearch = new Research(slimeOrbNetworksResearch, 7, "Is there a limit, orrrrr", 60);
            Research slimeeyenetworksresearch = new Research(slimeEyeNetworksResearch, 6, "It's glowing!", 100);

            slimeeyenetworksresearch.addItems(slimeeyenetworks);
            slimeorbnetworksresearch.addItems(slimeorbnetworks);
            slimeglobnetworksresearch.addItems(slimeglobnetworks);
            slimechunknetworksresearch.addItems(slimechunknetworks);

            slimeeyenetworksresearch.register();
            slimeorbnetworksresearch.register();
            slimeglobnetworksresearch.register();
            slimechunknetworksresearch.register();
        }

        //########################
        //  RESEARCH
        //########################

        NamespacedKey slimeChunkResearch = new NamespacedKey(plugin, "slime_chunk");
        NamespacedKey slimeGlobResearch = new NamespacedKey(plugin, "slime_glob");
        NamespacedKey slimeOrbResearch = new NamespacedKey(plugin, "slime_orb");
        NamespacedKey slimeEyeResearch = new NamespacedKey(plugin, "slime_eye");
        NamespacedKey particleResearch = new NamespacedKey(plugin, "particle");

        Research slimechunkresearch = new Research(slimeChunkResearch, 1, "I don't think this is slime...", 20);
        Research slimeglobresearch = new Research(slimeGlobResearch, 2, "More slime for the slime god.", 40);
        Research slimeorbresearch = new Research(slimeOrbResearch, 3, "It seems to be evolving!", 60);
        Research slimeeyeresearch = new Research(slimeEyeResearch, 4, "What have I discovered??", 100);
        Research particleresearch = new Research(particleResearch, 5, "ALL THE COLORS!!", 10);

        slimechunkresearch.addItems(slimechunk, slimechunkinverted);
        slimeglobresearch.addItems(slimeglob, slimeglobinverted);
        slimeorbresearch.addItems(slimeorb, slimeorbinverted);
        slimeeyeresearch.addItems(slimeeye, slimeeyeinverted, slimescribe);
        particleresearch.addItems(particlebase, particletop, particlebottom, particleeast, particlenorth, particlesouth, particlewest);

        slimechunkresearch.register();
        slimeglobresearch.register();
        slimeorbresearch.register();
        slimeeyeresearch.register();
        particleresearch.register();
    }
}