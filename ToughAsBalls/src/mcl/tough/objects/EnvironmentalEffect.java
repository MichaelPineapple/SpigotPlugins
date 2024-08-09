package mcl.tough.objects;

public class EnvironmentalEffect
{
    private String name;
    private int value;

    public EnvironmentalEffect(int _value, String _name)
    {
        this.name = _name;
        this.value = _value;
    }

    public String getName()
    {
        return name;
    }
    public int getValue()
    {
        return value;
    }
}
