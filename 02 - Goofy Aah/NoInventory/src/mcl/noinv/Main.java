package mcl.noinv;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);

    }

    public void fillInventory(Player p)
    {
        PlayerInventory inv = p.getInventory();
        for (int i = 9; i <= 35; i++) inv.setItem(i, new ItemStack(Material.BARRIER));
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e)
    {
        Location deathLoc = e.getEntity().getLocation();
        List<ItemStack> ogDroppedItems = e.getDrops();
        for (int i = 0; i < ogDroppedItems.size(); i++)
        {
            ItemStack item = ogDroppedItems.get(i);
            if (item != null)
            {
                if (item.getType() != Material.BARRIER)
                {
                    Objects.requireNonNull(deathLoc.getWorld()).dropItemNaturally(deathLoc, new ItemStack(item));
                }
            }
        }
        e.getDrops().clear();
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        fillInventory(e.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        fillInventory(e.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e)
    {
        fillInventory(e.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        Inventory clickedInv = e.getClickedInventory();
        if (clickedInv != null)
        {
            if (clickedInv.getType() == InventoryType.PLAYER)
            {
                int slot = e.getSlot();
                if (slot >= 9 && slot <= 35) e.setResult(Event.Result.DENY);
            }
        }
    }
}