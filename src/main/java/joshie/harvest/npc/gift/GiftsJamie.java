package joshie.harvest.npc.gift;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsJamie extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.diamond) {
            return Quality.AWESOME;
        }

        if (is(stack, Category.RARE)) {
            return Quality.GOOD;
        }

        if (is(stack, Category.ANIMALS)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
