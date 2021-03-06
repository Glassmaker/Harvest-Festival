package joshie.harvest.mining.loot;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.util.SafeStack;
import joshie.harvest.items.HFItems;
import joshie.harvest.player.tracking.TrackingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class LootCursed extends LootChance {
    private static final SafeStack hoe = new SafeStack(new ItemStack(HFItems.hoe, 1, ToolTier.MYSTRIL.ordinal()));
    private static final SafeStack sickle = new SafeStack(new ItemStack(HFItems.sickle, 1, ToolTier.MYSTRIL.ordinal()));
    private static final SafeStack watering = new SafeStack(new ItemStack(HFItems.wateringcan, 1, ToolTier.MYSTRIL.ordinal()));
    
    public LootCursed(ItemStack stack, double chance) {
        super(stack, chance);
    }

    public boolean canPlayerObtain(EntityPlayer player) {
        TrackingData stats = HFTrackers.getPlayerTracker(player).getTracking();
        return stats.hasObtainedItem(hoe) && stats.hasObtainedItem(sickle) && stats.hasObtainedItem(watering);
    }
}
