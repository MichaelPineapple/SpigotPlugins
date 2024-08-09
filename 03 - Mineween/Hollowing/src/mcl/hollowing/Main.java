package mcl.hollowing;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Cake;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e)
    {
        addMaxHealth(e.getPlayer(), -1);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Block b = e.getClickedBlock();
            if (b != null)
            {
                if (b.getType() == Material.CAKE)
                {
                    if (e.getPlayer().getMaxHealth() < 20)
                    {
                        takeBite(b);
                        addMaxHealth(e.getPlayer(), 1);
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

    private void takeBite(Block b)
    {
        Cake cake = (Cake)b.getBlockData();
        int bites = cake.getBites() + 1;
        if (bites > cake.getMaximumBites()) b.setType(Material.AIR);
        else
        {
            cake.setBites(bites);
            b.setBlockData(cake);
        }
    }

    private void addMaxHealth(Player p, int val)
    {
        double x = p.getMaxHealth() + (val*2);
        if (x > 20) x = 20;
        if (x < 2) x = 2;
        p.setMaxHealth(x);
    }
}