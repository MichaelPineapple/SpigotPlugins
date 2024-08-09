package mcl.bonk;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class Main extends JavaPlugin implements Listener
{
    static final Set<Material> BONK_TOOLS = Set.of(Material.BONE, Material.STICK, Material.FLINT, Material.BAMBOO);

    static final Set<Material> AXES = Set.of(Material.WOODEN_AXE, Material.STONE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE,
            Material.DIAMOND_AXE, Material.NETHERITE_AXE);

    static final Set<Material> LOGS = Set.of(Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.DARK_OAK_LOG,
            Material.ACACIA_LOG, Material.CHERRY_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG);

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
            if (LOGS.contains(mat))
            {
                ItemStack stack = p.getInventory().getItemInMainHand();
                Material tool = stack.getType();
                if (!AXES.contains(tool))
                {
                    if (BONK_TOOLS.contains(tool))
                    {
                        int x = stack.getAmount() - 1;
                        if (x < 0) x = 0;
                        stack.setAmount(x);
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                    }
                    else e.setDropItems(false);
                }
            }
        }
    }
}