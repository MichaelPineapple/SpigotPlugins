package mcl.Objects;

import mcl.Core.Civ;
import mcl.Core.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;

import java.util.ArrayList;

public class City
{
    public City(CivPlayer _mayor, String _name)
    {
        citizens = new ArrayList<>();
        citizens.add(_mayor);
        officers = new ArrayList<>();
        officers.add(_mayor);
        territory = new ArrayList<>();
        alliances = new ArrayList<>();
        allianceStrings = new ArrayList<>();
        mayor = _mayor;
        name = _name;
    }

    /** Other **/
    private String name;
    //private Location tpp;
    public String getDisplayName() { return ChatColor.AQUA + name + ChatColor.LIGHT_PURPLE; }
    public String getName() {return name;}
    public void setName(String _name) { name = _name; }
    public int getPopulation() { return citizens.size(); }
    public int getTerritoryCount() { return territory.size(); }
    public int getMaxTerritory() { return getPopulation() * 10; }
    public int getRemainingTerritory() { return getMaxTerritory() - getTerritoryCount(); }
    public boolean isAllianceCitizen(CivPlayer _p)
    {
        boolean output = false;
        for (City ally : alliances)
        {
            if (ally.isCitizen(_p)) output = true;
        }
        return output;
    }

    /** Mayor **/
    private CivPlayer mayor;
    public CivPlayer getMayor() { return mayor; }
    public void setMayor(CivPlayer _mayor) { mayor = _mayor; }

    /** Territory **/
    private ArrayList<Chunk> territory;
    public void addLand(Chunk _chunk) { territory.add(_chunk); }
    public void addLand(ArrayList<Chunk> _chunks) { territory.addAll(_chunks); }
    public void delLand(Chunk _chunk) { territory.remove(_chunk); }
    public ArrayList<Chunk> copyLand() { return new ArrayList<>(territory); }
    public boolean hasClaimed(Chunk _chunk)
    {
        boolean output = false;
        for (Chunk c: territory)
        {
            if (_chunk.getWorld().getUID().toString().equals(c.getWorld().getUID().toString()) &&
                    (_chunk.getX() == c.getX()) && (_chunk.getZ() == c.getZ()))
            {
                output = true;
            }
        }
        return output;
    }

    /** Citizens **/
    private ArrayList<CivPlayer> citizens;
    public ArrayList<CivPlayer> copyCitizens() { return new ArrayList<>(citizens); }
    public void addCitizen(CivPlayer _p) { citizens.add(_p); }
    public void addCitizens(ArrayList<CivPlayer> _players){ citizens.addAll(_players); }
    public void delCitizen(CivPlayer _p)
    {
        int indexToDelete = -1;
        for (int i = 0; i < citizens.size(); i++)
        {
            if (citizens.get(i).getUUID().equals(_p.getUUID())) indexToDelete = i;
        }
        if (indexToDelete > -1) citizens.remove(indexToDelete);
    }

    /*
    public boolean isCitizen(Player _p)
    {
        boolean output = false;
        for (CivPlayer p : citizens)
        {
            if (p.getUUID().equals(_p.getUniqueId().toString())) output = true;
        }
        return output;
    }
     */

    public boolean isCitizen(CivPlayer _p)
    {
        boolean output = false;
        for (CivPlayer p : citizens)
        {
            if (p.equals(_p)) output = true;
        }
        return output;
    }

    /** Officers **/
    private ArrayList<CivPlayer> officers;
    public ArrayList<CivPlayer> copyOfficers() { return new ArrayList<>(officers); }
    public void addOfficer(CivPlayer _p) { officers.add(_p); }
    public void addOfficers(ArrayList<CivPlayer> _players){ officers.addAll(_players); }
    public void delOfficer(CivPlayer _p)
    {
        int indexToDelete = -1;
        for (int i = 0; i < officers.size(); i++)
        {
            if (officers.get(i).getUUID().equals(_p.getUUID())) indexToDelete = i;
        }
        if (indexToDelete > -1) officers.remove(indexToDelete);
    }
    public boolean isOfficer(CivPlayer _p)
    {
        boolean output = false;
        for (CivPlayer p : officers)
        {
            if (p.equals(_p)) output = true;
        }
        return output;
    }

    /** Alliances **/
    private ArrayList<City> alliances;
    private ArrayList<String> allianceStrings;
    public void addAlliance(City _city) { alliances.add(_city); }
    public void delAlliance(City _city) { alliances.remove(_city); }
    public ArrayList<City> copyAlliances() { return new ArrayList<>(alliances); }
    public void addAllianceStrings(ArrayList<String> _list) { allianceStrings.addAll(_list); }
    public void ratifyAlliances()
    {
        for (String allyStr: allianceStrings)
        {
            City tmp = Civ.GET_CITY_FROM_NAME(allyStr);
            if (tmp != null) addAlliance(tmp);
        }
    }

    /** Data **/
    public String toData()
    {
        String chunksStr = "";
        String citizensStr = DataManager._CITIZENS_DELIM_+"\n";
        String officersStr = DataManager._OFFICERS_DELIM_+"\n";
        String alliancesStr = DataManager._ALLIANCES_DELIM_+"\n";

        for (Chunk chunk:territory)
        {
            chunksStr += (chunk.getX() + " " +chunk.getZ() + " " + chunk.getWorld().getUID().toString() + "\n");
        }
        for (CivPlayer p : citizens)
        {
            citizensStr += (p.getUUID() + " " + p.getName() + "\n");
        }
        for (CivPlayer p : officers)
        {
            officersStr += (p.getUUID() + " " + p.getName() + "\n");
        }
        for(City c:alliances)
        {
            alliancesStr += c.getDisplayName() + "\n";
        }

        if (alliancesStr.length() > 0) alliancesStr = alliancesStr.substring(0,  alliancesStr.length()-1);

        return  name + "\n" + mayor.getUUID() + "\n" + mayor.getName() + "\n" + chunksStr + citizensStr + officersStr + alliancesStr;
    }

}
