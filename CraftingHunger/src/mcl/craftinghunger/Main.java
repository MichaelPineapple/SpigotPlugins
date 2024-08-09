package mcl.craftinghunger;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event)
    {
        Player p = (Player) event.getWhoClicked();

        int foodLevel = p.getFoodLevel();
        if (foodLevel < 1)
        {
            event.setCancelled(true);
            p.sendMessage("TOO HUNGRY TO CRAFT");
            p.playSound(p.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0f, 1.0f);
        }
        else
        {
            p.playSound(p.getLocation(), Sound.BLOCK_WOOD_STEP, 1.0f, 1.0f);
            p.playSound(p.getLocation(), Sound.BLOCK_WOOD_FALL, 1.0f, 1.0f);
            p.playSound(p.getLocation(), Sound.BLOCK_WOOD_HIT, 1.0f, 1.0f);
            p.playSound(p.getLocation(), Sound.BLOCK_WOOD_PLACE, 1.0f, 1.0f);
            p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BREAK, 1.0f, 1.0f);
            p.setFoodLevel(foodLevel-2);
        }
    }
}


