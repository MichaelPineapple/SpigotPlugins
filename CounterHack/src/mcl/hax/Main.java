package mcl.hax;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.List;
import java.util.Arrays;

public class Main extends JavaPlugin implements Listener
{
    List<PotionEffect> hackEffects = Arrays.asList(
            new PotionEffect(PotionEffectType.NAUSEA, 1000000, 1000),
            new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 1000),
            new PotionEffect(PotionEffectType.SLOWNESS, 100000, 1000),
            new PotionEffect(PotionEffectType.GLOWING, 1000000, 1000),
            new PotionEffect(PotionEffectType.MINING_FATIGUE, 100000, 1000));


    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("hack").setExecutor(new HackCommand());
        this.getCommand("hackall").setExecutor(new HackAllCommand());
    }

    void crashPlayer(Player p)
    {
        int task1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            public void run()
            {
                p.spawnParticle(Particle.SQUID_INK, p.getEyeLocation(), 1000);
            }

        }, 0, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() { public void run()
        {
            p.spawnParticle(Particle.SQUID_INK, p.getEyeLocation(), Integer.MAX_VALUE);
        }}, 100);
    }

    void hackPlayer(Player targetPlayer)
    {
        targetPlayer.sendMessage("YOU HAVE BEEN HACKED");
        targetPlayer.setWalkSpeed(0);
        targetPlayer.setCanPickupItems(false);
        targetPlayer.setAllowFlight(false);
        targetPlayer.setFlySpeed(0);
        targetPlayer.setOp(false);
        targetPlayer.setGameMode(GameMode.ADVENTURE);
        targetPlayer.addPotionEffects(hackEffects);
        targetPlayer.sendTitle(ChatColor.BOLD + "" + ChatColor.RED + "FUCK YOU", ChatColor.MAGIC+"oiuwyf87rgfo38ff093uf93f8y387f3f8f4738f743f8374f384f734gf83f34", 1, 1000, 1);
        crashPlayer(targetPlayer);
    }


    class HackCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender.isOp())
            {
                if (sender instanceof Player)
                {
                    Player p = (Player)sender;
                    if (!(p.getName().equals("InspectorMclel") || p.getName().equals("JarekBoi")))
                    {
                        sender.sendMessage("Nice try...");
                        return true;
                    }
                }

                if (args.length >= 1)
                {
                    String targetPlayerName = args[0];
                    Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                    if (targetPlayer != null)
                    {
                        hackPlayer(targetPlayer);
                        Bukkit.broadcastMessage(targetPlayerName + " has been HACKED");
                    }
                    else sender.sendMessage("Player not found");
                }
                else sender.sendMessage("Please specify a player");
            }
            else sender.sendMessage("You must be an operator");

            return true;
        }
    }


    class HackAllCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender.isOp())
            {
                if (sender instanceof Player)
                {
                    Player p = (Player)sender;
                    if (!(p.getName().equals("InspectorMclel") || p.getName().equals("JarekBoi")))
                    {
                        sender.sendMessage("Nice try...");
                        return true;
                    }
                }

                for (Player p : Bukkit.getOnlinePlayers()) hackPlayer(p);
                Bukkit.broadcastMessage("Yall got hacked lol");
            }
            else sender.sendMessage("You must be an operator");

            return true;
        }
    }

}