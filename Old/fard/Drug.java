package fardrugs;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;
import org.bukkit.entity.Player;


public interface Drug
{
    public void add(Plugin context);
    public void takeDrug(Plugin context, Player p);
    public ItemStack getItem();
    public String getLocalName();
}
