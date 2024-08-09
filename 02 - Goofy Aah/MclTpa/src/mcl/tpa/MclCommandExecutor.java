package mcl.tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

abstract class MclCommandExecutor implements CommandExecutor
{
    public abstract void handleCommand(Player player, Command cmd, String label, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof Player player) handleCommand(player, cmd, label, args);
        else System.out.println("This command can only be used by a player.");
        return true;
    }
}
