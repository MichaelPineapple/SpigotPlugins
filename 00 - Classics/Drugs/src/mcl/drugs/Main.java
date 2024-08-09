package mcl.drugs;

import org.bukkit.*;
import org.bukkit.block.Block;
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
import java.util.*;

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
        gunch.add(this);
        DRUG_LIST.add(fard);
        DRUG_LIST.add(gunch);
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
                    Drug drug = getDrug(item.getItemMeta().getItemName().toLowerCase());
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
        for (Drug d : DRUG_LIST) $return += d.getLocalName()+", ";
        return $return;
    }

    private static int getRnd(int min, int max) { return rnd.nextInt((max - min) + 1) + min; }

    Location getLocationNear(Location loc, int radius)
    {
        return new Location(loc.getWorld(), loc.getBlockX()+getRnd(-radius, radius), loc.getBlockY()+getRnd(-radius, radius), loc.getBlockZ()+getRnd(-radius, radius));
    }

    void runServerCmd(String str)
    {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), str);
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



    boolean isAir(Location _loc)
    {
        return isAir(_loc.getBlock().getType());
    }
    boolean isAir(Material _mat)
    {
        return _mat.isAir();
    }


    Location getGroundAt(Location _loc)
    {
        return _loc.getWorld().getHighestBlockAt(_loc).getLocation();
    }

    void changeBlock(Player _p, Location _loc, Material _material)
    {
        _p.sendBlockChange(_loc, _material.createBlockData());
    }

    Location getPlayerTarget(Player _p, int _distance)
    {
        return _p.getTargetBlock(null, _distance).getLocation();
    }

    void resetBlock(Player _p, Location _loc)
    {
        _p.sendBlockChange(_loc, _loc.getBlock().getBlockData());
    }

    Object getRandomElement(List _list) { return _list.get(getRnd(0, _list.size()-1)); }
    Object getRandomElement(Object[] _array) { return _array[getRnd(0, _array.length-1)]; }

    boolean isBlockVisible(Block _b)
    {
        boolean $return = false;

        Block[] adjacent = getDirectAdjacentBlocks(_b);

        for (Block b : adjacent)
        {
            Material m = b.getType();
            if (m == Material.AIR || (!m.isOccluding() && m != Material.WATER && m != Material.LAVA)) $return = true;
        }

        return $return;
    }

    boolean isBlockVisible(Location _loc)
    {
        return isBlockVisible(_loc.getBlock());
    }

    boolean isLog(Material _m)
    {
        return (_m == Material.ACACIA_LOG || _m == Material.BIRCH_LOG ||
                _m == Material.DARK_OAK_LOG || _m == Material.JUNGLE_LOG ||
                _m == Material.OAK_LOG || _m == Material.SPRUCE_LOG);
    }

    boolean isLeaf(Material _m)
    {
        return (_m == Material.ACACIA_LEAVES || _m == Material.BIRCH_LEAVES ||
                _m == Material.DARK_OAK_LEAVES || _m == Material.JUNGLE_LEAVES ||
                _m == Material.OAK_LEAVES || _m == Material.SPRUCE_LEAVES);
    }


    Block[] getAdjacentBlocks(Block _block)
    {
        Block[] $return = {
                _block.getRelative(0, 0, 1),
                _block.getRelative(0, 0, -1),
                _block.getRelative(0, 1, 0),
                _block.getRelative(0, -1, 0),
                _block.getRelative(1, 0, 0),
                _block.getRelative(-1, 0, 0),
                _block.getRelative(1, 1, 1),
                _block.getRelative(1, 1, -1),
                _block.getRelative(1, -1, 1),
                _block.getRelative(-1, -1, -1),
                _block.getRelative(-1, 1, 1),
                _block.getRelative(-1, 1, -1),
                _block.getRelative(-1, -1, 1),
                _block.getRelative(-1, -1, -1),
        };


        return $return;
    }

    Block[] getAdjacentBlocks(Location _loc)
    {
        return getAdjacentBlocks(_loc.getBlock());
    }

    int scheduleTask(Plugin _context, Runnable _func, long _delay)
    {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(_context, _func, _delay);
    }

    int scheduleTaskLoop(Plugin _context, Runnable _func, long _startDelay, long _loopDelay)
    {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(_context, _func, _startDelay, _loopDelay);
    }

    void cancelTask(int _taskID)
    {
        Bukkit.getScheduler().cancelTask(_taskID);
    }

    Block[] getDirectAdjacentBlocks(Block _block)
    {
        Block[] $return = {
                _block.getRelative(0, 0, 1),
                _block.getRelative(0, 0, -1),
                _block.getRelative(0, 1, 0),
                _block.getRelative(0, -1, 0),
                _block.getRelative(1, 0, 0),
                _block.getRelative(-1, 0, 0),
        };

        return $return;
    }


    // *** DRUGS ***

    Drug fard = new Drug()
    {
        private ItemStack fardItem;
        private String localName = "fard";

        final int DELAY = 200, PRE_TRIP = 500, MAIN_TRIP = 1000;

        List<PotionEffect> fardEffects = Arrays.asList(
                new PotionEffect(PotionEffectType.REGENERATION, MAIN_TRIP, 1000),
                new PotionEffect(PotionEffectType.HASTE, MAIN_TRIP, 10),
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, MAIN_TRIP, 10),
                new PotionEffect(PotionEffectType.SPEED, MAIN_TRIP, 10),
                new PotionEffect(PotionEffectType.NIGHT_VISION, MAIN_TRIP, 20));

        List<PotionEffect> fardAfterEffects = Arrays.asList(
                new PotionEffect(PotionEffectType.BLINDNESS, 100, 10),
                new PotionEffect(PotionEffectType.MINING_FATIGUE, 200, 10),
                new PotionEffect(PotionEffectType.SLOWNESS, 200, 0));

        @Override
        public void add(Plugin context)
        {
            fardItem = new ItemStack(Material.PRISMARINE_CRYSTALS);
            ItemMeta meta = fardItem.getItemMeta();
            meta.setItemName(localName);
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(context, () ->
            {
                p.playSound(startLocation, Sound.MUSIC_GAME, 10.0f, 10.0f);
                p.playSound(startLocation, Sound.MUSIC_GAME, 10.0f, 10.0f);
            }, DELAY);

            // early trip
            int task1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(context, new Runnable()
            {
                int fugg = 1;
                public void run()
                {
                    p.spawnParticle(Particle.ENCHANT, p.getEyeLocation(), (int)(fugg/10.0f), 3, 3, 3, 0);
                    p.spawnParticle(Particle.ENCHANT, p.getEyeLocation(), (int)(fugg/100.0f));
                    fugg++;
                }

            }, DELAY, 1);

            // main trip start
            Bukkit.getScheduler().scheduleSyncDelayedTask(context, () ->
            {
                Location loc = p.getLocation();
                p.playSound(loc, Sound.AMBIENT_UNDERWATER_ENTER, 10.0f, 1.0f);
                p.playSound(loc, Sound.ENTITY_PLAYER_HURT_DROWN, 10.0f, 1.0f);
                p.playSound(loc, Sound.ENTITY_PLAYER_BREATH, 10.0f, 1.0f);
                p.sendTitle("屁", "", MAIN_TRIP, 0, 0);
                p.addPotionEffects(fardEffects);
                p.setPlayerWeather(WeatherType.CLEAR);
                p.setPlayerTime(p.getWorld().getTime(), false);
            }, DELAY+PRE_TRIP);

            // main trip loop
            int task2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(context, () ->
            {
                Location loc = p.getLocation();
                Location rndLoc = getLocationNear(loc, 20);

                p.playSound(loc, Sound.MUSIC_DISC_11, 10.0f, 10.0f);
                p.playSound(loc, Sound.AMBIENT_UNDERWATER_LOOP, 10.0f, 1.0f);
                p.playSound(loc, Sound.BLOCK_CONDUIT_AMBIENT, 10.0f, 1.0f);

                if (p.getPlayerTimeOffset() > 12000) p.setPlayerTime(6000, false);
                else p.setPlayerTime(18000, false);

                p.spawnParticle(Particle.SQUID_INK, rndLoc, 5000);
                p.spawnParticle(Particle.END_ROD, loc, 5000, 20, 20, 20, 0);
                p.playSound(rndLoc, Sound.ENTITY_FOX_SCREECH, 10.0f, 0.1f);
                p.playSound(rndLoc, Sound.MUSIC_DISC_13, 10.0f, 10.0f);

            }, DELAY+PRE_TRIP+1, 20);

            // end
            Bukkit.getScheduler().scheduleSyncDelayedTask(context, () ->
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
                p.resetTitle();
            }, MAIN_TRIP+PRE_TRIP+DELAY);

        }

        @Override
        public ItemStack getItem() { return this.fardItem; }

        @Override
        public String getLocalName() { return this.localName; }


    };


    Drug gunch = new Drug()
    {
        private ItemStack gunchItem;
        private String localName = "gunch";

        @Override
        public void add(Plugin context)
        {
            gunchItem = new ItemStack(Material.RED_MUSHROOM);
            ItemMeta meta = gunchItem.getItemMeta();
            meta.setItemName(localName);
            meta.setDisplayName("Gunch");
            meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            gunchItem.setItemMeta(meta);

            NamespacedKey key = new NamespacedKey(context, localName);
            ShapedRecipe recipe = new ShapedRecipe(key, gunchItem);
            recipe.shape(" H ", "HMH", " M ");
            recipe.setIngredient('H', Material.HONEYCOMB);
            recipe.setIngredient('M', Material.RED_MUSHROOM);
            Bukkit.addRecipe(recipe);
        }

        final Material[] acidBlockMaterials =
        {
                Material.BRAIN_CORAL_BLOCK,
                Material.BUBBLE_CORAL_BLOCK,
                Material.FIRE_CORAL_BLOCK,
                Material.HORN_CORAL_BLOCK,
                Material.TUBE_CORAL_BLOCK,
                Material.WHITE_GLAZED_TERRACOTTA,
                Material.ORANGE_GLAZED_TERRACOTTA,
                Material.MAGENTA_GLAZED_TERRACOTTA,
                Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA,
                Material.LIME_GLAZED_TERRACOTTA,
                Material.PINK_GLAZED_TERRACOTTA,
                Material.GRAY_GLAZED_TERRACOTTA,
                Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
                Material.CYAN_GLAZED_TERRACOTTA,
                Material.PURPLE_GLAZED_TERRACOTTA,
                Material.BLUE_GLAZED_TERRACOTTA,
                Material.BROWN_GLAZED_TERRACOTTA,
                Material.GREEN_GLAZED_TERRACOTTA,
                Material.RED_GLAZED_TERRACOTTA,
                Material.BLACK_GLAZED_TERRACOTTA,
        };

        Material getRndAcidBlock()
        {
            return acidBlockMaterials[getRnd(0, acidBlockMaterials.length-1)];
        }

        final Material[] randomBlocks =
        {
                Material.EMERALD_BLOCK,
                Material.DIAMOND_BLOCK,
                Material.GOLD_BLOCK,
                Material.BRAIN_CORAL_BLOCK,
                Material.BUBBLE_CORAL_BLOCK,
                Material.FIRE_CORAL_BLOCK,
                Material.HORN_CORAL_BLOCK,
                Material.TUBE_CORAL_BLOCK,
                Material.WHITE_GLAZED_TERRACOTTA,
                Material.ORANGE_GLAZED_TERRACOTTA,
                Material.MAGENTA_GLAZED_TERRACOTTA,
                Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA,
                Material.LIME_GLAZED_TERRACOTTA,
                Material.PINK_GLAZED_TERRACOTTA,
                Material.GRAY_GLAZED_TERRACOTTA,
                Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
                Material.CYAN_GLAZED_TERRACOTTA,
                Material.PURPLE_GLAZED_TERRACOTTA,
                Material.BLUE_GLAZED_TERRACOTTA,
                Material.BROWN_GLAZED_TERRACOTTA,
                Material.GREEN_GLAZED_TERRACOTTA,
                Material.RED_GLAZED_TERRACOTTA,
                Material.BLACK_GLAZED_TERRACOTTA,
                Material.SEA_LANTERN,
                Material.HONEYCOMB_BLOCK,
                Material.GLOWSTONE,
                Material.PRISMARINE,
                Material.PRISMARINE_BRICKS,
                Material.MUSHROOM_STEM,
                Material.RED_MUSHROOM_BLOCK,
                Material.BROWN_MUSHROOM_BLOCK,
                Material.NETHERRACK,
                Material.TNT,
                Material.DIAMOND_ORE,
                Material.GOLD_ORE,
                Material.CRAFTING_TABLE,
                Material.CHEST,
                Material.GLASS,
                Material.BRICKS,
                Material.DRIED_KELP_BLOCK,
                Material.HAY_BLOCK,
                Material.FURNACE,
                Material.SPAWNER,
                Material.BEDROCK,
                Material.OBSIDIAN,
        };

        final Material[] randomNonSolidBlocks =
        {
                Material.SUGAR_CANE,
                Material.POPPY,
                Material.ORANGE_TULIP,
                Material.PINK_TULIP,
                Material.WHITE_TULIP,
                Material.RED_TULIP,
                Material.WITHER_ROSE,
                Material.AIR,
                Material.SPRUCE_SAPLING,
                Material.ACACIA_SAPLING,
                Material.BIRCH_SAPLING,
        };

        final Sound[] randomSounds =
        {

                Sound.ENTITY_FOX_AMBIENT,
                Sound.ENTITY_SKELETON_AMBIENT,
                Sound.ENTITY_CREEPER_PRIMED,
                Sound.ENTITY_BAT_AMBIENT,
                Sound.BLOCK_BEEHIVE_DRIP,
                Sound.BLOCK_ANVIL_USE,
                Sound.BLOCK_WOODEN_DOOR_OPEN,
                Sound.ENTITY_GENERIC_DRINK,
                Sound.ENTITY_PLAYER_SWIM,
                Sound.ENTITY_COW_AMBIENT,
                Sound.ENTITY_VILLAGER_AMBIENT,
                Sound.ENTITY_SHEEP_AMBIENT,
                Sound.ENTITY_ZOMBIE_AMBIENT,
                Sound.ENTITY_PLAYER_BURP,
                Sound.ENTITY_CAT_AMBIENT,
                Sound.BLOCK_CHEST_OPEN,
                Sound.ENTITY_BEE_LOOP,
        };


        final int TRIP_DURATION = 1000;
        List<PotionEffect> gunchEffects = Arrays.asList(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
        List<PotionEffect> gunchafterEffects = Arrays.asList(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));


        @Override
        public void takeDrug(Plugin context, Player p)
        {
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
            p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_FOX_EAT, 1.0f, 1.0f);

            runServerCmd("stopsounds "+p.getName());

            p.setPlayerTime(13500, false);
            p.addPotionEffects(gunchEffects);
            p.playSound(p.getEyeLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 10.0f, 0.1f);

            handleSkyShrooms(context, p, 0, TRIP_DURATION);
            handlePillars(context, p, 50, TRIP_DURATION-50);
            handleCreep(context, p, 100, TRIP_DURATION-100);

            Runnable endAllTask = () ->
            {
                p.addPotionEffects(gunchafterEffects);
                p.resetPlayerTime();
            };

            scheduleTask(context, endAllTask, TRIP_DURATION-10);
        }


        class GunchRunnable implements Runnable
        {
            ArrayList<Location> totalCreepBlocks;
            ArrayList<Location> creepBlocks;
            ArrayList<Location> finishedBlocks;
            Player _p;
            int index = 0;

            public GunchRunnable(int _threadId, Player _p, ArrayList<Location> _totalCreepBlocks, ArrayList<ArrayList<Location>>  _creepBlocks, ArrayList<Location> _finishedBlocks)
            {
                this.totalCreepBlocks = _totalCreepBlocks;
                this.creepBlocks =_creepBlocks.get(_threadId);
                this.finishedBlocks = _finishedBlocks;
                this._p = _p;
            }

            @Override
            public void run()
            {
                if (!creepBlocks.isEmpty())
                {
                    Location rndloc = creepBlocks.get(index);
                    index++;
                    if (index >= creepBlocks.size() - 1) index = 0;

                    Block[] adjacent = getAdjacentBlocks(rndloc);
                    for (Block b : adjacent)
                    {
                        if (!isAir(b.getType()))
                        {
                            if (isBlockVisible(b))
                            {
                                Location bLoc = b.getLocation();
                                if (!finishedBlocks.contains(bLoc) && !creepBlocks.contains(bLoc))
                                {
                                    changeBlock(_p, bLoc, Material.HONEY_BLOCK);
                                    _p.playSound(bLoc, Sound.BLOCK_HONEY_BLOCK_PLACE, 1.0f, 0.1f);
                                    creepBlocks.add(bLoc);
                                    totalCreepBlocks.add(bLoc);
                                }
                            }
                        }
                    }

                    Material realMat = rndloc.getBlock().getType();
                    Material mat = Material.HONEYCOMB_BLOCK;
                    if (isLog(realMat)) mat = Material.MUSHROOM_STEM;
                    else if (isLeaf(realMat)) mat = Material.BROWN_MUSHROOM_BLOCK;
                    else if (realMat == Material.WATER) mat = Material.LAVA;
                    else if (realMat == Material.LAVA) mat = Material.WATER;
                    else if (!realMat.isSolid()) mat = Material.BROWN_MUSHROOM;
                    changeBlock(_p, rndloc, mat);
                    finishedBlocks.add(rndloc);
                    creepBlocks.remove(rndloc);
                }
            }
        }


        void handleCreep(Plugin _context, Player _p, int _delay, int _duration)
        {
            ArrayList<Location> totalCreepBlocks = new ArrayList<>();
            ArrayList<Location> finishedBlocks = new ArrayList<>();

            ArrayList<ArrayList<Location>> creepBlocks2 = new ArrayList<ArrayList<Location>>();

            Location ploc = _p.getLocation().clone();
            ploc.subtract(0, 1, 0);

            final int creepThreadCount = 10;
            for (int i = 0; i < creepThreadCount; i++)
            {
                creepBlocks2.add(new ArrayList<Location>());
                creepBlocks2.get(i).add(getLocationNear(ploc, 5));
            }
            creepBlocks2.get(0).add(ploc);

            Runnable creepSeedThread = new Runnable()
            {
                int threadIndex = 0;
                @Override
                public void run()
                {
                    Location rndLoc = getLocationNear(_p.getLocation(), 32);

                    if (!isAir(rndLoc))
                    {
                        if (isBlockVisible(rndLoc))
                        {
                            if (!finishedBlocks.contains(rndLoc) && !creepBlocks2.get(threadIndex).contains(rndLoc))
                            {
                                creepBlocks2.get(threadIndex).add(rndLoc);
                            }
                        }
                    }

                    threadIndex++;
                    if (threadIndex > creepThreadCount-1) threadIndex = 0;

                }
            };

            int[] creepTasks = new int[creepThreadCount];
            for (int i = 0; i < creepTasks.length; i++)
            {
                creepTasks[i] = scheduleTaskLoop(_context, new GunchRunnable(i, _p, totalCreepBlocks, creepBlocks2, finishedBlocks), _delay, 1);
            }

            Runnable endThread = () ->
            {
                for (int i = 0; i < creepTasks.length; i++) cancelTask(creepTasks[i]);
                for (Location l : totalCreepBlocks) resetBlock(_p, l);
            };

            scheduleTask(_context, endThread, _delay+_duration);
        }


        void handleAcidBlocks(Plugin _context, Player _p, int _delay, int _duration)
        {
            ArrayList<Location> acidBlocksList = new ArrayList<>();

            Runnable acidCreateThread = () ->
            {
                Location l = getLocationNear(getGroundAt(getPlayerTarget(_p, 5)), 10);
                Material m = l.getBlock().getType();
                if (!isAir(m) && m.isSolid())
                {
                    acidBlocksList.add(l);
                    _p.playSound(l, Sound.BLOCK_PORTAL_AMBIENT, 0.1f, 0.01f);
                }
            };

            Runnable acidUpdateThread = () ->
            {
                for (Location l : acidBlocksList)
                {
                    changeBlock(_p, l, getRndAcidBlock());
                    _p.spawnParticle(Particle.NAUTILUS, l, 10, 0, 0, 0);
                }
            };

            Runnable acidCleanThread = () ->
            {
                if (!acidBlocksList.isEmpty())
                {
                    resetBlock(_p, acidBlocksList.get(0));
                    acidBlocksList.remove(0);
                }
            };

            int[] createTasks = new int[3];
            for (int i = 0; i < createTasks.length; i++) createTasks[i] = scheduleTaskLoop(_context, acidCreateThread, _delay, 1);

            int[] cleanTasks = new int[1];
            for (int i = 0; i < cleanTasks.length; i++) cleanTasks[i] = scheduleTaskLoop(_context, acidCleanThread, _delay+40, 1);

            int updateTask = scheduleTaskLoop(_context, acidUpdateThread, _delay, 1);

            Runnable endThread = () ->
            {
                for (int i = 0; i < createTasks.length; i++) cancelTask(createTasks[i]);
                for (int i = 0; i < cleanTasks.length; i++) cancelTask(cleanTasks[i]);
                cancelTask(updateTask);
                for (Location l : acidBlocksList) resetBlock(_p, l);
                acidBlocksList.clear();
            };

            scheduleTask(_context, endThread, _delay+_duration);
        }

        void handlePillars(Plugin _context, Player _p, int _delay, int _duration)
        {
            ArrayList<Location> pillarBlocks = new ArrayList<>();
            ArrayList<Location> pillarsList = new ArrayList<>();

            Runnable pillarCreateThread = () ->
            {
                pillarsList.add(getGroundAt(getLocationNear(_p.getLocation(), 100)));
            };

            Runnable pillarUpdateThread = () ->
            {
                Location pillar = (Location)getRandomElement(pillarsList);
                changeBlock(_p, pillar, Material.RED_MUSHROOM_BLOCK);
                pillarBlocks.add(pillar.clone());
                pillar.add(0, 1, 0);
            };

            int createTask = scheduleTaskLoop(_context, pillarCreateThread, _delay, 10);
            int updateTask = scheduleTaskLoop(_context, pillarUpdateThread, _delay, 1);

            Runnable endThread = () ->
            {
                cancelTask(createTask);
                cancelTask(updateTask);
                for (Location l : pillarBlocks) resetBlock(_p, l);
                pillarBlocks.clear();
                pillarsList.clear();
            };

            scheduleTask(_context, endThread, _delay+_duration);
        }

        void handleSkyShrooms(Plugin _context, Player _p, int _delay, int _duration)
        {
            ArrayList<Location> skyShroomList = new ArrayList<>();

            Runnable skyShroomThread = () ->
            {
                Location rndloc = getLocationNear(getPlayerTarget(_p, 20), 30);
                Material mat = Material.RED_MUSHROOM;
                if (!isAir(rndloc)) mat = Material.HONEY_BLOCK;
                changeBlock(_p, rndloc, mat);
                skyShroomList.add(rndloc);
            };

            Runnable skyShroomCleanupThread = () ->
            {
                if (!skyShroomList.isEmpty())
                {
                    resetBlock(_p, skyShroomList.get(0));
                    skyShroomList.remove(0);
                }
            };

            int[] skyShroomTasks = new int[3];
            int[] skyShroomCleanupTasks = new int[3];
            for (int i = 0; i < skyShroomTasks.length; i++) skyShroomTasks[i] = scheduleTaskLoop(_context, skyShroomThread, _delay, 1);
            for (int i = 0; i < skyShroomCleanupTasks.length; i++) skyShroomCleanupTasks[i] = scheduleTaskLoop(_context, skyShroomCleanupThread, _delay+20, 1);

            Runnable endThread = () ->
            {
                for (int i = 0; i < skyShroomTasks.length; i++) cancelTask(skyShroomTasks[i]);
            };

            Runnable endCleanupThread = () ->
            {
                for (int i = 0; i < skyShroomCleanupTasks.length; i++) cancelTask(skyShroomCleanupTasks[i]);
            };

            scheduleTask(_context, endThread, _delay+_duration);
            scheduleTask(_context, endCleanupThread, _delay+(_duration* 2L));
        }


        @Override
        public ItemStack getItem() { return this.gunchItem; }

        @Override
        public String getLocalName() { return this.localName; }


    };


}