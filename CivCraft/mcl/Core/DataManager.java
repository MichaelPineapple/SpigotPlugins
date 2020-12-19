package mcl.Core;
import mcl.Objects.City;
import mcl.Objects.CivPlayer;
import mcl.Other.Toolbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class DataManager
{
    /** PUBLIC **/
    public static final String _CITIZENS_DELIM_ = "$citizens:";
    public static final String _OFFICERS_DELIM_ = "$officers:";
    public static final String _ALLIANCES_DELIM_ = "$alliances:";

    /** PACKAGE PRIVATE **/
    static ArrayList<City> LOAD_CITIES()
    {
        ArrayList<City> output = new ArrayList<>();
        File[] files = new File(_CITY_DATA_FOLDER_).listFiles();
        for (File f: files)
        {
            City tmp = loadCity(f.getAbsolutePath());
            if (tmp != null) output.add(tmp);
        }
        return output;
    }

    static void SAVE_CITIES(ArrayList<City> _citiesList)
    {
        File folder = new File(_CITY_DATA_FOLDER_);
        if (folder.exists())
        {
            try { FileUtils.cleanDirectory(folder); }
            catch (IOException e) { e.printStackTrace(); }
        }
        for (City c: _citiesList) DataManager.saveCity(c);
    }

    static ArrayList<CivPlayer> LOAD_PLAYERTABLE()
    {
        ArrayList<CivPlayer> output = new ArrayList<>();
        String[] data = Toolbox.LOAD_DATA(_PLAYERTABLE_FILE_);
        for (int i = 0; i < data.length; i++)
        {
            String[] parse = data[i].split(" ");
            if (parse.length >= 2) output.add(new CivPlayer(parse[0], parse[1]));
            else System.out.println("CIV> Invalid entry in playertable!");
        }
        return output;
    }

    static void SAVE_PLAYERTABLE(ArrayList<CivPlayer> _playerTable)
    {
        String data = "";
        for (CivPlayer p: _playerTable) data += (p.getUUID() + " " + p.getName() + "\n");
        if (data.length() > 0) data = data.substring(0, data.length()-1);
        Toolbox.SAVE_DATA(_PLAYERTABLE_FILE_, data);
    }

    /** PRIVATE **/
    private static final String _DATA_FOLDER_ = "./plugins/CivCraft/";
    private static final String _CITY_DATA_FOLDER_ = _DATA_FOLDER_ + "cities/";
    private static final String _PLAYERTABLE_FILE_ = _DATA_FOLDER_ + "playertable";

    private static void saveCity(City _city)
    {
        Toolbox.SAVE_DATA(_CITY_DATA_FOLDER_ + _city.getName()+".city", _city.toData());
    }

    private static int findFirstIndexOf(String[] _array, String _str)
    {
        int output = -1;
        for (int i = _array.length-1; i > 0; i--)
        {
            if (_array[i].equals(_str)) output = i;
        }
        return output;
    }

    private static City loadCity(String _filename)
    {
        City output = null;
        String[] data = Toolbox.LOAD_DATA(_filename);
        int citizensDelimIndex = findFirstIndexOf(data, _CITIZENS_DELIM_);
        int officersDelimIndex = findFirstIndexOf(data, _OFFICERS_DELIM_);
        int alliancesDelimIndex = findFirstIndexOf(data, _ALLIANCES_DELIM_);

        if (data.length >= 5 && citizensDelimIndex != -1 && alliancesDelimIndex != -1)
        {
            String cityName = data[0];
            String mayorUUID = data[1];
            String mayorName = data[2];

            /** chunks **/
            ArrayList<Chunk> tmpChunksList = new ArrayList<>();
            for (int i = 3; i < citizensDelimIndex; i++)
            {
                try
                {
                    String[] parse = data[i].split(" ");
                    int x = Integer.parseInt(parse[0]);
                    int z = Integer.parseInt(parse[1]);
                    UUID worldId = UUID.fromString(parse[2]);
                    tmpChunksList.add(Bukkit.getWorld(worldId).getChunkAt(x, z));
                }
                catch (Exception ex) { System.out.println("Error: Failed to load territory!"); }
            }

            /** citizens **/
            ArrayList<CivPlayer> tmpCitizensList = new ArrayList<>();
            for (int i = citizensDelimIndex + 1; i < officersDelimIndex; i++)
            {
                try
                {
                    String[] parse = data[i].split(" ");
                    String id = parse[0];
                    String pName = parse[1];
                    if (!id.equals(mayorUUID)) tmpCitizensList.add(new CivPlayer(id, pName));
                }
                catch (Exception ex) { System.out.println("Error: Failed to load citizen!"); }
            }


            /** officers **/
            ArrayList<CivPlayer> tmpOfficersList = new ArrayList<>();
            for (int i = officersDelimIndex + 1; i < alliancesDelimIndex; i++)
            {
                try
                {
                    String[] parse = data[i].split(" ");
                    String id = parse[0];
                    String pName = parse[1];
                    if (!id.equals(mayorUUID)) tmpOfficersList.add(new CivPlayer(id, pName));
                }
                catch (Exception ex) { System.out.println("Error: Failed to load officer!"); }
            }

            /** alliances **/
            ArrayList<String> tmpAlliancesList = new ArrayList<>();
            for (int i = alliancesDelimIndex + 1; i < data.length; i++)
            {
                tmpAlliancesList.add(data[i]);
            }

            output = new City(new CivPlayer(mayorUUID, mayorName), cityName);
            output.addLand(tmpChunksList);
            output.addCitizens(tmpCitizensList);
            output.addAllianceStrings(tmpAlliancesList);
            output.addOfficers(tmpOfficersList);
        }
        else System.out.println("Invalid format!");

        return output;
    }

}


/** LEGACY **/
/*
    public static City LoadFromData(String filename)
    {
        City city = null;
        try
        {
            File file = new File(filename); //creates a new file instance
            FileReader fr = new FileReader(file);   //reads the file
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream

            String name = br.readLine();
            String mayorID = br.readLine();
            String mayorName = br.readLine();
            CivPlayer mayor = new CivPlayer(mayorID, mayorName);

            String line;
            ArrayList<Chunk> territory = new ArrayList<>();
            while(!(line = br.readLine()).equalsIgnoreCase("$citizens:"))
            {
                try
                {
                    String[] parse = line.split(" ");
                    int x = Integer.parseInt(parse[0]);
                    int z = Integer.parseInt(parse[1]);
                    UUID id = UUID.fromString(parse[2]);
                    territory.add(Bukkit.getWorld(id).getChunkAt(x, z));
                }
                catch (Exception ex) { ex.printStackTrace(); }
            }

            br.readLine(); // skip mayor
            ArrayList<CivPlayer> citizens = new ArrayList<>();
            while(!(line = br.readLine()).equalsIgnoreCase("$alliances:"))
            {
                try
                {
                    String[] parse = line.split(" ");
                    String id = parse[0];
                    String pName = parse[1];
                    citizens.add(new CivPlayer(id, pName));
                }
                catch (Exception ex) { ex.printStackTrace(); }
            }

            ArrayList<String> alliances = new ArrayList<>();
            while((line = br.readLine()) != null) alliances.add(line);

            br.close();

            city = new City(mayor, name);
            city.addLand(territory);
            city.addCitizen(citizens);
            city.addAllianceStrings(alliances);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Bukkit.broadcastMessage("ERROR LOADING CITY DATA!");
        }

        return city;
    }


 */


        /*
        try
        {
            File f = new File(_CITY_DATA_FOLDER_ + ChatColor.stripColor(saveCity.getName())+".city");
            f.getParentFile().mkdirs();
            f.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(f.getCanonicalPath()));
            writer.write(saveCity.asString());
            writer.close();
        }
        catch (IOException e)
        {
           e.printStackTrace();
           Bukkit.broadcastMessage("ERROR SAVING CITY DATA!");
        }

         */