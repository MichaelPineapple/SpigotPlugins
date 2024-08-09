package mcl.god;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Config
{

    public static ArrayList<String> JOIN_MSGS = new ArrayList<>(Arrays.asList(
            
            "%s has arrived",
            "%s connected to the server.",
            "shit, %s is here",
            "%s joined the game",
            "%s just joined, they must be lost...",
            "Hello %s, please announce your pronouns",
            "Umm... who invited %s?",
            "hey %s, m/f?",
            "someone joined but im not gonna say who",
            "sup %s",
            "%s joined. thats pretty cringe"
    ));

    public static ArrayList<String> QUIT_MSGS = new ArrayList<>(Arrays.asList(
            
            "%s quit. RIP",
            "damn, %s just left",
            "%s disconnected from the server.",
            "%s left the game",
            "WOW. %s rage quit",
            "Goodbye %s",
            "There goes %s",
            "%s is no longer with us...",
            "%s doesnt like this server",
            "%s quit. Too much cum?"
    ));

    public static ArrayList<String> DIE_MSGS = new ArrayList<>(Arrays.asList(
            
            "lmao %s. Press F to pay respects",
            "yikes, %s",
            "%s. oof",
            "lol %s",
            "bruh moment: %s",
            "%s. thats pretty cringe bro",
            "%s. retard",
            "%s. really chief?",
            "oh my god! %s!",
            "%s. can i get an F in the chat?",
            "bro %s",
            "lol %s     https://www.youtube.com/watch?v=dwLCjZVEtpE"
    ));

    public static ArrayList<String> CHAT_MSGS = new ArrayList<>(Arrays.asList(
            
            "Hows it going?",
            "So this is how you spend your free time?",
            "crunch",
            "try /cum",
            "tfw ur god",
            "nice house %s",
            "my pp: 8=============D",
            "uwu",
            "owo",
            "i bet %s unironically likes minecraft",
            "u_u",
            "h",
            "E",
            "B-Topia was better",
            "hmmmm",
            "w",
            "reeeeeeeee",
            "WARNING: %s IS GAY",
            "hey %s, how are you?",
            "this server sucks",
            "my bros, check out this video: https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            "this fard stuff is wack. dont do kids",
            "i see you"
    ));

    public static ArrayList<String> MUSIC_MSGS = new ArrayList<>(Arrays.asList(
            
            "This song is a classic",
            "yo pass the aux",
            "this is my jam",
            "yall best be ready for this absolute bop",
            "guys you gotta listen to this",
            "VOLUME UP LADIES",
            "music time",
            "im boutta bust a funky lyric",
            "lets get these jams",
            "yall mind if i play some music?"
    ));

    public static ArrayList<String> SNEEZE_MSGS = new ArrayList<>(Arrays.asList(
            
            "damn %s cover your nose",
            "bless you",
            "gesundheit (@%s)",
            "%s sneezed"
    ));

    public static GodConfigData LOAD_CONFIG_DATA()
    {
        JOIN_MSGS = loadShidFile("join", JOIN_MSGS);
        QUIT_MSGS = loadShidFile("quit", QUIT_MSGS);
        DIE_MSGS = loadShidFile("die", DIE_MSGS);
        CHAT_MSGS = loadShidFile("godbot_chat", CHAT_MSGS);
        MUSIC_MSGS = loadShidFile("godbot_music", MUSIC_MSGS);
        SNEEZE_MSGS = loadShidFile("godbot_sneeze", SNEEZE_MSGS);

        GodConfigData $return = new GodConfigData();

        ArrayList<String> gocbotConfigFileData = new ArrayList<>(Arrays.asList(
                "// Delete this file to restore defaults",
                "",
                "// Godbot enabled",
                $return.enabled+"",
                "",
                "// Godbot refresh timer (ticks)",
                $return.timer+""
        ));

        gocbotConfigFileData = loadShidFile("godbot", gocbotConfigFileData);

        try { $return.enabled = (gocbotConfigFileData.get(3).toLowerCase().equals("true")); }
        catch (Exception ex) { Toolkit.LOG("Error parsing 'Godbot enabled' value! Enabling by default."); }

        try { $return.timer = Integer.parseInt(gocbotConfigFileData.get(6)); }
        catch (Exception ex) { Toolkit.LOG("Error parsing 'Godbot refresh timer' value! Resorting to default."); }

        return $return;
    }


    private static ArrayList<String> loadShidFile(String _filename, ArrayList<String> _defaultData)
    {
        String filepath = "godconfig/" + _filename + ".shid";
        ArrayList<String> $return = _defaultData;
        try
        {
            File f = new File(filepath);
            if (f.exists())
            {
                $return.clear();
                Scanner scanner = new Scanner(f);
                while (scanner.hasNextLine()) $return.add(scanner.nextLine());
            }
            else
            {
                Toolkit.LOG("'" + filepath + "' not found! Creating new file.");
                f.getParentFile().mkdirs();
                f.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(f.getCanonicalPath()));
                for (String s : _defaultData)
                {
                    writer.write(s);
                    writer.newLine();
                }
                writer.close();
            }
        }
        catch (Exception ex)
        {
            Toolkit.LOG("Error accessing '" + filepath + "'! Resorting to default values.");
        }

        if ($return.size() < 1) $return.add("[null]");

        return $return;
    }





}

class GodConfigData
{
    public boolean enabled = true;
    public int timer = 20000;
}

class GOD_CMDS
{
    public final static String
        ACT = "rnd",
        CHAT = "chat",
        COLOR = "color",
        POTION = "potion",
        PP = "pp",
        WTF = "wtf",
        MUSIC = "music",
        DAY = "day",
        NIGHT = "night",
        BOO = "boo",
        SNEEZE = "sneeze",
        CUM = "cum",
        FARD = "fard";
}