package mcl.explosivearrows;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    public static final String EXPLOSIVE_ARROW_ID = "explosiveArrow";

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        addRecipes();
    }

    public void addRecipes()
    {
        ItemStack explosiveArrow = new ItemStack(Material.ARROW);
        ItemMeta meta = explosiveArrow.getItemMeta();
        if (meta != null)
        {
            meta.setItemName(EXPLOSIVE_ARROW_ID);
            meta.setDisplayName("Explosive Arrow");
            meta.setEnchantmentGlintOverride(true);
            explosiveArrow.setItemMeta(meta);
        }

        ShapelessRecipe explosiveArrowRecipe = new ShapelessRecipe(new NamespacedKey(this, EXPLOSIVE_ARROW_ID), explosiveArrow);
        explosiveArrowRecipe.addIngredient(Material.ARROW);
        explosiveArrowRecipe.addIngredient(Material.GUNPOWDER);
        Bukkit.addRecipe(explosiveArrowRecipe);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event)
    {
        ItemStack ammunition = event.getConsumable();
        if (ammunition != null)
        {
            ItemMeta ammoMeta = ammunition.getItemMeta();
            if (ammoMeta != null)
            {
                String localName = ammoMeta.getItemName();
                if (localName.equals(EXPLOSIVE_ARROW_ID))
                {
                    Entity projectile = event.getProjectile();
                    if (projectile instanceof Arrow)
                    {
                        Arrow arrow = (Arrow) projectile;
                        arrow.setColor(Color.RED);
                        arrow.setVisualFire(true);
                        arrow.setCustomName(EXPLOSIVE_ARROW_ID);
                        arrow.setCustomNameVisible(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onImpact(ProjectileHitEvent event)
    {
        Entity projectile = event.getEntity();
        if (projectile instanceof Arrow)
        {
            Arrow arrow = (Arrow) projectile;
            String customName = arrow.getCustomName();
            if (customName != null)
            {
                if (customName.equals(EXPLOSIVE_ARROW_ID))
                {
                    Location impactLocaton = null;

                    Entity impactedEntity = event.getHitEntity();
                    if (impactedEntity != null) impactLocaton = impactedEntity.getLocation();
                    else
                    {
                        Block impactedBlock = event.getHitBlock();
                        if (impactedBlock != null) impactLocaton = impactedBlock.getLocation();
                    }

                    if (impactLocaton != null)
                    {
                        World locWorld = impactLocaton.getWorld();
                        if (locWorld != null)
                        {
                            locWorld.createExplosion(impactLocaton, 5);
                            arrow.remove();
                        }
                    }
                }
            }
        }
    }

}