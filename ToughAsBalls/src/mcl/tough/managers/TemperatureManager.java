package mcl.tough.managers;

import mcl.tough.objects.EnvironmentalEffect;
import mcl.tough.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public class TemperatureManager
{
    // Hot (+)
    private static final EnvironmentalEffect Flame = new EnvironmentalEffect(1, "Flame");
    private static final EnvironmentalEffect Sun = new EnvironmentalEffect(1, "Sun");
    //private static final EnvironmentalEffect Torch = new EnvironmentalEffect(1, "Torch");
    private static final EnvironmentalEffect Depth = new EnvironmentalEffect(1, "Depth");

    // Cold (-)
    private static final EnvironmentalEffect Wet = new EnvironmentalEffect(-1, "Wet");
    private static final EnvironmentalEffect Weather = new EnvironmentalEffect(-1, "Weather");
    private static final EnvironmentalEffect Altitude = new EnvironmentalEffect(-1, "Altitude");


    public ArrayList<EnvironmentalEffect> calcEnvTemp(Player p)
    {
        World world = p.getWorld();
        Location loc = p.getLocation();
        int sunlight = loc.getBlock().getLightFromSky();
        boolean inOverworld = world.getEnvironment() == World.Environment.NORMAL;
        boolean wet = p.isInWater();

        ArrayList<EnvironmentalEffect> envEffectsList = new ArrayList<>();

        // Water
        if (wet) envEffectsList.add(Wet);

        // Fire
        if (loc.getBlock().getLightFromBlocks() > 9) envEffectsList.add(Flame);

//        // Torch
//        Material mainHandItem = p.getInventory().getItemInMainHand().getType();
//        Material offHandItem = p.getInventory().getItemInOffHand().getType();
//        if (mainHandItem == Material.TORCH || offHandItem == Material.TORCH) envEffectsList.add(Torch);

        int biomeEffects = calculateBiomeTemp(loc.getBlock().getBiome());
        envEffectsList.add(new EnvironmentalEffect(biomeEffects, "Biome"));

        int clothingEffect = getClothingEffect(p.getInventory(), wet);
        envEffectsList.add(new EnvironmentalEffect(clothingEffect, "Clothing"));

        if (inOverworld)
        {
            boolean belowBlock = loc.getBlockY() < world.getHighestBlockAt(loc).getY();
            if (!belowBlock)
            {
                // Weather
                if (world.hasStorm()) envEffectsList.add(Weather);

                // Sun
                if (world.getTime() < 12000) envEffectsList.add(Sun);
            }

            int yPos = loc.getBlockY();

            // Altitude
            if (yPos > 200) envEffectsList.add(Altitude);

            // Depth
            if (yPos < -32) envEffectsList.add(Depth);

            // Indoors
            if (!wet)
            {
                if (sunlight <= 12)
                {
                    int shelterEffects = 0;
                    if (biomeEffects < 0)
                    {
                        shelterEffects = 1;
                    }
                    if (biomeEffects > 0)
                    {
                        shelterEffects = -1;
                    }
                    envEffectsList.add(new EnvironmentalEffect(shelterEffects, "Shelter"));
                }
            }
        }


        return envEffectsList;
    }

    private int calculateBiomeTemp(Biome _biome)
    {
        int output = 0;
        switch (_biome)
        {
            // Neutral
            // FOREST, OCEAN, CUSTOM, PLAINS, WINDSWEPT_HILLS, MUSHROOM_FIELDS, BEACH, DEEP_OCEAN, STONY_SHORE,
            // BIRCH_FOREST, DARK_FOREST, WINDSWEPT_FOREST, SUNFLOWER_PLAINS, WINDSWEPT_GRAVELLY_HILLS, FLOWER_FOREST
            // OLD_GROWTH_BIRCH_FOREST, MEADOW, GROVE, JAGGED_PEAKS, STONY_PEAKS, RIVER, CHERRY_GROVE
            // THE_VOID, THE_END, END_MIDLANDS, END_HIGHLANDS, END_BARRENS, SMALL_END_ISLANDS

            // Warm
            case LUSH_CAVES, SWAMP, MANGROVE_SWAMP, LUKEWARM_OCEAN, DEEP_LUKEWARM_OCEAN:
            case WINDSWEPT_SAVANNA, SAVANNA_PLATEAU, SAVANNA, JUNGLE, SPARSE_JUNGLE, BAMBOO_JUNGLE:
                output = 1;
                break;

            // Hot
            case DESERT, WARM_OCEAN, WOODED_BADLANDS, BADLANDS:
                output = 2;
                break;

            // Scorching
            case NETHER_WASTES, BASALT_DELTAS, CRIMSON_FOREST, SOUL_SAND_VALLEY, ERODED_BADLANDS, WARPED_FOREST:
                output = 3;
                break;

            // Chilly
            case DRIPSTONE_CAVES, TAIGA, OLD_GROWTH_SPRUCE_TAIGA, OLD_GROWTH_PINE_TAIGA:
                output = -1;
                break;

            // Cold
            case DEEP_COLD_OCEAN, COLD_OCEAN, SNOWY_SLOPES, SNOWY_PLAINS, SNOWY_BEACH, SNOWY_TAIGA, DEEP_DARK:
                output = -2;
                break;

            // Freezing
            case DEEP_FROZEN_OCEAN, FROZEN_OCEAN, FROZEN_RIVER, ICE_SPIKES, FROZEN_PEAKS, JAGGED_PEAKS:
                output = -3;
                break;
        }
        return output;
    }


    private int getClothingEffect(PlayerInventory _inv, boolean _wet)
    {
        int output = 0;

        String helmetKey = Util.getItemKey(_inv.getHelmet());
        String chestKey = Util.getItemKey(_inv.getChestplate());
        String pantsKey = Util.getItemKey(_inv.getLeggings());
        String bootsKey = Util.getItemKey(_inv.getBoots());

        int woolCount = 0;
        int leafCount = 0;
        int wetsuitCount = 0;

        if (helmetKey != null)
        {
            if (helmetKey.equals(RecipeManager.WOOL_HOOD_KEY)) woolCount += 1;
            if (helmetKey.equals(RecipeManager.LEAF_HELMET_KEY)) leafCount += 1;
        }

        if (chestKey != null)
        {
            if (chestKey.equals(RecipeManager.WOOL_JACKET_KEY))
            {
                woolCount += 1;
                if (!_wet)
                {
                    output += 1;
                }
            }

            if (chestKey.equals(RecipeManager.LEAF_CHEST_KEY)) leafCount += 1;
            if (chestKey.equals(RecipeManager.WETSUIT_CHEST_KEY)) wetsuitCount += 1;
        }

        if (pantsKey != null)
        {
            if (pantsKey.equals(RecipeManager.WOOL_PANTS_KEY)) woolCount += 1;
            if (pantsKey.equals(RecipeManager.WETSUIT_PANTS_KEY)) wetsuitCount += 1;
            if (pantsKey.equals(RecipeManager.LEAF_PANTS_KEY)) leafCount += 1;
        }

        if (bootsKey != null)
        {
            if (bootsKey.equals(RecipeManager.WOOL_BOOTS_KEY)) woolCount += 1;
            if (bootsKey.equals(RecipeManager.WETSUIT_BOOTS_KEY)) wetsuitCount += 1;
            if (bootsKey.equals(RecipeManager.LEAF_BOOTS_KEY)) leafCount += 1;
        }

        if (_wet)
        {
            if (wetsuitCount >= 3) output += 2;
        }
        else
        {
            if (woolCount >= 4) output += 1;
        }

        if (leafCount >= 4) output = -1;

        return output;
    }


    public static String FORMAT_TEMP_DISPLAY(int temp)
    {
        String output = "ERR";

        if (temp == 0)
        {
            output = "NEUTRAL";
        }
        else if (temp == 1)
        {
            output = ChatColor.YELLOW+"WARM";
        }
        else if (temp == 2)
        {
            output = ChatColor.GOLD + "HOT";
        }
        else if (temp > 2)
        {
            output = ChatColor.RED + "" + ChatColor.BOLD + "SCORCHING";
        }
        else if (temp == -1)
        {
            output = ChatColor.AQUA + "CHILLY";
        }
        else if (temp == -2)
        {
            output = ChatColor.BLUE + "COLD";
        }
        else
        {
            output = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "FREEZING";
        }

        return output;
    }
}
