package mcl.god;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Random;

public class Toolkit
{


    private static ChatColor godColor = ChatColor.LIGHT_PURPLE;

    public static void SET_GODCOLOR(ChatColor _newColor)
    {
        godColor = _newColor;
    }

    public static ChatColor GET_GODCOLOR()
    {
        return godColor;
    }

    public static String GET_GODCAST(String txt) { return (godColor + "" + ChatColor.BOLD + "<God> " + ChatColor.WHITE + "" + ChatColor.ITALIC + txt); }
    public static void GODCAST_WITHCOLOR(String txt) { for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(GET_GODCAST(txt)); }

    private static Random rnd = new Random();
    public static int GET_RND(int min, int max)
    {
        return rnd.nextInt((max - min) + 1) + min;
    }

    public static void RUN_SERVER_CMD(String str)
    {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), str);
    }

    public static String REPEAT_CHAR(int n, char c)
    {
        return new String(new char[n]).replace('\0', c);
    }

    public static void LOG(String _str)
    {
        System.out.println("GOD: " + _str);
    }
}
