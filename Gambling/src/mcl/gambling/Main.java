package mcl.gambling;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Main extends JavaPlugin implements Listener
{
    Random rnd = new Random();

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        PluginCommand cmd = this.getCommand("gamble");
        if (cmd != null) cmd.setExecutor(new GambleExecutor());
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
                    int x = item.getAmount() - 1;
                    if (x < 0) return false;
                    item.setAmount(x);
                    p.playSound(p, Sound.ITEM_BUNDLE_DROP_CONTENTS, 1.0f, 1.0f);
                    Material[] materials = Material.values();
                    int matIndex = rnd.nextInt(0, materials.length);
                    Material mat = materials[matIndex];
                    p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(mat));
                    p.playSound(p, Sound.ITEM_BUNDLE_DROP_CONTENTS, 1.0f, 1.0f);
                }
            }
            else sender.sendMessage("Must be player");

            return true;
        }
    }
}