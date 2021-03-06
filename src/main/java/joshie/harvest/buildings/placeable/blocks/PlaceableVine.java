package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableVine extends PlaceableBlock {
    public PlaceableVine(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 1) {
            if (n2) {
                return swap ? 2 : 4;
            } else if (swap) {
                return 8;
            }
        } else if (meta == 4) {
            if (n2) {
                return swap ? 8 : 1;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 8) {
            if (n1) {
                return swap ? 4 : 2;
            } else if (swap) {
                return 1;
            }
        } else if (meta == 2) {
            if (n1) {
                return swap ? 1 : 8;
            } else if (swap) {
                return 4;
            }
        }

        return meta;
    }
}
