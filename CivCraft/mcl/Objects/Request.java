package mcl.Objects;

import org.bukkit.entity.Player;

public class Request
{
    private Player reciever, sender;

    public Request(Player _reciever, Player _sender)
    {
        this.reciever = _reciever;
        this.sender = _sender;
    }

    public Player getReciever() { return reciever; }
    public Player getSender() { return sender; }
}
