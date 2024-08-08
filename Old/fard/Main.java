package fardrugs;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener
{
    ArrayList<Drug> DRUG_LIST = new ArrayList<>();
    private static Random rnd = new Random();

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("givedrug").setExecutor(new GiveDrugCommand());
        fard.add(this);
        DRUG_LIST.add(fard);
    }

    @Override
    public void onDisable() { }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getHand() == EquipmentSlot.HAND)
        {
            Action act = event.getAction();
            if (act == Action.RIGHT_CLICK_AIR || act == Action.RIGHT_CLICK_BLOCK)
            {
                ItemStack item = event.getItem();

                if (item != null)
                {
                    Drug drug = getDrug(item.getItemMeta().getLocalizedName().toLowerCase());
                    if (drug != null) drug.takeDrug(this, event.getPlayer());
                }
            }
        }
    }

    Drug getDrug(String str)
    {
        Drug $return = null;
        for (Drug d : DRUG_LIST) if (str.equals(d.getLocalName())) $return = d;
        return $return;
    }

    String getDrugOptions()
    {
        String $return = "Options: ";
        for (Drug d : DRUG_LIST) $return += " "+d.getLocalName();
        return $return;
    }

    private static int getRnd(int min, int max) { return rnd.nextInt((max - min) + 1) + min; }

    Location getLocationNear(World Za_WaRuDo, Location loc, int radius)
    {
        return new Location(Za_WaRuDo, loc.getBlockX()+getRnd(-radius, radius), loc.getBlockY()+getRnd(-radius, radius), loc.getBlockZ()+getRnd(-radius, radius));
    }


    class GiveDrugCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender.isOp())
            {
                if (args.length >= 1)
                {
                    Drug drug = getDrug(args[0].toLowerCase());
                    if (drug != null)
                    {
                        String drugName = drug.getItem().getItemMeta().getDisplayName();
                        if (args.length >= 2)
                        {
                            try
                            {
                                Player p = Bukkit.getPlayer(args[1]);
                                p.getInventory().addItem(drug.getItem());
                                sender.sendMessage(drugName+" given to "+p.getName());
                            }
                            catch (Exception ex) { sender.sendMessage("Unknown player");}
                        }
                        else
                        {
                            if (sender instanceof Player)
                            {
                                ((Player)sender).getInventory().addItem(drug.getItem());
                                sender.sendMessage(drugName+" added to inventory");
                            }
                            else sender.sendMessage("You must specify a player");
                        }
                    }
                    else sender.sendMessage("Unknown drug, Try again. " +getDrugOptions());

                }
                else sender.sendMessage("Please specify drug. "+getDrugOptions());
            }
            else sender.sendMessage("You must be OP");

            return true;
        }
    }



    // *** DRUGS ***

    Drug fard = new Drug()
    {
        private ItemStack fardItem;
        private String localName = "fard";

        final int DELAY = 200, PRE_TRIP = 500, MAIN_TRIP = 1000;

        List<PotionEffect> fardEffects = Arrays.asList(
                new PotionEffect(PotionEffectType.HEAL, MAIN_TRIP, 1000),
                new PotionEffect(PotionEffectType.FAST_DIGGING, MAIN_TRIP, 10),
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, MAIN_TRIP, 10),
                new PotionEffect(PotionEffectType.SPEED, MAIN_TRIP, 10),
                new PotionEffect(PotionEffectType.NIGHT_VISION, MAIN_TRIP, 20));

        List<PotionEffect> fardAfterEffects = Arrays.asList(
                new PotionEffect(PotionEffectType.BLINDNESS, 100, 10),
                new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 10),
                new PotionEffect(PotionEffectType.SLOW, 200, 0));

        @Override
        public void add(Plugin context)
        {
            fardItem = new ItemStack(Material.PRISMARINE_CRYSTALS);
            ItemMeta meta = fardItem.getItemMeta();
            meta.setLocalizedName(localName);
            meta.setDisplayName("Fardamphetamine");
            meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            fardItem.setItemMeta(meta);


            NamespacedKey key = new NamespacedKey(context, "fardamphetamine");
            ShapedRecipe recipe = new ShapedRecipe(key, fardItem);
            recipe.shape("FFF", "FCF", "FFF");
            recipe.setIngredient('F', Material.DEAD_BRAIN_CORAL_BLOCK);
            recipe.setIngredient('C', Material.PRISMARINE_SHARD);
            Bukkit.addRecipe(recipe);
        }

        @Override
        public void takeDrug(Plugin context, Player p)
        {
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
            p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 1.0f, 1.0f);
            Location startLocation = p.getLocation();

            // start music
            Bukkit.getScheduler().scheduleSyncDelayedTask(context, new Runnable() { public void run()
            {
                p.playSound(startLocation, Sound.MUSIC_GAME, 10.0f, 10.0f);
                p.playSound(startLocation, Sound.MUSIC_GAME, 10.0f, 10.0f);
            }}, DELAY);

            // early trip
            int task1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(context, new Runnable()
            {
                int fugg = 1;
                public void run()
                {
                    p.spawnParticle(Particle.ENCHANTMENT_TABLE, p.getEyeLocation(), (int)(fugg/10.0f), 3, 3, 3, 0);
                    p.spawnParticle(Particle.ENCHANTMENT_TABLE, p.getEyeLocation(), (int)(fugg/100.0f));
                    fugg++;
                }

            }, DELAY, 1);

            // main trip start
            Bukkit.getScheduler().scheduleSyncDelayedTask(context, new Runnable() { public void run()
            {
                Location loc = p.getLocation();
                p.playSound(loc, Sound.AMBIENT_UNDERWATER_ENTER, 10.0f, 1.0f);
                p.playSound(loc, Sound.ENTITY_PLAYER_HURT_DROWN, 10.0f, 1.0f);
                p.playSound(loc, Sound.ENTITY_PLAYER_BREATH, 10.0f, 1.0f);
                p.sendTitle("屁", "", MAIN_TRIP, 0, 0);
                p.addPotionEffects(fardEffects);
                p.setPlayerWeather(WeatherType.CLEAR);
                p.setPlayerTime(p.getWorld().getTime(), false);
            }}, DELAY+PRE_TRIP);

            // main trip loop
            int task2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(context, new Runnable() { public void run()
            {
                Location loc = p.getLocation();
                Location rndLoc = getLocationNear(p.getWorld(), loc, 20);

                p.playSound(loc, Sound.MUSIC_DISC_11, 10.0f, 10.0f);
                p.playSound(loc, Sound.AMBIENT_UNDERWATER_LOOP, 10.0f, 1.0f);
                p.playSound(loc, Sound.BLOCK_CONDUIT_AMBIENT, 10.0f, 1.0f);

                if (p.getPlayerTimeOffset() > 12000) p.setPlayerTime(6000, false);
                else p.setPlayerTime(18000, false);

                p.spawnParticle(Particle.SQUID_INK, rndLoc, 5000);
                p.spawnParticle(Particle.END_ROD, loc, 5000, 20, 20, 20, 0);
                p.playSound(rndLoc, Sound.ENTITY_FOX_SCREECH, 10.0f, 0.1f);
                p.playSound(rndLoc, Sound.MUSIC_DISC_13, 10.0f, 10.0f);

            }}, DELAY+PRE_TRIP+1, 20);

            // end
            Bukkit.getScheduler().scheduleSyncDelayedTask(context, new Runnable() { public void run()
            {
                p.resetPlayerWeather();
                p.resetPlayerTime();
                p.stopSound(Sound.AMBIENT_UNDERWATER_LOOP);
                p.stopSound(Sound.ENTITY_FOX_SCREECH);
                p.stopSound(Sound.MUSIC_DISC_13);
                p.stopSound(Sound.MUSIC_GAME);
                p.stopSound(Sound.MUSIC_DISC_11);
                Bukkit.getScheduler().cancelTask(task1);
                Bukkit.getScheduler().cancelTask(task2);
                p.addPotionEffects(fardAfterEffects);
                p.teleport(startLocation);
            }}, MAIN_TRIP+PRE_TRIP+DELAY);

        }

        @Override
        public ItemStack getItem() { return this.fardItem; }

        @Override
        public String getLocalName() { return this.localName; }


    };


}
