package mcl;

import mcl.Commands.CommandHandler;
import mcl.Commands.Commands;
import mcl.Core.Civ;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        super.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        registerCommands();
        Civ.INIT(this);
        Civ.LOAD_PLAYERTABLE();
        Civ.LOAD_CITIES();
    }

    @Override
    public void onDisable()
    {
        Civ.SAVE_PLAYERTABLE();
        Civ.SAVE_CITIES();
        super.onDisable();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        event.setCancelled(!Civ.CAN_BUILD(event.getPlayer(), event.getBlock()));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        event.setCancelled(!Civ.CAN_BUILD(event.getPlayer(), event.getBlock()));
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event)
    {
        Block b = event.getClickedBlock();
        if (b != null && b.getType().isInteractable())
        {
            event.setCancelled(!Civ.CAN_BUILD(event.getPlayer(), b));
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
        Civ.ON_PLAYER_JOIN(event.getPlayer());
    }

    void registerCommands()
    {
        registerCmd(Commands.FOUND_CITY);
        registerCmd(Commands.CLAIM_LAND);
        registerCmd(Commands.UNCLAIM_LAND);
        registerCmd(Commands.SHOW_LAND);
        registerCmd(Commands.ADD_CITIZEN);
        registerCmd(Commands.REMOVE_CITIZEN);
        registerCmd(Commands.LIST_CITIES);
        registerCmd(Commands.CITY_STATS);
        registerCmd(Commands.CITY_RENAME);
        registerCmd(Commands.ADD_ALLY);
        registerCmd(Commands.ALLY_ACCEPT);
        registerCmd(Commands.ALLY_DENY);
        registerCmd(Commands.LEAVE_ALLIANCE);
        registerCmd(Commands.LEAVE_CITY);
        registerCmd(Commands.RESIGN);
        registerCmd(Commands.ADD_OFFICER);
        registerCmd(Commands.DEL_OFFICER);
        registerAdminCmd(Commands.Admin.CLEAR_DATA);
        registerAdminCmd(Commands.Admin.DEL_CITY);
        registerAdminCmd(Commands.Admin.UNLOAD_CHUNKS);
        registerAdminCmd(Commands.Admin.LOAD_DATA);
    }
    void registerCmd(String _cmd)
    {
        this.getCommand(_cmd).setExecutor(new CommandHandler());
        this.getCommand(_cmd).setTabCompleter(new CommandHandler.TabCompletion());
    }
    void registerAdminCmd(String _cmd) { this.getCommand(_cmd).setExecutor(new CommandHandler.Admin()); }
}