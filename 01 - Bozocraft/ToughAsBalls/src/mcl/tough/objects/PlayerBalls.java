package mcl.tough.objects;

import java.util.ArrayList;

public class PlayerBalls
{
    public static float MAX_HYDRATION = 100;
    private float hydration = MAX_HYDRATION;
    private int bodyTemp = 0;
    private ArrayList<EnvironmentalEffect> temperatureEffects;

    public PlayerBalls()
    {
    }

    public float getHydration()
    {
        return hydration;
    }

    public int getBodyTemp()
    {
        return bodyTemp;
    }

    public void adjustHydration(float _val)
    {
        hydration += _val;
        if (hydration > MAX_HYDRATION) hydration = MAX_HYDRATION;
        if (hydration < 0) hydration = 0;
    }

    public void setTemperatureEffects(ArrayList<EnvironmentalEffect> _effects)
    {
        temperatureEffects = _effects;

        int temp = 0;
        for (EnvironmentalEffect effect: temperatureEffects)
        {
            temp += effect.getValue();
        }
        bodyTemp = temp;
    }

    public ArrayList<EnvironmentalEffect> getTemperatureEffects()
    {
        return temperatureEffects;
    }

}
