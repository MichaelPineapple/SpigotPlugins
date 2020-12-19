package mcl.Core;

import mcl.Objects.City;
import mcl.Objects.CivPlayer;
import mcl.Objects.Request;
import mcl.Other.Toolbox;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;

public class Civ
{
    /*
    public static String pName(String _str)
    {
        return ChatColor.ITALIC + _str + ChatColor.LIGHT_PURPLE;
    }

     */

    /** PUBLIC **/
    public static ArrayList<City> CITY_LIST = new ArrayList<>();

    public static void INIT(Plugin _context)
    {
        context = _context;
    }

    public static void SAVE_CITIES()
    {
        DataManager.SAVE_CITIES(CITY_LIST);
    }

    public static void LOAD_CITIES()
    {
        CITY_LIST = DataManager.LOAD_CITIES();
        for (City c : CITY_LIST) c.ratifyAlliances();
    }

    public static void MSG(Player _p, String _str)
    {
        _p.sendMessage(ChatColor.BOLD + "" + "" + "CIV > " + ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + _str);
    }
    public static void MSG(CivPlayer _p, String _str)
    {
        Player tmp = Bukkit.getPlayer(_p.getName());
        if (tmp != null) MSG(tmp, _str);
    }

    public static Request GET_PENDING_REQUEST(Player _p)
    {
        Request output = null;
        for (Request q: pendingAllianceRequests)
        {
            if (q.getReciever().getUniqueId().toString().equals(_p.getUniqueId().toString())) output = q;
        }
        pendingAllianceRequests.remove(output);
        return output;
    }

    public static void REQUEST_ALLIANCE(Player _sender, Player _receiver)
    {
        // clear existing request involving either sender or receiver
        ArrayList<Request> qlust = new ArrayList<>();
        for (Request q: pendingAllianceRequests)
        {
            if (q.getReciever().getUniqueId().toString().equals(_receiver.getUniqueId().toString())) qlust.add(q);
            else if (q.getSender().getUniqueId().toString().equals(_sender.getUniqueId().toString())) qlust.add(q);
        }
        for (Request q: qlust) pendingAllianceRequests.remove(q);

        pendingAllianceRequests.add(new Request(_receiver, _sender));
    }

    public static boolean CAN_BUILD(Player _p, Block _b)
    {
        boolean output = true;
        Chunk c = _b.getChunk();
        City chunkCity = GET_CHUNK_OWNER(c);
        if (chunkCity != null)
        {
            CivPlayer civP = new CivPlayer(_p);
            if (!chunkCity.isCitizen(civP))
            {
                if (!chunkCity.isAllianceCitizen(civP))
                {
                    output = false;
                    MSG(_p, "This territory is owned by "+chunkCity.getDisplayName());
                }
            }
        }
        return output;
    }

    public static City GET_CITY_FROM_NAME(String _CityName)
    {
        City output = null;
        for (City n: CITY_LIST)
        {
            if (ChatColor.stripColor(n.getDisplayName()).equals(_CityName)) output = n;
        }
        return output;
    }

    public static City GET_CHUNK_OWNER(Chunk _chunk)
    {
        City output = null;
        for (City c: CITY_LIST)
        {
            if (c.hasClaimed(_chunk)) output = c;
        }
        return output;
    }

    public static City GET_CITIZENSHIP(Player _p)
    {
        return GET_CITIZENSHIP(new CivPlayer(_p));
    }
    public static City GET_CITIZENSHIP(CivPlayer _p)
    {
        City output = null;
        for (City n: CITY_LIST)
        {
            if (n.isCitizen(_p)) output = n;
        }
        return output;
    }

    public static City GET_MAYOR_CITY(Player _p)
    {
        City output = null;
        for (City n: CITY_LIST)
        {
            if (n.getMayor().equals(_p)) output = n;
        }
        return output;
    }

    public static void HIGHLIGHT_ALL_TERRITORIES(Player _p)
    {
        for (City c : CITY_LIST)
        {
            Material border = Material.BLACK_CONCRETE;
            CivPlayer civP = new CivPlayer(_p);
            if (c.isCitizen(civP)) border = Material.CYAN_CONCRETE;
            else if (c.isAllianceCitizen(civP)) border = Material.LIME_CONCRETE;
            highlightTerritory(_p, c, border);
        }
    }

    public static void LOAD_PLAYERTABLE()
    {
        playerTable = DataManager.LOAD_PLAYERTABLE();
    }

    public static void SAVE_PLAYERTABLE()
    {
        DataManager.SAVE_PLAYERTABLE(playerTable);
    }

    public static void ON_PLAYER_JOIN(Player _p)
    {
        if (GET_PLAYER(_p.getName()) == null) registerNewPlayer(_p);
    }

    public static CivPlayer GET_PLAYER(String _name)
    {
        CivPlayer output = null;
        for (CivPlayer p: playerTable)
        {
            if (p.getName().equals(_name)) output = p;
        }
        return output;
    }

    public static ArrayList<CivPlayer> COPY_PLAYERTABLE()
    {
        return new ArrayList<>(playerTable);
    }

    public static ArrayList<String> GET_PLAYERTABLE_NAMES()
    {
        ArrayList<String> output = new ArrayList<>();
        for (CivPlayer p: playerTable) output.add(p.getName());
        return output;
    }

    public static ArrayList<String> GET_CITY_NAMES()
    {
        ArrayList<String> output = new ArrayList<>();
        for (City c : CITY_LIST) output.add(c.getName());
        return output;
    }


    /*
    public static CivPlayer GET_PLAYER_FROM_UUID(String _uuid)
    {
        CivPlayer output = null;
        for (CivPlayer p: playerTable)
        {
            if (p.getUUID().equals(_uuid)) output = p;
        }
        return output;
    }
    public static CivPlayer GET_PLAYER_FROM_UUID(UUID _uuid)
    {
        CivPlayer output = null;
        for (CivPlayer p: playerTable)
        {
            if (p.getUUID().equals(_uuid.toString())) output = p;
        }
        return output;
    }

     */




    /** PRIVATE **/
    private static ArrayList<Request> pendingAllianceRequests = new ArrayList<>();
    private static Plugin context;
    private static ArrayList<CivPlayer> playerTable = new ArrayList<>();

    private static void registerNewPlayer(Player _p)
    {
        playerTable.add(new CivPlayer(_p.getUniqueId().toString(), _p.getName()));
    }

    private static void highlightTerritory(Player _p, City _city, Material _borderMaterial)
    {
        ArrayList<Chunk> landTmp = _city.copyLand();
        for (Chunk c: landTmp) highlightChunk(_p, c, _borderMaterial);
    }

    private static Location getRelativeLoc(Location _loc, int yOffset)
    {
        return _loc.getBlock().getRelative(0, yOffset, 0).getLocation();
    }

    private static boolean isIntangable(Location _loc)
    {
        Material m = _loc.getBlock().getType();
        boolean output = !m.isSolid();
        if (Toolbox.IS_LEAF(m) || Toolbox.IS_BANNER(m) || m == Material.BAMBOO ||
                m == Material.CACTUS || Toolbox.IS_FENCE(m) || m == Material.LANTERN) output = true;
        return output;
    }

    private static Location getBestBorderBlock(Location _loc)
    {
        Location crawl = _loc.clone();
        int count = 0;
        int max = 20;
        if (isIntangable(crawl))
        {
            while (isIntangable(crawl))
            {
                crawl = getRelativeLoc(crawl, -1);
                count++;
                if (count > max) break;
            }
        }
        else
        {
            while (!isIntangable(crawl))
            {
                crawl = getRelativeLoc(crawl, 1);
                count++;
                if (count > max) break;
            }
            crawl = getRelativeLoc(crawl, -1);
        }

        return crawl;
    }

    private static void highlightChunk(Player _p, Chunk _c, Material _borderMaterial)
    {
        Location chunkLocation = _c.getBlock(0,0,0).getLocation();
        Location playerLocation = _p.getLocation();
        double xDistance = Math.abs(chunkLocation.getX() - playerLocation.getX());
        double zDistance = Math.abs(chunkLocation.getZ() - playerLocation.getZ());
        if (xDistance < 256 && zDistance < 256)
        {
            int playerY = playerLocation.getBlockY()-1;
            ArrayList<Location> edgeList = new ArrayList<>();
            for (int i = 0; i < 16; i++)
            {
                edgeList.add(_c.getBlock(i, playerY, 0).getLocation());
                edgeList.add(_c.getBlock(i, playerY, 15).getLocation());
                edgeList.add(_c.getBlock(0, playerY, i).getLocation());
                edgeList.add(_c.getBlock(15, playerY, i).getLocation());
            }

            ArrayList<Location> surfaceLocList = new ArrayList<>();
            for (Location l : edgeList) surfaceLocList.add(getBestBorderBlock(l));

            BlockData blokDat = _borderMaterial.createBlockData();
            for (Location l : surfaceLocList) _p.sendBlockChange(l, blokDat);

            Runnable clearHighlight = () ->
            {
                for (Location l : surfaceLocList) _p.sendBlockChange(l, l.getWorld().getBlockAt(l).getBlockData());
            };
            Toolbox.SCHEDULE_TASK(context, clearHighlight, 100);
        }
    }





}
