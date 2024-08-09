package mcl.drugs;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;
import org.bukkit.entity.Player;

public interface Drug
{
    void add(Plugin context);
    void takeDrug(Plugin context, Player p);
    ItemStack getItem();
    String getLocalName();
}
