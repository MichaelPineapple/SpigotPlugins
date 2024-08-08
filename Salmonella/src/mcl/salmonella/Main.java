package mcl.salmonella;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

public class Main extends JavaPlugin implements Listener
{
    static final Set<Material> RAW_FOOD = Set.of(Material.CHICKEN, Material.BEEF, Material.PORKCHOP,
            Material.MUTTON, Material.RABBIT, Material.ROTTEN_FLESH);

    static final List<PotionEffect> FOOD_POISONING = List.of(new PotionEffect(PotionEffectType.HUNGER, 10000, 100));

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e)
    {
        Player p = e.getPlayer();
        Material item = e.getItem().getType();
        if (RAW_FOOD.contains(item)) p.addPotionEffects(FOOD_POISONING);
    }
}