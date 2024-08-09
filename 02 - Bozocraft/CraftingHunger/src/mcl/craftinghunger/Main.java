package mcl.craftinghunger;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    final Sound[] CRAFTING_SOUNDS =
    {
        Sound.BLOCK_WOOD_STEP,
        Sound.BLOCK_WOOD_FALL,
        Sound.BLOCK_WOOD_HIT,
        Sound.BLOCK_WOOD_PLACE,
        Sound.BLOCK_WOOD_BREAK,
    };

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event)
    {
        Player p = (Player) event.getWhoClicked();
        int food = p.getFoodLevel() - 2;
        if (food < 0) food = 0;
        playSounds(p, CRAFTING_SOUNDS);
        p.setFoodLevel(food);
    }

    private void playSounds(Player p, Sound[] sounds)
    {
        World w = p.getWorld();
        for (Sound s: sounds) w.playSound(p, s, 0.5f, 1.0f);
    }
}


