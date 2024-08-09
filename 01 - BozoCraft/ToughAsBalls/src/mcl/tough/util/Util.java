package mcl.tough.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Util
{
    public static final String NO_BALLS_MSG = "YOU HAVE NO BALLS! TELL MICHAEL!";

    public static void displayTxt(Player _p, String _txt)
    {
        String playerName = _p.getName();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+playerName+" actionbar {\"text\":\""+_txt+"\"}");
    }

    public static void displayTitle(Player _p, String _txt)
    {
        _p.sendTitle(" ", _txt, 10, 10, 10);
    }


    public static String getItemKey(ItemStack _item)
    {
        String output = null;
        if (_item != null)
        {
            ItemMeta meta = _item.getItemMeta();
            if (meta != null) output = meta.getLocalizedName();
        }
        return output;
    }

}
