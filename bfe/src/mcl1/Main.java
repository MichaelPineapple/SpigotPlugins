package mcl1;

import javafx.application.Platform;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener
{
    Random rnd = new Random();
    int getRnd(int min, int max) { return rnd.nextInt((max - min) + 1) + min; }
    ArrayList<BfePlayer> bfePlayersList = new ArrayList<>();

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("bfe").setExecutor(new BfeCommand());
    }

    @Override
    public void onDisable() { }

    BfePlayer findBfePlayer(UUID id)
    {
        BfePlayer $return = null;
        for (int i = 0; i < bfePlayersList.size(); i++)
        {
            BfePlayer tmp = bfePlayersList.get(i);
            if (tmp.getUUID().equals(id)) $return = tmp;
        }
        return $return;
    }

    void teleportPlayer(Player player, Location loc, World world)
    {
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++)
        {
            if (inv.getItem(i) != null) world.dropItemNaturally(player.getLocation(), inv.getItem(i));
        }
        player.getInventory().clear();
        player.teleport(loc);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }


    Location getRandomLocation(int radius, World theWorld)
    {
        int x = getRnd(-radius, radius), z = getRnd(-radius, radius);
        int y = theWorld.getHighestBlockYAt(x, z) + 5;
        return new Location(theWorld, x, y, z);
    }

    void teleportPlayerToRandomLocation(Player player)
    {
        World theWorld = player.getWorld();
        teleportPlayer(player, getRandomLocation(10000, theWorld), theWorld);
    }

    void alertPlayer(Player p, String txt)
    {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(txt));
    }


    class BfePlayer
    {
        private UUID id;
        private long waitTime = 60;
        public Instant time;

        public BfePlayer(UUID _id)
        {
            this.id = _id;
            this.time = Instant.MIN;
        }

        public void restartTimer() { this.time = Instant.now(); }
        public long getTimeLeft() { return waitTime - (Duration.between(this.time, Instant.now()).getSeconds()); }
        public UUID getUUID() { return this.id; }
    }

    class BfeCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof Player)
            {
                Player player = (Player)sender;
                UUID pid = player.getUniqueId();
                BfePlayer pp = findBfePlayer(pid);
                if (pp == null)
                {
                    pp = new BfePlayer(pid);
                    bfePlayersList.add(pp);
                }
                long timeRemaining = pp.getTimeLeft();

                if (timeRemaining < 1)
                {
                    alertPlayer(player, "Teleporting...");
                    teleportPlayerToRandomLocation(player);
                    pp.restartTimer();
                }
                else
                {
                    alertPlayer(player, "Please wait " + timeRemaining + " seconds before teleporting again");
                    player.playSound(player.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.1f, 1.0f);
                }
            }
            else System.out.println("This command can only be used by a player.");

            return true;
        }
    }
}
