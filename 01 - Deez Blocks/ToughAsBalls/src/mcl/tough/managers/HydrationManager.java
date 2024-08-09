package mcl.tough.managers;

import mcl.tough.objects.PlayerBalls;
import mcl.tough.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HydrationManager
{
    private final BallsManager ballsContext;

    public HydrationManager(BallsManager _ballsContext)
    {
        this.ballsContext = _ballsContext;
    }

    public void adjustHydration(Player _player, float _val, boolean _forceShow)
    {
        PlayerBalls balls = ballsContext.getBalls(_player);
        if (balls != null)
        {
            balls.adjustHydration(_val);
            float hydration = balls.getHydration();

            if (hydration < 20 || _forceShow)
            {
                Util.displayTitle(_player, FORMAT_HYDRATION(hydration));
            }
        }
        else _player.sendMessage(Util.NO_BALLS_MSG);
    }


    public static String FORMAT_HYDRATION(float _val)
    {
        float barStep = 2.0f;
        int simpleMaxHydration = (int)(PlayerBalls.MAX_HYDRATION / barStep);
        int simpleHydration = (int)(_val/barStep);
        int diff = simpleMaxHydration - simpleHydration;
        return "H2O"+ ChatColor.AQUA + " " + "|".repeat(simpleHydration) + ChatColor.RESET + "" + "|".repeat(diff);
    }
}
