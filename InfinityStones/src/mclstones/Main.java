package mclstones;

import javafx.print.PageLayout;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import sun.reflect.generics.tree.TypeArgument;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener
{
    final String SPACE_TAG = "spacestone", TIME_TAG = "timestone", MIND_TAG = "mindstone", REALITY_TAG = "realitystone", POWER_TAG = "powerstone", SOUL_TAG = "soulstone";
    final int MIND_HOLD_DURATION = 200, TIME_FREEZE_DURATION = 300, TIME_FREEZE_COOLDOWN = 60;
    final PotionEffect spaceEffect1 = new PotionEffect(PotionEffectType.SLOW_FALLING, 1000, 0);
    final PotionEffect spaceEffect2 = new PotionEffect(PotionEffectType.LEVITATION, 100, 10);
    final PotionEffect timeEffect1 = new PotionEffect(PotionEffectType.SLOW, TIME_FREEZE_DURATION, 100000);
    final PotionEffect timeEffect2 = new PotionEffect(PotionEffectType.SLOW_DIGGING, TIME_FREEZE_DURATION, 10000);
    final Vector summonVector = new Vector(0, 0.5, 0), zeroVector = new Vector(0, 0, 0);

    Instant lastTimeFreeze = Instant.MIN;

    ItemStack SpaceStone = new ItemStack(Material.DIAMOND);
    ItemStack TimeStone = new ItemStack(Material.EMERALD);
    ItemStack MindStone = new ItemStack(Material.YELLOW_DYE);
    ItemStack RealityStone = new ItemStack(Material.RED_DYE);
    ItemStack PowerStone = new ItemStack(Material.LAPIS_LAZULI);
    ItemStack SoulStone = new ItemStack(Material.GOLD_NUGGET);

    ItemStack ThanosBoots = new ItemStack(Material.GOLDEN_BOOTS);
    ItemStack ThanosPants = new ItemStack(Material.GOLDEN_LEGGINGS);
    ItemStack ThanosChest = new ItemStack(Material.GOLDEN_CHESTPLATE);
    ItemStack ThanosHelmet = new ItemStack(Material.GOLDEN_HELMET);

    BlockData[] spookyBlocks =
    {
            Material.DIAMOND_BLOCK.createBlockData(),
            Material.GOLD_BLOCK.createBlockData(),
            Material.EMERALD_BLOCK.createBlockData(),
    };


    EntityType[] minions =
    {
            EntityType.HUSK,
            EntityType.STRAY,
    };

    ItemStack nelsonHelmet = new ItemStack(Material.IRON_HELMET);
    ItemStack henrySword = new ItemStack(Material.IRON_SWORD);

    int forceGrabTask = -1, forceGrabTask2 = -2;

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("thanos").setExecutor(new ThanosCommand());
        createItems();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player)
        {
            Player p = (Player)event.getDamager();
            Entity target = event.getEntity();
            ItemStack item = p.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR)
            {
                if (item.getItemMeta().getLocalizedName().equals(MIND_TAG)) mindLeft(target, p);
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        if (event.getHand() == EquipmentSlot.HAND)
        {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR)
            {
                String itemTag = item.getItemMeta().getLocalizedName();
                if (itemTag.equals(MIND_TAG))
                {
                    mindRight(event.getRightClicked(), event.getPlayer());
                    event.setCancelled(true);
                }
                else if (itemTag.equals(SOUL_TAG))
                {
                    soulRight(event.getRightClicked());
                    event.setCancelled(true);
                }
            }
        }
    }



    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null)
        {
            if (event.getHand() == EquipmentSlot.HAND)
            {
                Action act = event.getAction();
                String itemTag = event.getItem().getItemMeta().getLocalizedName();

                if (itemTag.equals(SPACE_TAG))
                {
                    if (isLeftClick(act)) spaceLeft(event.getPlayer());
                    else if (isRightClick(act)) spaceRight(event.getPlayer());
                }
                else if (itemTag.equals(TIME_TAG))
                {
                    if (isLeftClick(act)) timeLeft(event.getPlayer());
                    else if (isRightClick(act)) timeRight(event.getPlayer());
                }
                else if (itemTag.equals(REALITY_TAG))
                {
                    if (isLeftClick(act)) realityLeft(event.getPlayer());
                    else if (isRightClick(act)) realityRight(event.getPlayer());
                }
                else if (itemTag.equals(POWER_TAG))
                {
                    if (isLeftClick(act)) powerLeft(event.getPlayer());
                    else if (isRightClick(act)) powerRight(event.getPlayer());
                }
                else if (itemTag.equals(SOUL_TAG))
                {
                    if (isLeftClick(act)) soulLeft(event.getPlayer());
                }
                else if (itemTag.equals(MIND_TAG))
                {
                    if (isRightClick(act))
                    {
                        cancelForceGrab();
                        //event.setCancelled(true);
                    }
                }
            }
        }
    }

    void cancelForceGrab()
    {
        if (forceGrabTask != -1)
        {
            Bukkit.getScheduler().cancelTask(forceGrabTask);
            Bukkit.getScheduler().cancelTask(forceGrabTask2);
            forceGrabTask = -1;
            forceGrabTask2 = -2;
        }
    }


    Random rnd = new Random();
    int getRnd(int min, int max) { return rnd.nextInt((max - min) + 1) + min; }

    boolean isLeftClick(Action act) { return (act == Action.LEFT_CLICK_AIR || act == Action.LEFT_CLICK_BLOCK); }
    boolean isRightClick(Action act) { return (act == Action.RIGHT_CLICK_AIR || act == Action.RIGHT_CLICK_BLOCK); }

    void screenSplash(Player p, String mainTxt, String subTxt, int fadeIn, int duration, int fadeOut)
    {
        p.resetTitle();
        p.sendTitle(mainTxt, subTxt, fadeIn, duration, fadeOut);
    }

    void smallSplash(Player p, String txt) { p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(txt)); }

    void playAudio(Entity _entity, Sound _sound) { playAudio(_entity, _sound, 10.0f, 0.5f); }
    void playAudio(Entity _entity, Sound _sound, float volume, float pitch) { _entity.getWorld().playSound(_entity.getLocation(), _sound, volume, pitch); }
    void playAudio(Entity _entity, Sound _sound, float volume, float pitch, int mult)
    {
        World w = _entity.getWorld();
        Location loc = _entity.getLocation();
        for (int i = 0; i < mult; i++) w.playSound(loc, _sound, volume, pitch);
    }

    void giveStones(Player p)
    {
        Inventory i = p.getInventory();
        i.addItem(SpaceStone);
        i.addItem(TimeStone);
        i.addItem(MindStone);
        i.addItem(RealityStone);
        i.addItem(PowerStone);
        i.addItem(SoulStone);
    }


    class ThanosCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender.hasPermission("thanos"))
            {
                if (args.length >= 1)
                {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p != null)
                    {
                        giveStones(p);
                        sender.sendMessage(p.getName() + " has been given all the infinity stones");
                    }
                    else sender.sendMessage("Player not found");
                }
                else
                {
                    if (sender instanceof Player) giveStones((Player)sender);
                    else sender.sendMessage("Please specify a player");
                }


            }
            else sender.sendMessage("Only authorized users can use this command");

            return true;
        }
    }











































    // *** POWERS ***

    // Space
    void spaceLeft(Player p)
    {
        List<Entity> nearbyEnts = p.getNearbyEntities(20, 20, 20);
        for (Entity e : nearbyEnts)
        {
            if (e instanceof LivingEntity)
            {
                LivingEntity boi = (LivingEntity)e;
                boi.addPotionEffect(spaceEffect1, true);
                boi.addPotionEffect(spaceEffect2, true);
                playAudio(boi, Sound.BLOCK_BEACON_ACTIVATE);
            }
        }
        playAudio(p, Sound.BLOCK_BEACON_ACTIVATE);
    }

    void spaceRight(Player p)
    {
        playAudio(p, Sound.ENTITY_ENDERMAN_TELEPORT);
        p.teleport(p.getTargetBlock(null, 1000).getLocation());
        playAudio(p, Sound.ENTITY_ENDERMAN_TELEPORT);
        p.addPotionEffect(spaceEffect1, true);
    }

    // Time
    void timeLeft(Player p)
    {
        long timeRemaining = TIME_FREEZE_COOLDOWN - (Duration.between(lastTimeFreeze, Instant.now()).getSeconds());
        if (timeRemaining < 1)
        {
            screenSplash(p, "", ChatColor.GREEN+"TIME FROZEN", 0, TIME_FREEZE_DURATION, 0);
            playAudio(p, Sound.BLOCK_ENDER_CHEST_OPEN, 10.0f, 0.01f, 10);
            List<Entity> bois = p.getWorld().getEntities();
            bois.remove(p);
            for (Entity e : bois) freezeEntity(e, TIME_FREEZE_DURATION);
            lastTimeFreeze = Instant.now();
        }
        else
        {
            playAudio(p, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 10.0f, 1.0f);
            playAudio(p, Sound.BLOCK_ENDER_CHEST_OPEN, 0.5f, 0.01f);
            smallSplash(p, ChatColor.GREEN+"Time Freeze available in "+timeRemaining+" seconds");
        }
    }

    void timeRight(Player p)
    {
        World w = p.getWorld();
        playAudio(p, Sound.BLOCK_ENDER_CHEST_OPEN);
        if (w.getTime() > 12000) w.setTime(6000);
        else w.setTime(18000);
    }

    // Mind
    void mindLeft(Entity target, Player p)
    {
        if (forceGrabTask == -1)
        {
            if (target instanceof Player)
            {
                Player playerTarget = (Player) target;
                playerTarget.sendTitle("", ChatColor.YELLOW + "YOU ARE BEING CONTROLED BY THE MIND STONE", 0, MIND_HOLD_DURATION, 0);
                playerTarget.setAllowFlight(true);
            }

            forceGrabTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
            {
                int countdown = MIND_HOLD_DURATION;

                public void run()
                {
                    if (p.isDead() || target.isDead()) cancelForceGrab();
                    target.teleport(p.getTargetBlock(null, 3).getLocation());
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "" + countdown));
                    countdown--;
                }
            }, 0, 1);

            forceGrabTask2 = Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
            {
                public void run()
                {
                    cancelForceGrab();
                    if (target instanceof Player) ((Player) target).resetTitle();
                }
            }, MIND_HOLD_DURATION);

            playAudio(target, Sound.BLOCK_BEACON_DEACTIVATE);
            playAudio(p, Sound.BLOCK_BEACON_DEACTIVATE);
        }


    }

    void mindRight(Entity target, Player p)
    {
        if (target instanceof Player)
        {
            Player targetPlayer = (Player)target;
            p.openInventory(targetPlayer.getInventory());
            p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
        }
        else p.playSound(p.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0f, 1.0f);
    }

    // Reality
    void realityLeft(Player p)
    {
        World w = p.getWorld();
        Location targetLoc = p.getTargetBlock(null, 1000).getLocation();
        w.playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 10.0f, 1.0f);
        w.playSound(targetLoc, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 10.0f, 1.0f);
        Entity minion = w.spawnEntity(targetLoc, minions[getRnd(0, minions.length-1)]);
        minion.setVelocity(summonVector);
        minion.setCustomNameVisible(true);

        if (minion instanceof Husk)
        {
            Husk henry = (Husk)minion;
            henry.setCustomName("Henry");
            EntityEquipment equip = henry.getEquipment();
            equip.setItemInMainHand(henrySword);
        }
        else if (minion instanceof Stray)
        {
            Stray nelson = (Stray)minion;
            nelson.setCustomName("Nelson");
            EntityEquipment equip = nelson.getEquipment();
            equip.setHelmet(nelsonHelmet);
        }

        w.spawnParticle(Particle.CRIT_MAGIC, targetLoc, 1000);
    }

    void realityRight(Player p)
    {
        World w = p.getWorld();
        Location targetLoc = p.getTargetBlock(null, 3).getLocation();
        w.playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 10.0f, 0.5f);
        w.playSound(targetLoc, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 10.0f, 0.5f);
        w.spawnParticle(Particle.PORTAL, targetLoc, 1000, 1, 1, 1, 1);
        w.spawnParticle(Particle.NAUTILUS, targetLoc, 1000, 1, 1, 1, 1);
        for (Player q : Bukkit.getOnlinePlayers()) q.sendBlockChange(targetLoc, spookyBlocks[getRnd(0, spookyBlocks.length-1)]);
    }

    // Power
    void powerLeft(Player p)
    {
        World w = p.getWorld();
        Location targetLoc = p.getTargetBlock(null, 1000).getLocation();
        w.strikeLightning(targetLoc);
        w.createExplosion(targetLoc.getBlockX(), targetLoc.getBlockY(), targetLoc.getBlockZ(), 10, true, true);
    }

    void powerRight(Player p)
    {
        World w = p.getWorld();
        EntityEquipment equip = p.getEquipment();
        Location ploc = p.getLocation();

        ItemStack boots = equip.getBoots();
        ItemStack pants = equip.getLeggings();
        ItemStack chest = equip.getChestplate();
        ItemStack helmet = equip.getHelmet();

        if (boots != null && !boots.getItemMeta().getLocalizedName().equals(ThanosBoots.getItemMeta().getLocalizedName())) w.dropItemNaturally(ploc, boots);
        if (pants != null && !pants.getItemMeta().getLocalizedName().equals(ThanosPants.getItemMeta().getLocalizedName())) w.dropItemNaturally(ploc, pants);
        if (chest != null && !chest.getItemMeta().getLocalizedName().equals(ThanosChest.getItemMeta().getLocalizedName())) w.dropItemNaturally(ploc, chest);
        if (helmet != null && !helmet.getItemMeta().getLocalizedName().equals(ThanosHelmet.getItemMeta().getLocalizedName())) w.dropItemNaturally(ploc, helmet);

        equip.setBoots(ThanosBoots);
        equip.setLeggings(ThanosPants);
        equip.setChestplate(ThanosChest);
        equip.setHelmet(ThanosHelmet);

        playAudio(p, Sound.ITEM_ARMOR_EQUIP_DIAMOND, 10.0f, 0.5f, 5);
    }

    // Soul
    void soulLeft(Player p)
    {
        Inventory inv = p.getInventory();
        if (inv.contains(SpaceStone) && inv.contains(TimeStone) && inv.contains(MindStone) && inv.contains(PowerStone) && inv.contains(RealityStone) && inv.contains(SoulStone))
        {
            snap(p);
            inv.remove(SoulStone);
        }
        else playAudio(p, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0f, 1.0f);
    }

    void soulRight(Entity target)
    {
        snapEntity(target);
    }






































    // *** SNAP ***
    void snap(Player p)
    {
        World w = p.getWorld();
        w.createExplosion(p.getLocation(), 5);
        playAudio(p, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);

        List<LivingEntity> bois = w.getLivingEntities();
        bois.remove(p);
        for (LivingEntity e : bois)
        {
            if (getRnd(0, 1) == 1)
            {
                snapEntity(e);
                if (e instanceof Player) Bukkit.broadcastMessage(((Player) e).getDisplayName()+" was snapped");
            }
        }
    }
    void snapEntity(Entity target)
    {
        target.remove();
        target.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target.getLocation(), 1000, target.getWidth()/2, target.getHeight()/2, target.getWidth()/2, 0);
    }





































    // *** ZA WARUDO ***
    void freezeEntity(Entity e, int duration)
    {
        playAudio(e, Sound.BLOCK_ENDER_CHEST_OPEN, 10.0f, 0.01f, 5);
        Vector originalVelocity = e.getVelocity();
        Location freezeLocation = e.getLocation();
        e.setSilent(true);
        e.setVelocity(zeroVector);

        if (e instanceof LivingEntity)
        {
            if (e instanceof Player)
            {
                Player targetP = (Player)e;
                screenSplash(targetP, "", ChatColor.GREEN+"TIME FROZEN", 0, TIME_FREEZE_DURATION, 0);
                targetP.setWalkSpeed(0);
                targetP.setAllowFlight(true);
                targetP.setCanPickupItems(false);
                targetP.addPotionEffect(timeEffect2, true);
                //targetP.addPotionEffect(timeEffect1, true);
            }
            else ((LivingEntity) e).setAI(false);
        }
        else e.setGravity(false);

        int task1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() { public void run()
        {
            e.teleport(freezeLocation);

        }}, 0, 0);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() { public void run()
        {
            Bukkit.getScheduler().cancelTask(task1);
            playAudio(e, Sound.BLOCK_ENDER_CHEST_CLOSE, 10.0f, 0.01f, 5);
            e.setVelocity(originalVelocity);
            e.setGravity(true);
            e.setSilent(false);

            if (e instanceof LivingEntity)
            {
                if (e instanceof Player)
                {
                    Player targetP = (Player)e;
                    targetP.setWalkSpeed(0.2f);
                    targetP.setCanPickupItems(true);
                    targetP.setAllowFlight(false);
                }
                else ((LivingEntity) e).setAI(true);
            }

        }}, duration);

    }




































    // *** ITEM CREATION ***
    void createItems()
    {
        // Infinity Stones
        ChatColor hue = ChatColor.AQUA;
        ChatColor white = ChatColor.WHITE;
        ItemMeta meta = SpaceStone.getItemMeta();
        meta.setLocalizedName(SPACE_TAG);
        meta.setDisplayName(hue + "Space Stone");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setLore(Arrays.asList(hue+"Left Click: "+white+"Anti-Gravity", hue+"Right Click: "+white+"Teleport"));
        SpaceStone.setItemMeta(meta);

        hue = ChatColor.GREEN;
        meta = TimeStone.getItemMeta();
        meta.setLocalizedName(TIME_TAG);
        meta.setDisplayName(hue + "Time Stone");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setLore(Arrays.asList(hue+"Left Click: "+white+"Freeze Time", hue+"Right Click: "+white+"Toggle Day/Night"));
        TimeStone.setItemMeta(meta);

        hue = ChatColor.YELLOW;
        meta = MindStone.getItemMeta();
        meta.setLocalizedName(MIND_TAG);
        meta.setDisplayName(hue + "Mind Stone");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setLore(Arrays.asList(hue+"Left Click: "+white+"Force Grab", hue+"Right Click: "+white+"Open Player's Inventory"));
        MindStone.setItemMeta(meta);

        hue = ChatColor.RED;
        meta = RealityStone.getItemMeta();
        meta.setLocalizedName(REALITY_TAG);
        meta.setDisplayName(hue + "Reality Stone");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setLore(Arrays.asList(hue+"Left Click: "+white+"Summon Minions", hue+"Right Click: "+white+"Distort Reality"));
        RealityStone.setItemMeta(meta);

        hue = ChatColor.LIGHT_PURPLE;
        meta = PowerStone.getItemMeta();
        meta.setLocalizedName(POWER_TAG);
        meta.setDisplayName(hue + "Power Stone");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setLore(Arrays.asList(hue+"Left Click: "+white+"Stormbreaker", hue+"Right Click: "+white+"Armor"));
        PowerStone.setItemMeta(meta);

        hue = ChatColor.GOLD;
        meta = SoulStone.getItemMeta();
        meta.setLocalizedName(SOUL_TAG);
        meta.setDisplayName(hue + "Soul Stone");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setLore(Arrays.asList(hue+"Left Click: "+white+"Snap (All Stones Required)", hue+"Right Click: "+white+"Destroy Soul"));
        SoulStone.setItemMeta(meta);

        // Thanos Armor
        hue = ChatColor.DARK_PURPLE;
        meta = ThanosBoots.getItemMeta();
        meta.setDisplayName(hue+"Thanos Boots");
        meta.setLocalizedName("thanosboots");
        meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 100, true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
        meta.addEnchant(Enchantment.DURABILITY, 100, true);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        ThanosBoots.setItemMeta(meta);

        meta = ThanosPants.getItemMeta();
        meta.setDisplayName(hue+"Thanos Leggings");
        meta.setLocalizedName("thanospants");
        meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 100, true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
        meta.addEnchant(Enchantment.DURABILITY, 100, true);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        ThanosPants.setItemMeta(meta);

        meta = ThanosChest.getItemMeta();
        meta.setDisplayName(hue+"Thanos Chestplate");
        meta.setLocalizedName("thanoschest");
        meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 100, true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
        meta.addEnchant(Enchantment.DURABILITY, 100, true);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        ThanosChest.setItemMeta(meta);

        meta = ThanosHelmet.getItemMeta();
        meta.setDisplayName(hue+"Thanos Helmet");
        meta.setLocalizedName("thanoshelmet");
        meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 100, true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
        meta.addEnchant(Enchantment.DURABILITY, 100, true);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        ThanosHelmet.setItemMeta(meta);

    }


}
