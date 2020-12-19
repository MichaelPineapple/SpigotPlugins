package mcl.Commands;

public class Commands
{
    /** USER COMMANDS **/
    public static final String
        FOUND_CITY = "settle",
        CLAIM_LAND = "claim",
        UNCLAIM_LAND = "unclaim",
        SHOW_LAND = "land",
        ADD_CITIZEN = "addcitizen",
        REMOVE_CITIZEN = "removecitizen",
        LIST_CITIES = "citylist",
        CITY_STATS = "citystats",
        ADD_ALLY = "ally",
        ALLY_ACCEPT = "allyaccept",
        ALLY_DENY = "allydeny",
        CITY_RENAME = "cityrename",
        LEAVE_ALLIANCE = "leavealliance",
        LEAVE_CITY = "leavecity",
        RESIGN = "resign",
        ADD_OFFICER = "addofficer",
        DEL_OFFICER = "removeofficer";

    /** ADMIN COMMANDS **/
    public static class Admin
    {
        public static final String
                DEL_CITY = "civadmin_delcity",
                CLEAR_DATA = "civadmin_clear",
                LOAD_DATA = "civadmin_load",
                UNLOAD_CHUNKS = "civadmin_unloadchunks";
    }
}
