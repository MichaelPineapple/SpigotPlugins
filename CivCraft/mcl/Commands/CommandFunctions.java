package mcl.Commands;

import mcl.Core.Civ;
import mcl.Objects.City;
import mcl.Objects.CivPlayer;
import mcl.Objects.Request;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class CommandFunctions
{
    /** USER COMMAND FUNCTIONS **/
    protected static void addOfficer(Player _p, String _officerName)
    {
        CivPlayer officer = Civ.GET_PLAYER(_officerName);
        if (officer != null)
        {
            City n = Civ.GET_MAYOR_CITY(_p);
            if (n != null)
            {
                if (!n.isOfficer(officer))
                {
                    n.addOfficer(officer);
                    Civ.MSG(_p, officer.getName() + " is now an officer of " + n.getDisplayName());
                    Civ.MSG(officer,"You have been promoted to the rank of officer in " + n.getDisplayName());
                }
                else Civ.MSG(_p, officer.getName() + " is already an officer of " + n.getDisplayName());
            }
            else Civ.MSG(_p, "You are not the mayor of any city.");
        }
        else Civ.MSG(_p, "Player not found!");
    }

    protected static void delOfficer(Player _p, String _officerName)
    {
        CivPlayer officer = Civ.GET_PLAYER(_officerName);
        if (officer != null)
        {
            City n = Civ.GET_MAYOR_CITY(_p);
            if (n != null)
            {
                if (n.isOfficer(officer))
                {
                    n.delOfficer(officer);
                    Civ.MSG(_p, officer.getName() + " is no longer an officer of " + n.getDisplayName());
                    Civ.MSG(officer,"You are no longer an officer of " + n.getDisplayName());
                }
                else Civ.MSG(_p, officer.getName() + " is not an officer of " + n.getDisplayName());
            }
            else Civ.MSG(_p, "You are not the mayor of any city.");
        }
        else Civ.MSG(_p, "Player not found!");
    }

    protected static void renameCity(Player _p, String _cName)
    {
        City n = Civ.GET_MAYOR_CITY(_p);
        if (n != null)
        {
            if (Civ.GET_CITY_FROM_NAME(_cName) == null)
            {
                n.setName(_cName);
                Civ.MSG(_p, "You city has been renamed to "+n.getDisplayName());
            }
            else Civ.MSG(_p, "A city already has that name");
        }
        else Civ.MSG(_p, "You are not the mayor of any city.");
    }

    protected static void leaveAlliance(Player _p, String _cName)
    {
        City n = Civ.GET_MAYOR_CITY(_p);
        if (n != null)
        {
            City ally = Civ.GET_CITY_FROM_NAME(_cName);
            if (ally != null)
            {
                Player allyMayor = Bukkit.getPlayer(UUID.fromString(ally.getMayor().getUUID()));
                if (allyMayor != null)
                {
                    n.delAlliance(ally);
                    ally.delAlliance(n);
                    Civ.MSG(allyMayor, n.getDisplayName() + " has terminated the alliance.");
                    Civ.MSG(_p, "Alliance terminated with "+ally.getDisplayName());
                }
            }
            else Civ.MSG(_p, "City not found!");
        }
        else Civ.MSG(_p, "You are not the mayor of any city.");
    }

    protected static void addAlly(Player _p, String _cName)
    {
        City n = Civ.GET_MAYOR_CITY(_p);
        if (n != null)
        {
            City ally = Civ.GET_CITY_FROM_NAME(_cName);
            if (ally != null)
            {
                Player reciever = Bukkit.getPlayer(UUID.fromString(ally.getMayor().getUUID()));
                if (reciever != null)
                {
                    Civ.REQUEST_ALLIANCE(_p, reciever);
                    Civ.MSG(reciever, n.getDisplayName() + " has requested to form an alliance. (Use /allyAccept or /allyDeny)");
                    Civ.MSG(_p, "Alliance request sent to " + ally.getDisplayName());
                }
                else Civ.MSG(_p, "The mayor of "+ally.getDisplayName()+" ("+ally.getMayor().getName()+") was not found!");
            }
            else Civ.MSG(_p, "City not found!");
        }
        else Civ.MSG(_p, "You are not the mayor of any city.");
    }

    protected static void allyAccept(Player _p)
    {
        Request q = Civ.GET_PENDING_REQUEST(_p);
        if (q != null)
        {
            City recieverCity = Civ.GET_MAYOR_CITY(_p);
            if (recieverCity != null)
            {
                Player sender = q.getSender();
                City senderCity = Civ.GET_MAYOR_CITY(sender);
                if (senderCity != null)
                {
                    recieverCity.addAlliance(senderCity);
                    senderCity.addAlliance(recieverCity);
                    Civ.MSG(_p, recieverCity.getDisplayName()+" is now in an alliance with "+senderCity.getDisplayName());
                    Civ.MSG(sender, senderCity.getDisplayName()+" is now in an alliance with "+recieverCity.getDisplayName());
                }
                else Civ.MSG(_p, "The requesting player is not the mayor of any city.");
            }
            else Civ.MSG(_p, "You are not the mayor of any city.");
        }
        else Civ.MSG(_p, "You have no pending requests.");
    }

    protected static void allyDeny(Player _p)
    {
        Request q = Civ.GET_PENDING_REQUEST(_p);
        if (q != null)
        {
            City recieverCity = Civ.GET_MAYOR_CITY(_p);
            if (recieverCity != null)
            {
                Player sender = q.getSender();
                City senderCity = Civ.GET_MAYOR_CITY(sender);
                if (senderCity != null)
                {
                    Civ.MSG(_p, recieverCity.getDisplayName()+" has denied the alliance request from "+senderCity.getDisplayName());
                    Civ.MSG(sender, recieverCity.getDisplayName()+" has denied your alliance request.");
                }
                else Civ.MSG(_p, "The requesting player is not the mayor of any city.");
            }
            else Civ.MSG(_p, "You are not the mayor of any city.");
        }
        else Civ.MSG(_p, "You have no pending requests.");
    }

    protected static void cityList(Player _p)
    {
        String reply = "Cities: ";
        for (City c : Civ.CITY_LIST) reply += c.getDisplayName() + ", ";
        Civ.MSG(_p, reply);
    }

    protected static void addCitizen(Player _p, String _citizenName)
    {
        CivPlayer citizen = Civ.GET_PLAYER(_citizenName);
        if (citizen != null)
        {
            City n = Civ.GET_CITIZENSHIP(_p);
            if (n != null)
            {
                if (n.isOfficer(new CivPlayer(_p)))
                {
                    City priorCitizenship = Civ.GET_CITIZENSHIP(citizen);
                    if (priorCitizenship == null)
                    {
                        n.addCitizen(citizen);
                        Civ.MSG(_p, citizen.getName() + " is now a citizen of " + n.getDisplayName());
                        Civ.MSG(citizen, "You are now a citizen of " + n.getDisplayName());
                    }
                    else Civ.MSG(_p, citizen.getName() + " is already a citizen of " + priorCitizenship.getDisplayName());
                }
                else Civ.MSG(_p, "You must be an officer to perform this action");
            }
            else Civ.MSG(_p, "You are not the mayor of any city.");
        }
        else Civ.MSG(_p, "Player not found!");
    }

    protected static void leaveCity(Player _p)
    {
        City n = Civ.GET_CITIZENSHIP(_p);
        if (n != null)
        {
            if (!n.getMayor().equals(_p))
            {
                n.delCitizen(new CivPlayer(_p));
                Civ.MSG(_p, "You are no longer a citizen of "+n.getDisplayName());
            }
            else Civ.MSG(_p, "The mayor cannot renounce citizenship. You must resign as mayor first.");
        }
        else Civ.MSG(_p, "You are not a citizen of any city!");
    }

    protected static void mayorResign(Player _p, String _nextMayorName)
    {
        City n = Civ.GET_MAYOR_CITY(_p);
        if (n != null)
        {
            CivPlayer nextMayor = Civ.GET_PLAYER(_nextMayorName);
            if (nextMayor != null)
            {
               if (n.isCitizen(nextMayor))
               {
                   n.setMayor(nextMayor);
                   Civ.MSG(_p, nextMayor.getName()+" is now mayor of "+n.getDisplayName());
                   Civ.MSG(nextMayor, "You are now the mayor of "+n.getDisplayName());
               }
               else Civ.MSG(_p, nextMayor.getName()+" is not a citizen of "+n.getDisplayName());
            }
            else Civ.MSG(_p, "Player '"+_nextMayorName+"' not found!");
        }
        else Civ.MSG(_p, "You are not the mayor of any city.");
    }

    protected static void disbandCity(Player _p)
    {
        City n = Civ.GET_MAYOR_CITY(_p);
        if (n != null)
        {
            if (n.getPopulation() <= 1)
            {
                ArrayList<City> alliancelist = n.copyAlliances();
                for (City ally : alliancelist) ally.delAlliance(n);
                Civ.MSG(_p, n.getDisplayName()+" has been disbanded");
                Civ.CITY_LIST.remove(n);
            }
            else Civ.MSG(_p, "Please enter a player to become mayor. Use /help for usage.");
        }
        else Civ.MSG(_p, "You are not the mayor of any city.");
    }

    protected static void removeCitizen(Player _p, String _citizenName)
    {
        CivPlayer citizen = Civ.GET_PLAYER(_citizenName);
        if (citizen != null)
        {
            City n = Civ.GET_MAYOR_CITY(_p);
            if (n != null)
            {
                n.delCitizen(citizen);
                Civ.MSG(_p, citizen.getName() + " is no longer a citizen of " + n.getDisplayName());
                Civ.MSG(citizen,"You are no longer a citizen of " + n.getDisplayName());
            }
            else Civ.MSG(_p, "You are not the mayor of any city.");
        }
        else Civ.MSG(_p, "Player not found!");
    }

    protected static void settle(Player _p, String _name)
    {
        City n = Civ.GET_MAYOR_CITY(_p);
        if (n == null)
        {
            City citizenship = Civ.GET_CITIZENSHIP(_p);
            if (citizenship == null)
            {
                if (Civ.GET_CITY_FROM_NAME(_name) == null)
                {
                    City tmp = new City(new CivPlayer(_p), _name);
                    Civ.CITY_LIST.add(tmp);
                    claimLand(_p);
                    Civ.MSG(_p, "You are now the mayor of " + tmp.getDisplayName());
                }
                else Civ.MSG(_p, "City already exists!");
            }
            else Civ.MSG(_p, "You cannot settle a new city because you are already a citizen of "+citizenship.getDisplayName());
        }
        else Civ.MSG(_p, "You are already the mayor of " + n.getDisplayName());
    }

    protected static void unclaimLand(Player _p)
    {
        City n = Civ.GET_MAYOR_CITY(_p);
        if (n != null)
        {
            Chunk c = _p.getLocation().getChunk();
            if (n.hasClaimed(c))
            {
                n.delLand(c);
                Civ.MSG(_p, n.getDisplayName() + " has relinquished control of this chunk");
            }
            else Civ.MSG(_p, "Your city does not own this chunk!");
            Civ.HIGHLIGHT_ALL_TERRITORIES(_p);
        }
        else Civ.MSG(_p, "You are not the mayor of any city.");
    }

    protected static void claimLand(Player _p)
    {
        City n = Civ.GET_CITIZENSHIP(_p);
        if (n != null)
        {
            if (n.isOfficer(new CivPlayer(_p)))
            {
                Chunk c = _p.getLocation().getChunk();
                if (!n.hasClaimed(c))
                {
                    City owner = Civ.GET_CHUNK_OWNER(c);
                    if (owner == null)
                    {
                        if (n.getRemainingTerritory() > 0)
                        {
                            n.addLand(c);
                            Civ.MSG(_p, n.getDisplayName() + " now owns chunk");
                        }
                        else Civ.MSG(_p, "You need a larger population to claim more land!");
                    }
                    else Civ.MSG(_p, "This chunk is owned by " + owner.getDisplayName());
                }
                else Civ.MSG(_p, n.getDisplayName() + " has already claimed this chunk.");
                Civ.HIGHLIGHT_ALL_TERRITORIES(_p);
            }
            else Civ.MSG(_p, "You must be an officer to perform this action");
        }
        else Civ.MSG(_p, "You are not a citizen of any city!");
    }

    protected static void showLand(Player _p)
    {
        Civ.HIGHLIGHT_ALL_TERRITORIES(_p);
        Civ.MSG(_p, "Showing chunks owned by cities.");
    }

    protected static void cityStats(Player _p)
    {
        City n = Civ.GET_CITIZENSHIP(_p);
        if (n != null)
        {
            String alliesStr = "Allied Cities: ";
            ArrayList<City> allyList = n.copyAlliances();
            for (City c : allyList) alliesStr += c.getDisplayName() + ", ";

            String citizensStr = "Citizens: ";
            ArrayList<CivPlayer> citizensList = n.copyCitizens();
            for (CivPlayer citizen : citizensList) citizensStr += citizen.getName() + ", ";

            String officersStr = "Officers: ";
            ArrayList<CivPlayer> officersList = n.copyOfficers();
            for (CivPlayer officer : officersList) officersStr += officer.getName() + ", ";

            String cityInfo = "Name: " + n.getDisplayName() + "\n" + "Claimed Chunks: " + n.getTerritoryCount() + "/" + n.getMaxTerritory() +
                    "\n" + citizensStr + "\n" + alliesStr + "\n" + officersStr;
            Civ.MSG(_p, cityInfo);
        }
        else Civ.MSG(_p, "You are not a citizen of any city.");
    }

    /** ADMIN COMMAND FUNCTIONS **/
    static class Admin
    {
        protected static void clearData(CommandSender _sender)
        {
            Civ.CITY_LIST.clear();
            _sender.sendMessage("CivCraft data has been cleared");
        }

        protected static void loadData(CommandSender _sender)
        {
            Civ.LOAD_CITIES();
            Civ.LOAD_PLAYERTABLE();
            _sender.sendMessage("CivCraft data has been loaded");
        }

        protected static void deleteCity(CommandSender _sender, String _cityName)
        {
            City c = Civ.GET_CITY_FROM_NAME(_cityName);
            if (c != null)
            {
                Civ.CITY_LIST.remove(c);
                _sender.sendMessage("City "+c.getDisplayName()+" has been deleted.");
            }
            else _sender.sendMessage("City '"+_cityName+"' not found!");
        }

        protected static void unloadChunks(CommandSender _sender)
        {
            List<World> worldsList = Bukkit.getWorlds();
            for (World w : worldsList)
            {
                Chunk[] loadedChunks = w.getLoadedChunks();
                for (Chunk c: loadedChunks) c.unload();
            }
            System.gc();
            _sender.sendMessage("All chunks unloaded!");
        }
    }



}
