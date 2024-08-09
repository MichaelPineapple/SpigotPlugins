package mcl.god;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("god").setExecutor(new GodcastCommand());
        this.getCommand("godbot").setExecutor(new Godbot.GodbotCommand());

        GodConfigData config = Config.LOAD_CONFIG_DATA();
        if (config.enabled) Godbot.INIT_GODBOT(this, config.timer);

        for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(ChatColor.YELLOW + "God joined");
    }

    @Override
    public void onDisable()
    {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "God left");
    }

    @EventHandler
    public void onBroadcastMessage(BroadcastMessageEvent event) { event.setMessage(Toolkit.GET_GODCAST(ChatColor.stripColor(event.getMessage()))); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Bukkit.broadcastMessage(String.format(Config.JOIN_MSGS.get(Toolkit.GET_RND(0, Config.JOIN_MSGS.size()-1)), event.getPlayer().getName()));
        event.setJoinMessage("");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Bukkit.broadcastMessage(String.format(Config.QUIT_MSGS.get(Toolkit.GET_RND(0, Config.QUIT_MSGS.size()-1)), event.getPlayer().getName()));
        event.setQuitMessage("");
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event)
    {
        try
        {
            String deathMsg = event.getDeathMessage();
            if (deathMsg.toLowerCase().contains("creeper"))
            {
                Bukkit.broadcastMessage(event.getEntity().getName() + " was blown up by a...");
                Bukkit.broadcastMessage("CREEPER!");
                Bukkit.broadcastMessage("AWW MAN...");
            }
            else Bukkit.broadcastMessage(String.format(Config.DIE_MSGS.get(Toolkit.GET_RND(0, Config.DIE_MSGS.size() - 1)), deathMsg));
            event.setDeathMessage("");
        }
        catch (Exception ex) { System.out.println("ERROR IN onPlayerDie() TELL MCLEL!"); }
    }

    class GodcastCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender.isOp())
            {
                String txt = "";
                for (int i = 0; i < args.length; i++) txt += (args[i]+" ");
                if (txt.charAt(0) == '/') Toolkit.RUN_SERVER_CMD(txt.substring(1));
                else Bukkit.broadcastMessage(txt);
            }
            else sender.sendMessage("You are not god...");

            return true;
        }
    }
}