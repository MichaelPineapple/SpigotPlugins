package mcl.tpa;

import org.bukkit.entity.Player;
import java.time.Instant;
import java.util.HashMap;

public class RequestManager
{
    private final HashMap<Player, TpaRequest> pendingRequestsMap;

    public RequestManager()
    {
        pendingRequestsMap = new HashMap<>();
    }

    TpaRequest getRequest(Player _destinationPlayer)
    {
        TpaRequest output = null;

        if (pendingRequestsMap.containsKey(_destinationPlayer))
        {
            output = pendingRequestsMap.get(_destinationPlayer);
            pendingRequestsMap.remove(_destinationPlayer);
        }

        return output;
    }

    public void makeRequest(Player _player, Player _destinationPlayer)
    {
        pendingRequestsMap.put(_destinationPlayer, new TpaRequest(_player, Instant.now()));
    }
}