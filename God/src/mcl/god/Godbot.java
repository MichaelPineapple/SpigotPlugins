package mcl.god;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Godbot
{
    private static PotionEffectType[] RND_POTS =
    {
            PotionEffectType.BAD_OMEN,
            PotionEffectType.BLINDNESS,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.REGENERATION,
            PotionEffectType.NAUSEA,
            PotionEffectType.SPEED,
            PotionEffectType.LUCK,
            PotionEffectType.JUMP_BOOST,
            PotionEffectType.REGENERATION,
            PotionEffectType.ABSORPTION,
            PotionEffectType.DOLPHINS_GRACE,
            PotionEffectType.HASTE,
            PotionEffectType.GLOWING,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.WATER_BREATHING,
            PotionEffectType.UNLUCK,
    };

    private static ChatColor[] godColors =
    {
            ChatColor.AQUA,
            ChatColor.BLUE,
            ChatColor.RED,
            ChatColor.LIGHT_PURPLE,
            ChatColor.GREEN,
    };

    private static Sound[] music =
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
    
    private static int godbotTask = -1;
    
    public static void INIT_GODBOT(Plugin _context, int _updateTimer)
    {
        godbotTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(_context, new Runnable()
        {
            public void run() { update(); }
        }, _updateTimer, _updateTimer);
    }

    private static void playToEveryone(Sound _sound, float volume)
    {
        for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), _sound, volume, 1.0f);
    }

    private static void giveAllPlayersEffect(PotionEffect _potion) 
    { 
        for (Player p : Bukkit.getOnlinePlayers()) p.addPotionEffect(_potion, true); 
    }

    private static Player getRandomPlayer()
    {
        Player $return = null;
        Object[] players = Bukkit.getOnlinePlayers().toArray();
        if (players.length > 0) $return =(Player)players[Toolkit.GET_RND(0, players.length-1)];
        return $return;
    }

    private static void update()
    {
        if (Toolkit.GET_RND(0, 20) > 10)
        {
            Player tmp = getRandomPlayer();
            if (tmp != null) godbotAct(getRandomPlayer());
        }
    }

    public static class GodbotCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender.isOp())
            {
                if (args.length > 0)
                {
                    Player p = getRandomPlayer();
                    if (args.length > 1)  p = Bukkit.getPlayer(args[1]);
                    if (p != null)
                    {
                        String act = args[0].toLowerCase();
                        if (act.equals(GOD_CMDS.ACT)) godbotAct(p);
                        else if (act.equals(GOD_CMDS.SNEEZE)) sneeze(p);
                        else if (act.equals(GOD_CMDS.DAY)) day();
                        else if (act.equals(GOD_CMDS.NIGHT)) night();
                        else if (act.equals(GOD_CMDS.CUM)) cum();
                        else if (act.equals(GOD_CMDS.BOO)) boo();
                        else if (act.equals(GOD_CMDS.POTION)) potion();
                        else if (act.equals(GOD_CMDS.PP)) pp(p);
                        else if (act.equals(GOD_CMDS.FARD)) fard();
                        else if (act.equals(GOD_CMDS.WTF)) wtf(p);
                        else if (act.equals(GOD_CMDS.COLOR)) color();
                        else if (act.equals(GOD_CMDS.MUSIC)) music();
                        else if (act.equals(GOD_CMDS.CHAT)) chat(p);
                        else sender.sendMessage("Unknown command, try '/help god'");
                    }
                    else sender.sendMessage("Player not found!");
                }
                else sender.sendMessage("Unknown command, try '/help god'");
            }
            else sender.sendMessage("You are not god...");

            return true;
        }
    }


    // *** GODBOT ACTIONS *** //

    private static void sneeze(Player _p)
    {
        Bukkit.broadcastMessage(String.format(Config.SNEEZE_MSGS.get(Toolkit.GET_RND(0, Config.SNEEZE_MSGS.size()-1)), _p.getDisplayName()));
        Toolkit.RUN_SERVER_CMD("sneeze "+_p.getName());
    }

    private static void day()
    {
        Bukkit.broadcastMessage("imma make it day");
        Toolkit.RUN_SERVER_CMD("time set day");
    }

    private static void night()
    {
        Bukkit.broadcastMessage("alright thats it, night time");
        Toolkit.RUN_SERVER_CMD("time set night");
    }

    private static void cum()
    {
        Toolkit.RUN_SERVER_CMD("cum");
    }

    private static void boo()
    {
        Bukkit.broadcastMessage("Boo!");
        playToEveryone(Sound.AMBIENT_CAVE, 1.0f);
    }

    private static void potion()
    {
        PotionEffectType pot = RND_POTS[Toolkit.GET_RND(0, RND_POTS.length-1)];
        Bukkit.broadcastMessage("have some "+pot.getName().toLowerCase().replace('_', ' '));
        giveAllPlayersEffect(new PotionEffect(pot, 1000, 1));
    }

    private static void pp(Player _p)
    {
        Bukkit.broadcastMessage(_p.getDisplayName() + "'s pp: 8"+Toolkit.REPEAT_CHAR(Toolkit.GET_RND(1, 10), '=')+"D");
    }

    private static void fard()
    {
        for (Player p : Bukkit.getOnlinePlayers()) Toolkit.RUN_SERVER_CMD("givedrug fard "+p.getName());
        Bukkit.broadcastMessage("have some drugs");
    }

    private static void wtf(Player _p)
    {
        Toolkit.GODCAST_WITHCOLOR("woah... " + ChatColor.stripColor(_p.getDisplayName()) + " just "+ChatColor.MAGIC+" 762dgiuyf2wi762fyfr");
        _p.sendTitle(ChatColor.MAGIC+"uigefiweu", "", 100, 100, 100);
    }

    private static void color()
    {
        ChatColor oldChatColor = Toolkit.GET_GODCOLOR();
        Toolkit.SET_GODCOLOR(godColors[Toolkit.GET_RND(0, godColors.length-1)]);
        ChatColor localGodColor = Toolkit.GET_GODCOLOR();
        String colorNameStr = localGodColor.name().toLowerCase().replace('_', ' ');
        String txt = "yo im "+colorNameStr+" now";
        if (oldChatColor == localGodColor) txt = "im still "+colorNameStr+"!";
        Bukkit.broadcastMessage(txt);
    }

    private static void music()
    {
        playToEveryone(music[Toolkit.GET_RND(0, music.length-1)], Float.MAX_VALUE);
        Bukkit.broadcastMessage(Config.MUSIC_MSGS.get(Toolkit.GET_RND(0, Config.MUSIC_MSGS.size()-1)));
    }

    private static void chat(Player _p)
    {
        Bukkit.broadcastMessage(String.format(Config.CHAT_MSGS.get(Toolkit.GET_RND(0, Config.CHAT_MSGS.size()-1)), _p.getDisplayName()));
    }

    private static void godbotAct(Player rndPlayer)
    {
        int h = Toolkit.GET_RND(0, 11);
        if (h == 0) chat(rndPlayer);
        else if (h == 1) cum();
        else if (h == 2) day();
        else if (h == 3) night();
        else if (h == 4) music();
        else if (h == 5) boo();
        else if (h == 6) potion();
        else if (h == 7) pp(rndPlayer);
        else if (h == 8) sneeze(rndPlayer);
        else if (h == 9) color();
        else if (h == 10) wtf(rndPlayer);
        else if (h == 11) fard();
    }


}
