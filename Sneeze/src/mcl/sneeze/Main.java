package mcl.sneeze;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        PluginCommand cmd =  this.getCommand("sneeze");
        if (cmd != null) cmd.setExecutor(new SneezeCommand());
    }

    @Override
    public void onDisable() { }

    void sneeze(Player _p)
    {
        World theworld = _p.getWorld();
        theworld.spawnParticle(Particle.SNEEZE, _p.getEyeLocation(), 10, 0.1, 0, 0.1, 0.1, null, true);
        theworld.playSound(_p.getEyeLocation(), Sound.ENTITY_PANDA_SNEEZE, 10.0f, 0.01f);
    }


    class SneezeCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof Player)  sneeze((Player)sender);
            else
            {
                if (args.length > 0)
                {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p != null)
                    {
                        sneeze(p);
                        sender.sendMessage("Forced '"+p.getName()+"' to sneeze.");
                    }
                    else sender.sendMessage("Player '"+args[0]+"' not found.");
                }
                else sender.sendMessage("Please specify a player.");
            }

            return true;
        }
    }

}
