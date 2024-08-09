package mcl.tpa;

import org.bukkit.*;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener
{
    RequestManager requestManager;
    ChatColor mainColor = ChatColor.AQUA;
    ChatColor playerNameColor = ChatColor.LIGHT_PURPLE;

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        registerCommand("tpa", new MclTpaRequestCommand());
        registerCommand("tpaccept", new MclTpaAcceptCommand());
        requestManager = new RequestManager();
    }

    private void registerCommand(String _cmdName, CommandExecutor _cmdExec)
    {
        Objects.requireNonNull(this.getCommand(_cmdName)).setExecutor(_cmdExec);
    }

    private void spawnExpOrb(World _world, Location _loc, int num)
    {
        _world.spawn(_loc, ExperienceOrb.class).setExperience(num);
    }

    private void playerDropItems(Player _player)
    {
        World world = _player.getWorld();
        Location loc = _player.getLocation();
        Inventory inv = _player.getInventory();
        for (int i = 0; i < inv.getSize(); i++)
        {
            ItemStack item = inv.getItem(i);
            if (item != null) world.dropItemNaturally(loc, item);
        }
        _player.getInventory().clear();
        int orbsFromLvl = (int)(((float)_player.getLevel()+_player.getExp()) / 0.14285715f);
        spawnExpOrb(world, loc, orbsFromLvl);
        _player.setExp(0);
        _player.setLevel(0);
    }

    void teleportPlayer(Player _player, Player _destinationPlayer)
    {
        playerDropItems(_player);
        _player.getWorld().playSound(_player.getLocation(), Sound.ENTITY_FOX_TELEPORT, 1.0f, 1.0f);
        _player.teleport(_destinationPlayer);
        _destinationPlayer.getWorld().playSound(_destinationPlayer.getLocation(), Sound.ENTITY_FOX_TELEPORT, 1.0f, 1.0f);
    }


    class MclTpaAcceptCommand extends MclCommandExecutor
    {
        @Override
        public void handleCommand(Player player, Command cmd, String label, String[] args)
        {
            TpaRequest request = requestManager.getRequest(player);
            if (request != null)
            {
                long delay = Duration.between(request.getTime(), Instant.now()).getSeconds();
                if (delay < 60)
                {
                    teleportPlayer(request.getRequestingPlayer(), player);
                }
                else player.sendMessage(mainColor + "Request timed out.");
            }
            else player.sendMessage(mainColor + "You have no pending requests.");
        }
    }

    class MclTpaRequestCommand extends MclCommandExecutor
    {
        @Override
        public void handleCommand(Player player, Command cmd, String label, String[] args)
        {
            if (args.length > 0)
            {
                String requestedPlayerName = args[0];
                Player requestedPlayer = Bukkit.getPlayer(requestedPlayerName);
                if (requestedPlayer != null)
                {
                    requestManager.makeRequest(player, requestedPlayer);
                    player.sendMessage(mainColor + "Request sent to " + playerNameColor + "" + requestedPlayer.getDisplayName());
                    requestedPlayer.sendMessage(mainColor + "New teleport request from " + playerNameColor + "" + player.getDisplayName());
                    requestedPlayer.sendMessage(mainColor + "Use " + ChatColor.LIGHT_PURPLE + " /tpaccept " + mainColor + " to accept.");
                }
                else player.sendMessage(mainColor + "Player " + playerNameColor + "" + requestedPlayerName + mainColor + " not found.");
            }
            else player.sendMessage(mainColor + "No player name provided.");
        }
    }

}