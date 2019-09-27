package E;

import org.bukkit.*;
import org.bukkit.block.data.type.Bed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Main extends JavaPlugin implements Listener
{

    final String GODBOT_OPTIONS_STR = "Options: enable, disable, reset, speed, speed <ticks>, act <h> <player>";

    final int updateTimerDefault = 10000;

    int updateTimer = updateTimerDefault;
    int godbotTask = -1;

    ChatColor godColor = ChatColor.LIGHT_PURPLE;

    String[] JOIN_MSGS =
    {
            "%s has arrived",
            "%s connected to the server.",
            "shit, %s is here",
            "%s joined the game",
            "%s just joined, they must be lost...",
            "Hello %s, please announce your pronouns",
            "Umm... who invited %s?",
            "hey %s, m/f?",
            "someone joined but im not gonna say who",
            "sup %s",
            "%s joined. thats pretty cringe",
    };

    String[] QUIT_MSGS =
    {
            "%s quit. RIP",
            "damn, %s just left",
            "%s disconnected from the server.",
            "%s left the game",
            "WOW. %s rage quit",
            "Goodbye %s",
            "There goes %s",
            "%s is no longer with us...",
            "%s doesnt like this server",
            "%s quit. Too much cum?",
    };

    String[] DIE_MSGS =
    {
            "lmao %s. Press F to pay respects",
            "yikes, %s",
            "%s. oof",
            "lol %s",
            "bruh moment: %s",
            "%s. thats pretty cringe bro",
            "%s. retard",
            "%s. really chief?",
            "oh my god! %s!",
            "%s. can i get an F in the chat?",
            "bro %s",
            "lol %s     https://www.youtube.com/watch?v=dwLCjZVEtpE",
    };

    String[] UPDATE_MSGS =
    {
            "Hows it going?",
            "So this is how you spend your free time?",
            "crunch",
            "try /cum",
            "tfw ur god",
            "nice house %s",
            "my pp: 8=============D",
            "uwu",
            "owo",
            "i bet %s unironically likes minecraft",
            "u_u",
            "h",
            "E",
            "B-Topia was better",
            "hmmmm",
            "w",
            "reeeeeeeee",
            "WARNING: %s IS GAY",
            "hey %s, how are you?",
            "this server sucks",
            "my bros, check out this video: https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            "this fard stuff is wack. dont do kids",
            "i see you",
    };

    String[] MUSIC_MSGS =
    {
            "This song is a classic",
            "yo pass the aux",
            "this is my jam",
            "yall best be ready for this absolute bop",
            "guys you gotta listen to this",
            "VOLUME UP LADIES",
            "music time",
            "im boutta bust a funky lyric",
            "lets get these jams",
            "yall mind if i play some music?"
    };

    String[] SNEEZE_MSGS =
    {
            "damn %s cover your nose",
            "bless you",
            "gesundheit (@%s)",
            "%s sneezed",
    };


    PotionEffectType[] RND_POTS =
    {
            PotionEffectType.BAD_OMEN,
            PotionEffectType.BLINDNESS,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.HEAL,
            PotionEffectType.CONFUSION,
            PotionEffectType.SPEED,
            PotionEffectType.LUCK,
            PotionEffectType.JUMP,
            PotionEffectType.REGENERATION,
            PotionEffectType.ABSORPTION,
            PotionEffectType.DAMAGE_RESISTANCE,
            PotionEffectType.FAST_DIGGING,
            PotionEffectType.GLOWING,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.WATER_BREATHING,
            PotionEffectType.UNLUCK,
    };

    ChatColor[] godColors =
    {
            ChatColor.AQUA,
            ChatColor.BLUE,
            ChatColor.RED,
            ChatColor.LIGHT_PURPLE,
            ChatColor.GREEN,
            ChatColor.YELLOW,
            ChatColor.GOLD,
    };

    Sound[] music =
    {
            Sound.MUSIC_DISC_CAT,
            Sound.MUSIC_DISC_WAIT,
            Sound.MUSIC_DISC_BLOCKS,
            Sound.MUSIC_DISC_CHIRP,
            Sound.MUSIC_DISC_FAR,
            Sound.MUSIC_DISC_MALL,
            Sound.MUSIC_DISC_MELLOHI,
            Sound.MUSIC_DISC_STAL,
            Sound.MUSIC_DISC_STRAD,
            Sound.MUSIC_DISC_WARD,
            Sound.MUSIC_CREDITS,
    };




    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("god").setExecutor(new GodcastCommand());
        this.getCommand("godbot").setExecutor(new GodbotCommand());
        initializeGodbot();
        for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(ChatColor.YELLOW + "God joined");
    }

    @Override
    public void onDisable() { Bukkit.broadcastMessage(ChatColor.YELLOW + "God left"); }

    @EventHandler
    public void onBroadcastMessage(BroadcastMessageEvent event) { event.setMessage(getGodcast(ChatColor.stripColor(event.getMessage()))); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Bukkit.broadcastMessage(String.format(JOIN_MSGS[getRnd(0, JOIN_MSGS.length-1)], event.getPlayer().getName()));
        event.setJoinMessage("");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Bukkit.broadcastMessage(String.format(QUIT_MSGS[getRnd(0, QUIT_MSGS.length-1)], event.getPlayer().getName()));
        event.setQuitMessage("");
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event)
    {
        String deathMsg = event.getDeathMessage();
        if (deathMsg.toLowerCase().contains("creeper"))
        {
            Bukkit.broadcastMessage(event.getEntity().getName() + " was blown up by a...");
            Bukkit.broadcastMessage("CREEPER!");
            Bukkit.broadcastMessage("AWW MAN...");
        }
        else Bukkit.broadcastMessage(String.format(DIE_MSGS[getRnd(0, DIE_MSGS.length-1)], deathMsg));
        event.setDeathMessage("");
    }

    private static Random rnd = new Random();
    private static int getRnd(int min, int max) { return rnd.nextInt((max - min) + 1) + min; }

    class GodcastCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender.isOp())
            {
                String txt = "";
                for (int i = 0; i < args.length; i++) txt += (args[i]+" ");
                if (txt.charAt(0) == '/') runServerCmd(txt.substring(1));
                else Bukkit.broadcastMessage(txt);
            }
            else sender.sendMessage("You are not god...");

            return true;
        }
    }

    void godcast_withcolor(String txt) { for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(getGodcast(txt)); }
    String getGodcast(String txt) { return (godColor + "" + ChatColor.BOLD + "<God> " + ChatColor.WHITE + "" + ChatColor.ITALIC + txt); }


    /* GOD BOD */

    class GodbotCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender.isOp())
            {
                if (args.length > 0)
                {
                    String option = args[0];
                    if (option.equals("disable"))
                    {
                       if (Bukkit.getScheduler().isQueued(godbotTask))
                       {
                           disableGodbot();
                           sender.sendMessage("Godbot disabled!");
                       }
                       else sender.sendMessage("Godbot is already disabled.");
                    }
                    else if (option.equals("enable"))
                    {
                        if (!Bukkit.getScheduler().isQueued(godbotTask))
                        {
                            enableGodbot();
                            sender.sendMessage("Godbot enabled!");
                        }
                        else sender.sendMessage("Godbot is already enabled.");
                    }
                    else if (option.equals("reset"))
                    {
                        setGodbotSpeed(updateTimerDefault);
                        sender.sendMessage("Godbot reset!");
                    }
                    else if (option.equals("speed"))
                    {
                        if (args.length > 1)
                        {
                            try
                            {
                                int ticks = Integer.parseInt(args[1]);
                                if (ticks > 0)
                                {
                                    setGodbotSpeed(ticks);
                                    sender.sendMessage("Godbot timer set to: "+updateTimer+" ticks");
                                }
                                else sender.sendMessage(ticks + " is an invalid value!");
                            }
                            catch (Exception ex){ sender.sendMessage("Invalid parameter!"); }
                        }
                        else  sender.sendMessage("Godbot timer is currently: "+updateTimer+" ticks");
                    }
                    else if (option.equals("act"))
                    {
                        try { godbotAct(Integer.parseInt(args[1]), Bukkit.getPlayer(args[2])); }
                        catch (Exception ex){ sender.sendMessage("Invalid parameter!"); }
                    }
                    else sender.sendMessage("Unknown command. "+GODBOT_OPTIONS_STR);
                }
                else sender.sendMessage(GODBOT_OPTIONS_STR);
            }
            else sender.sendMessage("You are not god...");

            return true;
        }
    }


    void initializeGodbot() { godbotTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {public void run() { update(); }}, updateTimer, updateTimer); }

    void disableGodbot() { if (Bukkit.getScheduler().isQueued(godbotTask)) Bukkit.getScheduler().cancelTask(godbotTask); }

    void enableGodbot() { if (!Bukkit.getScheduler().isQueued(godbotTask)) initializeGodbot(); }

    void setGodbotSpeed(int ticks)
    {
        updateTimer = ticks;
        disableGodbot();
        enableGodbot();
    }

    void runServerCmd(String str) { this.getServer().dispatchCommand(this.getServer().getConsoleSender(), str); }

    void playToEveryone(Sound _sound, float volume)
    {
        for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), _sound, volume, 1.0f);
    }


    void giveAllPlayersEffect(PotionEffect _potion) { for (Player p : Bukkit.getOnlinePlayers()) p.addPotionEffect(_potion, true); }

    String repeatChar(int n, char c) { return new String(new char[n]).replace('\0', c); }

    void godbotAct(int h, Player rndPlayer)
    {
        if (h >= 0) Bukkit.broadcastMessage(String.format(UPDATE_MSGS[h], rndPlayer.getDisplayName()));
        else if (h == -1) runServerCmd("cum");
        else if (h == -2)
        {
            Bukkit.broadcastMessage("imma make it day");
            runServerCmd("time set day");
        }
        else if (h == -3)
        {
            Bukkit.broadcastMessage("alright thats it, night time");
            runServerCmd("time set night");
        }
        else if (h == -4)
        {
            playToEveryone(music[getRnd(0, music.length-1)], Float.MAX_VALUE);
            Bukkit.broadcastMessage(MUSIC_MSGS[getRnd(0, MUSIC_MSGS.length-1)]);
        }
        else if (h == -5)
        {
            Bukkit.broadcastMessage("Boo!");
            playToEveryone(Sound.AMBIENT_CAVE, 1.0f);
        }
        else if (h == -6)
        {
            PotionEffectType pot = RND_POTS[getRnd(0, RND_POTS.length-1)];
            Bukkit.broadcastMessage("have some "+pot.getName().toLowerCase().replace('_', ' '));
            giveAllPlayersEffect(new PotionEffect(pot, 1000, 1));
        }
        else if (h == -7) Bukkit.broadcastMessage(rndPlayer.getDisplayName() + "'s pp: 8"+repeatChar(getRnd(1, 10), '=')+"D");
        else if (h == -8)
        {
            Bukkit.broadcastMessage(String.format(SNEEZE_MSGS[getRnd(0, SNEEZE_MSGS.length-1)], rndPlayer.getDisplayName()));
            World theworld = rndPlayer.getWorld();
            theworld.spawnParticle(Particle.SNEEZE, rndPlayer.getEyeLocation(), 10, 0.1, 0, 0.1, 0.1, null, true);
            theworld.playSound(rndPlayer.getEyeLocation(), Sound.ENTITY_PANDA_SNEEZE, 10.0f, 0.01f);
        }
        else if (h == -9)
        {
            ChatColor oldChatColor = godColor;
            godColor = godColors[getRnd(0, godColors.length-1)];
            String colorNameStr = godColor.name().toLowerCase();
            String txt = "yo im "+colorNameStr+" now";
            if (oldChatColor == godColor) txt = "im still "+colorNameStr+"!";
            Bukkit.broadcastMessage(txt);
        }
        else if (h == -10)
        {
            godcast_withcolor("woah... " + ChatColor.stripColor(rndPlayer.getDisplayName()) + " just "+ChatColor.MAGIC+" 762dgiuyf2wi762fyfr");
            rndPlayer.sendTitle(ChatColor.MAGIC+"uigefiweu", "", 100, 100, 100);
        }
        else if (h == -11)
        {
            for (Player p : Bukkit.getOnlinePlayers()) runServerCmd("givedrug fard "+p.getName());
            Bukkit.broadcastMessage("have some drugs");
        }
    }


    void update()
    {
        Object[] players = Bukkit.getOnlinePlayers().toArray();
        if (players.length > 0) godbotAct(getRnd(-20, UPDATE_MSGS.length-1), (Player)players[getRnd(0, players.length-1)]);
    }
}
