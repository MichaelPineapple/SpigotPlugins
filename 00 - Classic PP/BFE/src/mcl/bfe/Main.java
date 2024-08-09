package mcl.bfe;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;
import java.io.*;
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

    final int DEFAULT_BFE_DISTANCE = 20000;
    final int DEFUALT_COOLDOWN = 60;
    int bfeDistance = DEFAULT_BFE_DISTANCE;
    int cooldown = DEFUALT_COOLDOWN;

    final String DEFAULT_CONFIG_DATA = "// Distance (blocks)\n"+DEFAULT_BFE_DISTANCE+"\n// Cooldown (seconds)\n"+DEFUALT_COOLDOWN;

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("bfe").setExecutor(new BfeCommand());

        try
        {
            File configFile = new File("mclcfg/bfe/BFEconfig.shid");
            if (configFile.exists())
            {
                BufferedReader reader = new BufferedReader(new FileReader(configFile.getCanonicalPath()));
                reader.readLine();
                bfeDistance = Integer.parseInt(reader.readLine());
                reader.readLine();
                cooldown = Integer.parseInt(reader.readLine());
                reader.close();
            }
            else
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(configFile.getCanonicalPath()));
                writer.write(DEFAULT_CONFIG_DATA);
                writer.close();
            }
        }
        catch (Exception ex) { System.out.println("BFE: error reading/writing config file! Resorting to default values..."); }

    }
    
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

    void teleportPlayer(Player player, Location loc)
    {
        playerDropItems(player);
        player.teleport(loc);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    private void playerDropItems(Player player)
    {
        World world = player.getWorld();
        Location loc = player.getLocation();
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++)
        {
            ItemStack item = inv.getItem(i);
            if (item != null) world.dropItemNaturally(loc, item);
        }
        player.getInventory().clear();
        int orbsFromLvl = (int)(((float)player.getLevel()+player.getExp()) / 0.14285715f);
        spawnExpOrb(world, loc, orbsFromLvl);
        player.setExp(0);
        player.setLevel(0);
    }

    private void spawnExpOrb(World world, Location loc, int num)
    {
        world.spawn(loc, ExperienceOrb.class).setExperience(num);
    }

    Location getRandomLocation(int radius, World theWorld)
    {
        int x = getRnd(-radius, radius), z = getRnd(-radius, radius);
        int y = theWorld.getHighestBlockYAt(x, z) + 5;
        return new Location(theWorld, x, y, z);
    }

    void teleportPlayerToRandomLocation(Player player)
    {
        teleportPlayer(player, getRandomLocation(bfeDistance, player.getWorld()));
    }

    void alertPlayer(Player p, String txt)
    {
        //p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(txt));
        p.sendMessage(txt);
    }


    class BfePlayer
    {
        private UUID id;
        private long waitTime = cooldown;
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