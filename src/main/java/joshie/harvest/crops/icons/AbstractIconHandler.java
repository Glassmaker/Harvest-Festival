package joshie.harvest.crops.icons;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropRenderHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public abstract class AbstractIconHandler implements ICropRenderHandler {
    protected IIcon[] stageIcons;
    
    @Override
    public boolean doCustomRender(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block) {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnStage(Block block, PlantSection section, ICrop crop, int stage) {
        if (section == PlantSection.TOP || (section == PlantSection.BOTTOM && !crop.isDouble(stage))) {
            block.setBlockBounds(0F, 0F, 0F, 1F, 0.25F, 1F);
        } else block.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
    }
}
