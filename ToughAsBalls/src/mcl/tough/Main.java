package mcl.tough;

import mcl.tough.managers.BallsManager;
import mcl.tough.managers.HydrationManager;
import mcl.tough.managers.RecipeManager;
import mcl.tough.managers.TemperatureManager;
import mcl.tough.objects.EnvironmentalEffect;
import mcl.tough.objects.PlayerBalls;
import mcl.tough.util.Util;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.*;

public class Main extends JavaPlugin implements Listener
{
    BallsManager ballsManager;
    RecipeManager recipeManager;
    HydrationManager hydroManager;
    TemperatureManager tempManager;
    final int UPDATE_TICKS = 100;
    Integer updateTaskId = null;
    final int SCORCHING_THRESHOLD = 3;
    final int FREEZING_THRESHOLD = -3;


    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(this.getCommand("e")).setExecutor(new MclStatsCommand());
        ballsManager = new BallsManager();
        tempManager = new TemperatureManager();
        hydroManager = new HydrationManager(ballsManager);
        recipeManager = new RecipeManager(this);
        recipeManager.addRecipes();

        var onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player p : onlinePlayers)
        {
            boolean inHell = (p.getWorld().getEnvironment() == World.Environment.NETHER);
            ballsManager.addNewBalls(p);
        }

        updateTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            public void run()
            {
                onUpdate();
            }

        }, UPDATE_TICKS, UPDATE_TICKS);

    }

    @Override
    public void onDisable()
    {
        if (updateTaskId != null)
        {
            Bukkit.getScheduler().cancelTask(updateTaskId);
        }
    }

    /** EVENT HANDLERS **/

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        Player p = event.getPlayer();
        ballsManager.addNewBalls(p);
        ballsManager.getBalls(p).adjustHydration(-50);


        updateTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
        {
            public void run()
            {
                p.setHealth(10);
                p.setFoodLevel(10);
            }

        }, 1);
    }



    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if (!ballsManager.hasBalls(player)) ballsManager.addNewBalls(player);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e)
    {
        HumanEntity ent = e.getEntity();
        if (ent instanceof Player)
        {
            Player player = (Player)ent;
            hydroManager.adjustHydration(player, -5.0f, false);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        Player player = event.getPlayer();
        Material item = event.getItem().getType();
        if (item == Material.POTION)
        {
            hydroManager.adjustHydration(player, 20.0f, true);
        }
    }


    /** UPDATE **/

    public void onUpdate()
    {
        var onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player p : onlinePlayers)
        {

            ArrayList<EnvironmentalEffect> effects = tempManager.calcEnvTemp(p);

            PlayerBalls balls = ballsManager.getBalls(p);
            if (balls != null)
            {
                int prevBodyTemp = balls.getBodyTemp();
                balls.setTemperatureEffects(effects);
                int curBodyTemp = balls.getBodyTemp();
                if (curBodyTemp != prevBodyTemp) Util.displayTxt(p, TemperatureManager.FORMAT_TEMP_DISPLAY(curBodyTemp));

                if (curBodyTemp <= FREEZING_THRESHOLD) p.setFreezeTicks(UPDATE_TICKS*4);

                if (curBodyTemp >= SCORCHING_THRESHOLD) p.setFireTicks(UPDATE_TICKS);

                float hydration = balls.getHydration();
                if (hydration <= 0)
                {
                    p.damage(1);
                    Util.displayTxt(p, HydrationManager.FORMAT_HYDRATION(hydration));
                }

            }
            else p.sendMessage(Util.NO_BALLS_MSG);
        }
    }

    /** COMMANDS **/

    class MclStatsCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;

                PlayerBalls balls = ballsManager.getBalls(player);
                if (balls != null)
                {
                    player.sendMessage(HydrationManager.FORMAT_HYDRATION(balls.getHydration()));
                    player.sendMessage("BODY TEMP: "+TemperatureManager.FORMAT_TEMP_DISPLAY(balls.getBodyTemp()));
                    ArrayList<EnvironmentalEffect> envEffects = balls.getTemperatureEffects();
                    for (EnvironmentalEffect effect: envEffects) player.sendMessage(effect.getName()+" ("+effect.getValue()+")");
                }
                else player.sendMessage(Util.NO_BALLS_MSG);

            }
            else System.out.println("This command can only be used by a player.");

            return true;
        }
    }

}