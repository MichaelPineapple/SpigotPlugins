package mcl.tough.managers;

import mcl.tough.objects.PlayerBalls;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BallsManager
{
    private final HashMap<UUID, PlayerBalls> ballsMap;

    public BallsManager()
    {
        ballsMap = new HashMap<>();
    }

    public boolean hasBalls(Player _player)
    {
        return ballsMap.containsKey(_player.getUniqueId());
    }

    public void addNewBalls(Player _player)
    {
        ballsMap.put(_player.getUniqueId(), new PlayerBalls());
    }

    public PlayerBalls getBalls(Player _player)
    {
        return ballsMap.get(_player.getUniqueId());
    }
}
