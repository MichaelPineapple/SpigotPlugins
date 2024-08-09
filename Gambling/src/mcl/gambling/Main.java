package mcl.gambling;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener
{
    Random rnd = new Random();
    Set<UUID> gamers = new HashSet<>();

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        PluginCommand cmd = this.getCommand("gamble");
        if (cmd != null) cmd.setExecutor(new GambleExecutor());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        if (!gamers.contains(id))
        {
            p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.EMERALD));
            gamers.add(id);
        }
    }

    class GambleExecutor implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof Player)
            {
                Player p = (Player) sender;
                ItemStack item = p.getInventory().getItemInMainHand();
                if (item.getType() == Material.EMERALD)
                {
                    ItemStack offhand = p.getInventory().getItemInOffHand();
                    if (offhand.getType() == Material.AIR)
                    {
                        int x = item.getAmount() - 1;
                        if (x < 0) return false;
                        item.setAmount(x);
                        Material[] materials = Material.values();
                        int matIndex = rnd.nextInt(0, materials.length);
                        Material mat = materials[matIndex];
                        int num = rnd.nextInt(0, mat.getMaxStackSize());
                        p.getInventory().setItemInOffHand(new ItemStack(mat, num));
                        p.getWorld().playSound(p, Sound.ITEM_GOAT_HORN_SOUND_1, 1000.0f, 1.0f);
                    }
                    else p.sendMessage("Your offhand must be empty");
                }
                else p.sendMessage("You must be holding an emerald");
            }
            else sender.sendMessage("Must be player");

            return true;
        }
    }
}