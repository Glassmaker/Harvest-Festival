package joshie.harvest.items;

import static joshie.harvest.core.lib.HFModInfo.MEALPATH;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.api.cooking.IMealProvider;
import joshie.harvest.api.cooking.IMealRecipe;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.Translate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMeal extends ItemHFMeta implements IMealProvider, ICreativeSorted {
    private HashMap<String, IIcon> altMap = new HashMap();
    private HashMap<String, IIcon> iconMap = new HashMap();
    private IIcon[] burnt;
    
    public ItemMeal() {
        super(HFTab.tabCooking);
    }

    //Irrelevant since we overwrite them, but it needs it specified
    @Override
    public int getMetaCount() {
        return 1;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return Translate.translate("meal." + stack.stackTagCompound.getString("FoodName"));
        } else {
            int meta = Math.min(Utensil.values().length - 1, stack.getItemDamage());
            return Translate.translate("meal.burnt." + Utensil.values()[meta].name().replace("_", ".").toLowerCase());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        if (General.DEBUG_MODE) {
            if (stack.hasTagCompound()) {
                list.add(Translate.translate("meal.level") + " : " + stack.stackTagCompound.getInteger("FoodLevel"));
                list.add(Translate.translate("meal.sat") + " : " + stack.stackTagCompound.getFloat("FoodSaturation"));
                list.add(Translate.translate("meal.stamina") + " : " + stack.stackTagCompound.getInteger("FoodStamina"));
                list.add(Translate.translate("meal.fatigue") + " : " + stack.stackTagCompound.getInteger("FoodFatigue"));
            }
        }
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        if (stack.hasTagCompound()) {
            if (!player.capabilities.isCreativeMode) --stack.stackSize;
            int level = stack.stackTagCompound.getInteger("FoodLevel");
            float sat = stack.stackTagCompound.getFloat("FoodSaturation");
            int stamina = stack.stackTagCompound.getInteger("FoodStamina");
            int fatigue = stack.stackTagCompound.getInteger("FoodFatigue");
            HFTrackers.getPlayerTracker(player).getStats().affectStats(stamina, fatigue);
            player.getFoodStats().addStats(level, sat);
            world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

            return stack;
        } else return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return stack.stackTagCompound.getInteger("EatTime");
        } else return super.getMaxItemUseDuration(stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return stack.stackTagCompound.getBoolean("IsDrink") ? EnumAction.drink : EnumAction.eat;
        } else return EnumAction.none;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.canEat(false)) {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        }

        return stack;
    }

    //Apply all the relevant information about this meal to the meal stack
    public static ItemStack cook(ItemStack stack, IMeal meal) {
        stack.setTagCompound(new NBTTagCompound());
        stack.stackTagCompound.setString("FoodName", meal.getUnlocalizedName());
        stack.stackTagCompound.setInteger("FoodLevel", meal.getHunger());
        stack.stackTagCompound.setFloat("FoodSaturation", meal.getSaturation());
        stack.stackTagCompound.setInteger("FoodStamina", meal.getStamina());
        stack.stackTagCompound.setInteger("FoodFatigue", meal.getFatigue());
        stack.stackTagCompound.setBoolean("IsDrink", meal.isDrink());
        stack.stackTagCompound.setInteger("EatTime", meal.getEatTime());
        return stack;
    }

    @Override
    public String getMealName(ItemStack stack) {
        if (!stack.hasTagCompound()) return "burnt";
        return stack.stackTagCompound.getString("FoodName");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        int meta = Math.max(0, Math.min(Utensil.values().length - 1, damage));
        return burnt[meta];
    }
    
    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
    
    @Override
    public int getRenderPasses(int meta) {
        return 1;
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIconIndex(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconIndex(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return getIconFromDamage(stack.getItemDamage());
        }

        String key = stack.getTagCompound().getString("FoodName");
        IIcon icon = iconMap.get(key);
        return icon != null ? icon : getIconFromDamage(stack.getItemDamage());
    }
    
    @Override
    public IIcon getCookingIcon(ItemStack stack, int pass) {
        if (!stack.hasTagCompound()) {
            return getIconFromDamage(stack.getItemDamage());
        }
        
        String key = stack.getTagCompound().getString("FoodName");
        IIcon icon = altMap.get(key);
        return icon != null ? icon : getIconIndex(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(MEALPATH + "burnt");
        burnt = new IIcon[Utensil.values().length];
        for (Utensil u : Utensil.values()) {
            burnt[u.ordinal()] = register.registerIcon(MEALPATH + "burnt" + WordUtils.capitalize(u.name()));
        }

        for (IMealRecipe recipe : HFApi.COOKING.getRecipes()) {
            IMeal meal = recipe.getMeal();
            String key = meal.getUnlocalizedName();
            if (meal.hasAltTexture()) {
                altMap.put(key, register.registerIcon(MEALPATH + StringUtils.replace(key, ".", "_") + "_alt"));
            }
            
            iconMap.put(key, register.registerIcon(MEALPATH + StringUtils.replace(key, ".", "_")));
        }
    }
    
    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[] { HFTab.tabCooking };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creative, List list) {
        HashSet<IMeal> added = new HashSet();
        for (IMealRecipe recipe : HFApi.COOKING.getRecipes()) {
            IMeal best = recipe.getBestMeal();
            if (added.add(best)) {
                list.add(cook(new ItemStack(item), best));
            }
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 100;
    }
}
