package mcl.Other;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;

public class Toolbox
{
    public static int SCHEDULE_TASK(Plugin _context, Runnable _func, long _delay)
    {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(_context, _func, _delay);
    }

    public static boolean IS_BANNER(Material m)
    {
        return  (m == Material.BLACK_BANNER ||
                m == Material.ORANGE_BANNER ||
                m == Material.MAGENTA_BANNER ||
                m == Material.WHITE_BANNER ||
                m == Material.LIGHT_BLUE_BANNER ||
                m == Material.RED_BANNER ||
                m == Material.GREEN_BANNER ||
                m == Material.BROWN_BANNER ||
                m == Material.BLUE_BANNER ||
                m == Material.YELLOW_BANNER||
                m == Material.LIME_BANNER ||
                m == Material.PINK_BANNER ||
                m == Material.GRAY_BANNER ||
                m == Material.LIGHT_GRAY_BANNER ||
                m == Material.CYAN_BANNER ||
                m == Material.PURPLE_BANNER);
    }

    public static boolean IS_FENCE(Material m)
    {
        return (m == Material.ACACIA_FENCE || m == Material.BIRCH_FENCE || m == Material.OAK_FENCE || m == Material.CRIMSON_FENCE ||
                m == Material.JUNGLE_FENCE || m == Material.DARK_OAK_FENCE || m == Material.NETHER_BRICK_FENCE || m == Material.SPRUCE_FENCE ||
                m == Material.WARPED_FENCE || m == Material.ACACIA_FENCE_GATE || m == Material.BIRCH_FENCE_GATE || m == Material.OAK_FENCE_GATE ||
                m == Material.CRIMSON_FENCE_GATE || m == Material.JUNGLE_FENCE_GATE || m == Material.DARK_OAK_FENCE_GATE ||
                m == Material.SPRUCE_FENCE_GATE || m == Material.WARPED_FENCE_GATE || m == Material.ANDESITE_WALL || m == Material.BRICK_WALL ||
                m == Material.BLACKSTONE_WALL || m == Material.END_STONE_BRICK_WALL || m == Material.COBBLESTONE_WALL ||
                m == Material.DIORITE_WALL || m == Material.GRANITE_WALL || m == Material.MOSSY_COBBLESTONE_WALL ||
                m == Material.MOSSY_STONE_BRICK_WALL || m == Material.NETHER_BRICK_WALL || m == Material.POLISHED_BLACKSTONE_BRICK_WALL ||
                m == Material.POLISHED_BLACKSTONE_WALL || m == Material.PRISMARINE_WALL || m == Material.RED_NETHER_BRICK_WALL ||
                m == Material.RED_SANDSTONE_WALL || m == Material.SANDSTONE_WALL || m == Material.STONE_BRICK_WALL);
    }

    public static boolean IS_LEAF(Material m)
    {
        return (m == Material.ACACIA_LEAVES || m == Material.BIRCH_LEAVES || m == Material.OAK_LEAVES ||
                m == Material.DARK_OAK_LEAVES || m == Material.JUNGLE_LEAVES || m == Material.SPRUCE_LEAVES);
    }

    public static void SAVE_DATA(String _filename, String _data)
    {
        try
        {
            File f = new File(_filename);
            f.getParentFile().mkdirs();
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f.getCanonicalPath()));
            writer.write(_data);
            writer.close();
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    public static String[] LOAD_DATA(String _filename)
    {
        String[] output;
        try
        {
            File f = new File(_filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<String> tmpList = new ArrayList<>();
            String line;
            while((line = br.readLine()) != null) tmpList.add(line);
            output = new String[tmpList.size()];
            tmpList.toArray(output);
            br.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            output = null;
        }
        return output;
    }
}
