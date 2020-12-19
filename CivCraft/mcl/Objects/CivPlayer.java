package mcl.Objects;

import org.bukkit.entity.Player;

public class CivPlayer
{
    private String UUID;
    private String name;

    public CivPlayer(String _UUID, String _name)
    {
        this.UUID = _UUID;
        this.name = _name;
    }
    public CivPlayer(Player _p)
    {
        this.UUID = _p.getUniqueId().toString();
        this.name = _p.getName();
    }

    public String getName() { return name; }
    public String getUUID() { return UUID; }

    public boolean equals(CivPlayer _p)
    {
        return (_p.getUUID().equals(this.UUID));
    }
    public boolean equals(Player _p)
    {
        return (_p.getUniqueId().toString().equals(this.UUID));
    }

}
