package mcl.hardslate;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Set;

public class Main extends JavaPlugin implements Listener
{
    static final Set<Material> WEAKLINGS = Set.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE);

    static final Set<Material> DEEPSLATES = Set.of(Material.DEEPSLATE, Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_COPPER_ORE,
            Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.DEEPSLATE_GOLD_ORE,
            Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.DEEPSLATE_REDSTONE_ORE,
            Material.INFESTED_DEEPSLATE);

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.SURVIVAL)
        {
            Material mat = e.getBlock().getType();
            Material tool = p.getInventory().getItemInMainHand().getType();
            if (DEEPSLATES.contains(mat) && WEAKLINGS.contains(tool)) e.setCancelled(true);
        }
    }
}