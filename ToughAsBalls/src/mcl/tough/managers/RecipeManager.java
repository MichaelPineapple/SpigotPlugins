package mcl.tough.managers;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import java.util.Arrays;

public class RecipeManager
{
    private final Plugin context;

    public RecipeManager(Plugin _context)
    {
        this.context = _context;
    }

    private static final RecipeChoice WOOL = new RecipeChoice.MaterialChoice(Arrays.asList(Material.WHITE_WOOL, Material.LIME_WOOL, Material.BLACK_WOOL,
            Material.BLUE_WOOL, Material.BROWN_WOOL, Material.YELLOW_WOOL, Material.RED_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL,
            Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_GRAY_WOOL, Material.LIGHT_BLUE_WOOL, Material.GREEN_WOOL,
            Material.GRAY_WOOL, Material.CYAN_WOOL));

    private static final RecipeChoice LEAVES = new RecipeChoice.MaterialChoice(Arrays.asList(Material.ACACIA_LEAVES, Material.AZALEA_LEAVES, Material.BIRCH_LEAVES,
            Material.SPRUCE_LEAVES, Material.OAK_LEAVES, Material.JUNGLE_LEAVES, Material.FLOWERING_AZALEA_LEAVES, Material.DARK_OAK_LEAVES));

    public void addRecipes()
    {
        addWoolArmour();
        addLeafArmour();
        addWetsuitArmour();
    }

    /** WETSUIT ARMOUR **/

    public static final String WETSUIT_CHEST_KEY = "wetsuitchest";
    public static final String WETSUIT_PANTS_KEY = "wetsuitpants";
    public static final String WETSUIT_BOOTS_KEY = "wetsuitboots";

    private void addWetsuitArmour()
    {
        // Wetsuit Chest
        ItemStack wetsuitChest = createLeatherArmour(Material.LEATHER_CHESTPLATE, "Wetsuit Chest", WETSUIT_CHEST_KEY, Color.BLACK);
        ShapedRecipe wetsuitChestRecipe = prepareRecipe(WETSUIT_CHEST_KEY, wetsuitChest);
        wetsuitChestRecipe.shape(" C ", "CXC", " C ");
        wetsuitChestRecipe.setIngredient('C', Material.COPPER_INGOT);
        wetsuitChestRecipe.setIngredient('X', Material.LEATHER_CHESTPLATE);
        Bukkit.addRecipe(wetsuitChestRecipe);

        // Wetsuit Pants
        ItemStack wetsuitPants = createLeatherArmour(Material.LEATHER_LEGGINGS, "Wetsuit Pants", WETSUIT_PANTS_KEY, Color.BLACK);
        ShapedRecipe wetsuitPantsRecipe = prepareRecipe(WETSUIT_PANTS_KEY, wetsuitPants);
        wetsuitPantsRecipe.shape(" C ", "CXC", " C ");
        wetsuitPantsRecipe.setIngredient('C', Material.COPPER_INGOT);
        wetsuitPantsRecipe.setIngredient('X', Material.LEATHER_LEGGINGS);
        Bukkit.addRecipe(wetsuitPantsRecipe);

        // Wetsuit Boots
        ItemStack wetsuitBoots = createLeatherArmour(Material.LEATHER_BOOTS, "Wetsuit Boots", WETSUIT_BOOTS_KEY, Color.BLACK);
        ShapedRecipe wetsuitBootsRecipe = prepareRecipe(WETSUIT_BOOTS_KEY, wetsuitBoots);
        wetsuitBootsRecipe.shape(" C ", "CXC", " C ");
        wetsuitBootsRecipe.setIngredient('C', Material.COPPER_INGOT);
        wetsuitBootsRecipe.setIngredient('X', Material.LEATHER_BOOTS);
        Bukkit.addRecipe(wetsuitBootsRecipe);
    }

    /** WOOL ARMOUR **/

    public static final String WOOL_HOOD_KEY = "woolhood";
    public static final String WOOL_JACKET_KEY = "wooljacket";
    public static final String WOOL_PANTS_KEY = "woolpants";
    public static final String WOOL_BOOTS_KEY = "woolboots";

    private void addWoolArmour()
    {
        // Wool Hood
        ItemStack woolHood = createLeatherArmour(Material.LEATHER_HELMET, "Wool Hood", WOOL_HOOD_KEY, Color.WHITE);
        ShapedRecipe hoodRecipe = prepareRecipe(WOOL_HOOD_KEY, woolHood);
        hoodRecipe.shape("WWW", "W W", "   ");
        hoodRecipe.setIngredient('W', WOOL);
        Bukkit.addRecipe(hoodRecipe);

        // Wool Jacket
        ItemStack woolJacket = createLeatherArmour(Material.LEATHER_CHESTPLATE, "Wool Jacket", WOOL_JACKET_KEY, Color.WHITE);
        ShapedRecipe jacketRecipe = prepareRecipe(WOOL_JACKET_KEY, woolJacket);
        jacketRecipe.shape("W W", "WWW", "WWW");
        jacketRecipe.setIngredient('W', WOOL);
        Bukkit.addRecipe(jacketRecipe);

        // Wool Pants
        ItemStack woolPants = createLeatherArmour(Material.LEATHER_LEGGINGS, "Wool Pants", WOOL_PANTS_KEY, Color.WHITE);
        ShapedRecipe pantsRecipe = prepareRecipe(WOOL_PANTS_KEY, woolPants);
        pantsRecipe.shape("WWW", "W W", "W W");
        pantsRecipe.setIngredient('W', WOOL);
        Bukkit.addRecipe(pantsRecipe);

        // Wool Boots
        ItemStack woolBoots = createLeatherArmour(Material.LEATHER_BOOTS, "Wool Boots", WOOL_BOOTS_KEY, Color.WHITE);
        ShapedRecipe bootsRecipe = prepareRecipe(WOOL_BOOTS_KEY, woolBoots);
        bootsRecipe.shape("   ", "W W", "W W");
        bootsRecipe.setIngredient('W', WOOL);
        Bukkit.addRecipe(bootsRecipe);
    }


    /** LEAF ARMOUR **/

    public static final String LEAF_HELMET_KEY = "leafhelmet";
    public static final String LEAF_CHEST_KEY = "leafchest";
    public static final String LEAF_PANTS_KEY = "leafpants";
    public static final String LEAF_BOOTS_KEY = "leafboots";

    private void addLeafArmour()
    {
        // Leaf Helmet
        ItemStack helmet = createLeatherArmour(Material.LEATHER_HELMET, "Leaf Hat", LEAF_HELMET_KEY, Color.GREEN);
        ShapedRecipe helmetRecipe = prepareRecipe(LEAF_HELMET_KEY, helmet);
        helmetRecipe.shape("WWW", "W W", "   ");
        helmetRecipe.setIngredient('W', LEAVES);
        Bukkit.addRecipe(helmetRecipe);

        // Leaf Chest
        ItemStack chest = createLeatherArmour(Material.LEATHER_CHESTPLATE, "Leaf Shirt", LEAF_CHEST_KEY, Color.GREEN);
        ShapedRecipe chestRecipe = prepareRecipe(LEAF_CHEST_KEY, chest);
        chestRecipe.shape("W W", "WWW", "WWW");
        chestRecipe.setIngredient('W', LEAVES);
        Bukkit.addRecipe(chestRecipe);

        // Leaf Pants
        ItemStack pants = createLeatherArmour(Material.LEATHER_LEGGINGS, "Leaf Pants", LEAF_PANTS_KEY, Color.GREEN);
        ShapedRecipe pantsRecipe = prepareRecipe(LEAF_PANTS_KEY, pants);
        pantsRecipe.shape("WWW", "W W", "W W");
        pantsRecipe.setIngredient('W', LEAVES);
        Bukkit.addRecipe(pantsRecipe);

        // Leaf Boots
        ItemStack boots = createLeatherArmour(Material.LEATHER_BOOTS, "Leaf Sandals", LEAF_BOOTS_KEY, Color.GREEN);
        ShapedRecipe bootsRecipe = prepareRecipe(LEAF_BOOTS_KEY, boots);
        bootsRecipe.shape("   ", "W W", "W W");
        bootsRecipe.setIngredient('W', LEAVES);
        Bukkit.addRecipe(bootsRecipe);
    }

    /** INTERNAL UTIL FUNCTIONS **/

    private ItemStack createLeatherArmour(Material _material, String _name, String _id, Color _color)
    {
        ItemStack item = new ItemStack(_material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null)
        {
            if (meta instanceof LeatherArmorMeta)
            {
                LeatherArmorMeta meta2 = (LeatherArmorMeta) meta;

                meta2.setLocalizedName(_id);
                meta2.setDisplayName(_name);
                meta2.setColor(_color);
                item.setItemMeta(meta);
            }
        }
        return item;
    }

    private ShapedRecipe prepareRecipe(String _key, ItemStack _item)
    {
        NamespacedKey saddleKey = new NamespacedKey(context, _key);
        return new ShapedRecipe(saddleKey, _item);
    }

}
