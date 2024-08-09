package mcl.bones;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Set;

public class Main extends JavaPlugin implements Listener
{
    static final Set<EntityType> BONERS = Set.of(EntityType.MULE, EntityType.HORSE, EntityType.PIG, EntityType.SHEEP,
            EntityType.CAMEL, EntityType.COW, EntityType.DONKEY, EntityType.GOAT, EntityType.LLAMA,
            EntityType.MOOSHROOM, EntityType.PANDA, EntityType.POLAR_BEAR, EntityType.RAVAGER);

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e)
    {
        Entity ent = e.getEntity();
        EntityType type = ent.getType();
        if (BONERS.contains(type))  ent.getWorld().dropItemNaturally(ent.getLocation(), new ItemStack(Material.BONE));
    }
}