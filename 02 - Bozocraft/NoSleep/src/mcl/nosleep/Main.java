package mcl.nosleep;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onSleep(TimeSkipEvent e)
    {
        if (e.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) e.setCancelled(true);
    }
}