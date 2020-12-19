package mcl.Commands;
import mcl.Core.Civ;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            String command = cmd.getName();

            if (command.equals(Commands.FOUND_CITY))
            {
                if (args.length == 1) CommandFunctions.settle(p, args[0]);
                else p.sendMessage("Invalid!");
            }
            else if (command.equals(Commands.ADD_CITIZEN))
            {
                if (args.length == 1) CommandFunctions.addCitizen(p, args[0]);
                else sender.sendMessage("Invalid!");
            }
            else if (command.equals(Commands.REMOVE_CITIZEN))
            {
                if (args.length == 1) CommandFunctions.removeCitizen(p, args[0]);
                else sender.sendMessage("Invalid!");
            }
            else if (command.equals(Commands.CITY_RENAME))
            {
                if (args.length == 1) CommandFunctions.renameCity(p, args[0]);
                else sender.sendMessage("Please Enter a Valid City Name. Use /help for usage.");
            }
            else if (command.equals(Commands.ADD_ALLY))
            {
                if (args.length == 1) CommandFunctions.addAlly(p, args[0]);
                else sender.sendMessage("Please Enter a Valid City Name. Use /help for usage.");
            }
            else if (command.equals(Commands.LEAVE_ALLIANCE))
            {
                if (args.length == 1) CommandFunctions.leaveAlliance(p, args[0]);
                else sender.sendMessage("Please Enter a Valid City Name. Use /help for usage.");
            }
            else if (command.equals(Commands.ADD_OFFICER))
            {
                if (args.length == 1) CommandFunctions.addOfficer(p, args[0]);
                else sender.sendMessage("Please provide a player name.");
            }
            else if (command.equals(Commands.DEL_OFFICER))
            {
                if (args.length == 1) CommandFunctions.delOfficer(p, args[0]);
                else sender.sendMessage("Please provide a player name.");
            }
            else if(command.equals(Commands.RESIGN))
            {
                if (args.length > 0) CommandFunctions.mayorResign(p, args[0]);
                else CommandFunctions.disbandCity(p);
            }
            else if (command.equals(Commands.CLAIM_LAND)) CommandFunctions.claimLand(p);
            else if (command.equals(Commands.UNCLAIM_LAND)) CommandFunctions.unclaimLand(p);
            else if (command.equals(Commands.LIST_CITIES)) CommandFunctions.cityList(p);
            else if (command.equals(Commands.SHOW_LAND)) CommandFunctions.showLand(p);
            else if (command.equals(Commands.CITY_STATS)) CommandFunctions.cityStats(p);
            else if (command.equals(Commands.ALLY_ACCEPT)) CommandFunctions.allyAccept(p);
            else if (command.equals(Commands.ALLY_DENY)) CommandFunctions.allyDeny(p);
            else if (command.equals(Commands.LEAVE_CITY)) CommandFunctions.leaveCity(p);

            else sender.sendMessage("ERROR");
        }
        else sender.sendMessage("Only players can use this command.");

        return true;
    }

    /** ADMIN COMMAND HANDLER **/
    public static class Admin implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender.isOp())
            {
                String command = cmd.getName();

                if (command.equals(Commands.Admin.DEL_CITY))
                {
                    if (args.length == 1) CommandFunctions.Admin.deleteCity(sender, args[0]);
                    else sender.sendMessage("Invalid!");
                }
                else if (command.equals(Commands.Admin.CLEAR_DATA)) CommandFunctions.Admin.clearData(sender);
                else if (command.equals(Commands.Admin.UNLOAD_CHUNKS))  CommandFunctions.Admin.unloadChunks(sender);
                else if (command.equals(Commands.Admin.LOAD_DATA))  CommandFunctions.Admin.loadData(sender);
                else sender.sendMessage("ERROR");
            }
            else sender.sendMessage("You must be OP to use this command.");

            return true;
        }
    }

    /** Tab Complete **/
    public static  class TabCompletion implements TabCompleter
    {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
        {
            ArrayList<String> ouput = new ArrayList<>();
            if (sender instanceof Player)
            {
                Player p = (Player) sender;
                String command = cmd.getName();

                if (command.equals(Commands.ADD_CITIZEN))
                {
                    ouput = Civ.GET_PLAYERTABLE_NAMES();
                }
                else if (command.equals(Commands.REMOVE_CITIZEN))
                {
                    ouput = Civ.GET_PLAYERTABLE_NAMES();
                }
                else if (command.equals(Commands.ADD_ALLY))
                {
                    ouput = Civ.GET_CITY_NAMES();
                }
                else if (command.equals(Commands.LEAVE_ALLIANCE))
                {
                    ouput = Civ.GET_CITY_NAMES();
                }
                else if (command.equals(Commands.ADD_OFFICER))
                {
                    ouput = Civ.GET_PLAYERTABLE_NAMES();
                    /*
                    City n = Civ.GET_MAYOR_CITY(p);
                    if (n != null)
                    {
                        ArrayList<CivPlayer> tmpCitizensList = n.copyCitizens();
                        for (CivPlayer citizen : tmpCitizensList) ouput.add(citizen.getName());
                    }

                     */
                }
                else if (command.equals(Commands.DEL_OFFICER))
                {
                    ouput = Civ.GET_PLAYERTABLE_NAMES();
                }
                else if(command.equals(Commands.RESIGN))
                {
                    ouput = Civ.GET_PLAYERTABLE_NAMES();
                }
            }

            return ouput;
        }
    }

}


