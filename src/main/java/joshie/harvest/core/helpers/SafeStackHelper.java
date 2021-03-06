package joshie.harvest.core.helpers;

import java.util.Collection;
import java.util.HashMap;

import joshie.harvest.api.cooking.IMealProvider;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.util.HFStack;
import joshie.harvest.core.util.MealStack;
import joshie.harvest.core.util.SafeStack;
import joshie.harvest.core.util.WildStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Multimap;

public class SafeStackHelper {
    public static SafeStack getSafeStackType(ItemStack stack) {
        if (stack.getItem() instanceof IMealProvider) {
            return new MealStack(stack);
        } if (stack.getItem() instanceof ICropProvider || stack.getItem() instanceof ISizedProvider) {
            return new HFStack(stack);
        } else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            return new WildStack(stack);
        } else return new SafeStack(stack);
    }
    
    public static Collection getResult(ItemStack stack, Multimap map) {
        Collection list = map.get(new SafeStack(stack));
        if (list.size() < 1) list = map.get(new MealStack(stack));
        if (list.size() < 1) list = map.get(new WildStack(stack));
        if (list.size() < 1) list = map.get(new HFStack(stack));
        return list;
    }
    
    public static Object getResult(ItemStack stack, HashMap map) {
        Object result = map.get(new SafeStack(stack));
        if (result == null) result = map.get(new MealStack(stack));
        if (result == null) result = map.get(new WildStack(stack));
        if (result == null) result = map.get(new HFStack(stack));
        return result;
    }
}
