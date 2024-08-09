package mcl.tpa;

import org.bukkit.entity.Player;
import java.time.Instant;

public class TpaRequest
{
    private final Player requestingPlayer;
    private final Instant time;

    public TpaRequest(Player _requestingPlayer, Instant _time)
    {
        this.requestingPlayer = _requestingPlayer;
        this.time = _time;
    }

    public Player getRequestingPlayer()
    {
        return this.requestingPlayer;
    }
    public Instant getTime()
    {
        return this.time;
    }
}