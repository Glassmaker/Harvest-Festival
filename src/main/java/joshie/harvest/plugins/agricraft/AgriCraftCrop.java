package joshie.harvest.plugins.agricraft;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.crops.Crop;
import net.minecraft.item.ItemStack;

import com.InfinityRaider.AgriCraft.blocks.BlockModPlant;
import com.InfinityRaider.AgriCraft.items.ItemModSeed;

import cpw.mods.fml.common.registry.GameRegistry;

public class AgriCraftCrop extends Crop {
    private BlockModPlant plant;
    private String seeds;
    private String block;

    public AgriCraftCrop(String unlocalized, int stages, int regrow, int color, Season... seasons) {
        super("agricraft:" + unlocalized, seasons, 0, 0, stages, regrow, 0, color);
        this.block = unlocalized;
    }

    @Override
    public ItemStack getCropStack() {
        return plant.getFruits().get(0);
    }

    @Override
    public String getSeedsName() {
        if (seeds != null) return seeds;
        else {
            seeds = new ItemStack((ItemModSeed) plant.getSeed()).getDisplayName();
            return seeds;
        }
    }

    @Override
    public boolean hasItemAssigned() {
        return true;
    }

    public void loadItem() {
        plant = (BlockModPlant) GameRegistry.findBlock("AgriCraft", "crop" + block);        
        setCropIconHandler(new IconHandlerAgriCraft(plant, getStages()));
        ItemModSeed seed = (ItemModSeed) plant.getSeed();
        if (seed != null) {
            seeds = new ItemStack(seed).getDisplayName();
        }
    }
}
