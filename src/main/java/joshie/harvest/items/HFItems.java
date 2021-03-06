package joshie.harvest.items;

import java.util.EnumMap;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.config.General;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.crops.Crop;
import joshie.harvest.items.render.RenderItemAnimal;
import joshie.harvest.items.render.RenderItemNPC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HFItems {
    public static EnumMap<SizeableMeta, Item> sized = new EnumMap(SizeableMeta.class);
    public static ItemAnimal animal;
    public static ItemTreat treats;
    public static Item seeds;
    public static Item general;
    public static Item meal;
    public static Item structures;
    public static Item spawnerNPC;
    public static Item spawnerAnimal;

    public static Item egg;
    public static Item milk;
    public static Item mayonnaise;
    public static Item wool;

    //Tool Items
    public static Item hoe;
    public static Item sickle;
    public static Item wateringcan;
    public static Item hammer;

    public static void preInit() {
        //Add a new crop item for things that do not have an item yet :D
        for (ICrop crop : Crop.crops) {
            if (!crop.hasItemAssigned()) {
                crop.setItem(new ItemStack(new ItemCrop(crop).setUnlocalizedName("crop." + crop.getUnlocalizedName()), 1, 0));
                ItemStack clone = crop.getCropStack().copy();
                clone.setItemDamage(OreDictionary.WILDCARD_VALUE);
                OreDictionary.registerOre("crop" + WordUtils.capitalizeFully(crop.getUnlocalizedName().replace("_", "")), clone);
            }
        }

        for (SizeableMeta size : SizeableMeta.values()) {
            if (size.ordinal() >= SizeableMeta.YOGHURT.ordinal()) continue;
            else {
                sized.put(size, size.getOrCreateStack());
            }
        }

        egg = sized.get(SizeableMeta.EGG);
        milk = sized.get(SizeableMeta.MILK);
        mayonnaise = sized.get(SizeableMeta.MAYONNAISE);
        wool = sized.get(SizeableMeta.WOOL);

        animal = (ItemAnimal) new ItemAnimal().setUnlocalizedName("animal");
        seeds = new ItemHFSeeds().setUnlocalizedName("crops.seeds");
        general = new ItemGeneral().setUnlocalizedName("general.item");
        meal = new ItemMeal().setUnlocalizedName("meal");
        treats = (ItemTreat) new ItemTreat().setUnlocalizedName("treat");

        /* Tools **/
        hoe = new ItemHoe().setUnlocalizedName("hoe");
        sickle = new ItemSickle().setUnlocalizedName("sickle");
        wateringcan = new ItemWateringCan().setUnlocalizedName("wateringcan");
        hammer = new ItemHammer().setUnlocalizedName("hammer");

        //Creative Mod Items
        structures = new ItemBuilding().setUnlocalizedName("structures");
        spawnerNPC = new ItemNPCSpawner().setUnlocalizedName("spawner.npc");

        if (General.DEBUG_MODE) {
            new ItemCheat().setUnlocalizedName("cheat");
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void initClient() {
        MinecraftForgeClient.registerItemRenderer(HFItems.animal, new RenderItemAnimal());
        MinecraftForgeClient.registerItemRenderer(HFItems.spawnerNPC, new RenderItemNPC());
    }
}
