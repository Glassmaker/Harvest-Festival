package joshie.harvest.cooking.entity;

import java.util.Random;

import joshie.harvest.api.cooking.ICookingAltIcon;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCookingItem extends Render {
    private static final ResourceLocation field_110798_h = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private RenderBlocks itemRenderBlocks = new RenderBlocks();

    /** The RNG used in RenderItem (for bobbing itemstacks on the ground) */
    private Random random = new Random();
    public boolean renderWithColor = true;

    /** Defines the zLevel of rendering of item on GUI. */
    public float zLevel;
    public static boolean renderInFrame;

    public RenderCookingItem() {
        shadowSize = 0.15F;
        shadowOpaque = 0.75F;
    }

    /**
     * Renders the item
     */
    public void doRenderItem(EntityCookingItem entity, double par2, double par4, double par6, float par8, float par9) {
        renderInFrame = true;
        bindEntityTexture(entity);
        random.setSeed(187L);
        ItemStack itemstack = entity.getEntityItem();
        if (itemstack.getItem() != null) {
            GL11.glPushMatrix();
            float f2 = 0F;
            float f3 = 0F;
            byte b0 = getMiniBlockCount(itemstack);

            GL11.glTranslatef((float) par2, (float) par4 + f2, (float) par6);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float f4;
            float f5;
            float f6;
            int i;

            //field_147909_c = renderBlocks
            if (ForgeHooksClient.renderEntityItem(entity, itemstack, f2, f3, random, renderManager.renderEngine, field_147909_c, b0)) {
                ;
            } else if (itemstack.getItemSpriteNumber() == 0 && itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
                Block block = Block.getBlockFromItem(itemstack.getItem());
                GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);

                if (renderInFrame) {
                    GL11.glScalef(1.25F, 1.25F, 1.25F);
                    GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                float f7 = 0.25F;
                int j = block.getRenderType();

                if (j == 1 || j == 19 || j == 12 || j == 2) {
                    f7 = 0.5F;
                }

                GL11.glScalef(f7, f7, f7);

                for (i = 0; i < b0; ++i) {
                    GL11.glPushMatrix();

                    if (i > 0) {
                        f5 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                        f4 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                        f6 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                        GL11.glTranslatef(f5, f4, f6);
                    }

                    f5 = 1.0F;
                    itemRenderBlocks.renderBlockAsItem(block, itemstack.getItemDamage(), f5);
                    GL11.glPopMatrix();
                }
            } else {
                float f8;

                if (itemstack.getItemSpriteNumber() == 1 && itemstack.getItem().requiresMultipleRenderPasses()) {
                    if (renderInFrame) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    ICookingAltIcon alt = !entity.isFinishedProduct && itemstack.getItem() instanceof ICookingAltIcon? (ICookingAltIcon) itemstack.getItem() : null;
                    for (int k = 0; k < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++k) {
                        random.setSeed(187L);
                        IIcon icon = alt == null ? itemstack.getItem().getIcon(itemstack, k): alt.getCookingIcon(itemstack, k);
                        f8 = 1.0F;

                        if (renderWithColor) {
                            i = itemstack.getItem().getColorFromItemStack(itemstack, k);
                            f5 = (i >> 16 & 255) / 255.0F;
                            f4 = (i >> 8 & 255) / 255.0F;
                            f6 = (i & 255) / 255.0F;
                            GL11.glColor4f(f5 * f8, f4 * f8, f6 * f8, 1.0F);
                            this.renderDroppedItem(entity, icon, b0, par9, f5 * f8, f4 * f8, f6 * f8, k);
                        } else {
                            this.renderDroppedItem(entity, icon, b0, par9, 1.0F, 1.0F, 1.0F, k);
                        }
                    }
                } else {
                    if (renderInFrame) {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    } else {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    ICookingAltIcon alt = !entity.isFinishedProduct && itemstack.getItem() instanceof ICookingAltIcon? (ICookingAltIcon) itemstack.getItem() : null;
                    IIcon icon1 = alt == null? itemstack.getIconIndex() : alt.getCookingIcon(itemstack, 0);
                    if (renderWithColor) {
                        int l = itemstack.getItem().getColorFromItemStack(itemstack, 0);
                        f8 = (l >> 16 & 255) / 255.0F;
                        float f9 = (l >> 8 & 255) / 255.0F;
                        f5 = (l & 255) / 255.0F;
                        f4 = 1.0F;
                        this.renderDroppedItem(entity, icon1, b0, par9, f8 * f4, f9 * f4, f5 * f4);
                    } else {
                        this.renderDroppedItem(entity, icon1, b0, par9, 1.0F, 1.0F, 1.0F);
                    }
                }
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getEntityTexture(EntityItem entity) {
        return renderManager.renderEngine.getResourceLocation(entity.getEntityItem().getItemSpriteNumber());
    }

    private void renderDroppedItem(EntityItem par1EntityItem, IIcon par2Icon, int par3, float par4, float par5, float par6, float par7) {
        renderDroppedItem(par1EntityItem, par2Icon, par3, par4, par5, par6, par7, 0);
    }

    private void renderDroppedItem(EntityItem par1EntityItem, IIcon par2Icon, int par3, float par4, float par5, float par6, float par7, int pass) {
        Tessellator tessellator = Tessellator.instance;

        if (par2Icon == null) {
            TextureManager texturemanager = MCClientHelper.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(par1EntityItem.getEntityItem().getItemSpriteNumber());
            par2Icon = ((TextureMap) texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        float f4 = par2Icon.getMinU();
        float f5 = par2Icon.getMaxU();
        float f6 = par2Icon.getMinV();
        float f7 = par2Icon.getMaxV();
        float f8 = 1.0F;
        float f9 = 0.5F;
        float f10 = 0.25F;
        float f11;

        GL11.glPushMatrix();

        if (renderInFrame) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        } else {
            GL11.glRotatef(((par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        }

        float f12 = 0.0625F;
        f11 = 0.021875F;
        ItemStack itemstack = par1EntityItem.getEntityItem();
        int j = itemstack.stackSize;
        byte b0 = getMiniItemCount(itemstack);

        GL11.glTranslatef(-f9, -f10, -((f12 + f11) * b0 / 2.0F));

        for (int k = 0; k < b0; ++k) {
            // Makes items offset when in 3D, like when in 2D, looks much better. Considered a vanilla bug...
            if (k > 0 && shouldSpreadItems()) {
                float x = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                float y = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                float z = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                GL11.glTranslatef(x, y, f12 + f11);
            } else {
                GL11.glTranslatef(0f, 0f, f12 + f11);
            }

            if (itemstack.getItemSpriteNumber() == 0) {
                bindTexture(TextureMap.locationBlocksTexture);
            } else {
                bindTexture(TextureMap.locationItemsTexture);
            }

            GL11.glColor4f(par5, par6, par7, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, f5, f6, f4, f7, par2Icon.getIconWidth(), par2Icon.getIconHeight(), f12);

            if (itemstack.hasEffect(pass)) {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                renderManager.renderEngine.bindTexture(field_110798_h);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                float f13 = 0.76F;
                GL11.glColor4f(0.5F * f13, 0.25F * f13, 0.8F * f13, 1.0F);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glPushMatrix();
                float f14 = 0.125F;
                GL11.glScalef(f14, f14, f14);
                float f15 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
                GL11.glTranslatef(f15, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f12);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f14, f14, f14);
                f15 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
                GL11.glTranslatef(-f15, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f12);
                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }
        }

        GL11.glPopMatrix();

    }

    /**
     * Renders the item's icon or block into the UI at the specified position.
     */
    public void renderItemIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5) {
        renderItemIntoGUI(par1FontRenderer, par2TextureManager, par3ItemStack, par4, par5, false);
    }

    public void renderItemIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5, boolean renderEffect) {
        int l = par3ItemStack.getItemDamage();
        Object object = par3ItemStack.getIconIndex();
        float f;
        int i1;
        float f1;
        float f2;

        if (par3ItemStack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(par3ItemStack.getItem()).getRenderType())) {
            par2TextureManager.bindTexture(TextureMap.locationBlocksTexture);
            Block block = Block.getBlockFromItem(par3ItemStack.getItem());
            GL11.glPushMatrix();
            GL11.glTranslatef(par4 - 2, par5 + 3, -3.0F + zLevel);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            i1 = par3ItemStack.getItem().getColorFromItemStack(par3ItemStack, 0);
            f = (i1 >> 16 & 255) / 255.0F;
            f1 = (i1 >> 8 & 255) / 255.0F;
            f2 = (i1 & 255) / 255.0F;

            if (renderWithColor) {
                GL11.glColor4f(f, f1, f2, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            itemRenderBlocks.useInventoryTint = renderWithColor;
            itemRenderBlocks.renderBlockAsItem(block, l, 1.0F);
            itemRenderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        } else if (par3ItemStack.getItem().requiresMultipleRenderPasses()) {
            GL11.glDisable(GL11.GL_LIGHTING);

            Item item = par3ItemStack.getItem();
            for (int j1 = 0; j1 < item.getRenderPasses(l); ++j1) {
                par2TextureManager.bindTexture(par3ItemStack.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
                IIcon icon = item.getIcon(par3ItemStack, l);
                int k1 = par3ItemStack.getItem().getColorFromItemStack(par3ItemStack, l);
                f1 = (k1 >> 16 & 255) / 255.0F;
                f2 = (k1 >> 8 & 255) / 255.0F;
                float f3 = (k1 & 255) / 255.0F;

                if (renderWithColor) {
                    GL11.glColor4f(f1, f2, f3, 1.0F);
                }

                renderIcon(par4, par5, icon, 16, 16);

                if (par3ItemStack.hasEffect(j1)) {
                    renderEffect(par2TextureManager, par4, par5);
                }
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
            ResourceLocation resourcelocation = par2TextureManager.getResourceLocation(par3ItemStack.getItemSpriteNumber());
            par2TextureManager.bindTexture(resourcelocation);

            if (object == null) {
                object = ((TextureMap) MCClientHelper.getMinecraft().getTextureManager().getTexture(resourcelocation)).getAtlasSprite("missingno");
            }

            i1 = par3ItemStack.getItem().getColorFromItemStack(par3ItemStack, 0);
            f = (i1 >> 16 & 255) / 255.0F;
            f1 = (i1 >> 8 & 255) / 255.0F;
            f2 = (i1 & 255) / 255.0F;

            if (renderWithColor) {
                GL11.glColor4f(f, f1, f2, 1.0F);
            }

            renderIcon(par4, par5, (IIcon) object, 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);

            if (par3ItemStack.hasEffect(0)) {
                renderEffect(par2TextureManager, par4, par5);
            }
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private void renderEffect(TextureManager manager, int x, int y) {
        GL11.glDepthFunc(GL11.GL_GREATER);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        manager.bindTexture(field_110798_h);
        zLevel -= 50.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
        GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
        renderGlint(x * 431278612 + y * 32178161, x - 2, y - 2, 20, 20);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        zLevel += 50.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }

    /**
     * Render the item's icon or block into the GUI, including the glint effect.
     */
    public void renderItemAndEffectIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5) {
        if (par3ItemStack != null) if (!ForgeHooksClient.renderInventoryItem(field_147909_c, par2TextureManager, par3ItemStack, renderWithColor, zLevel, par4, par5)) {
            this.renderItemIntoGUI(par1FontRenderer, par2TextureManager, par3ItemStack, par4, par5, true);
        }
    }

    private void renderGlint(int par1, int par2, int par3, int par4, int par5) {
        for (int j1 = 0; j1 < 2; ++j1) {
            if (j1 == 0) {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            if (j1 == 1) {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            float f = 0.00390625F;
            float f1 = 0.00390625F;
            float f2 = Minecraft.getSystemTime() % (3000 + j1 * 1873) / (3000.0F + j1 * 1873) * 256.0F;
            float f3 = 0.0F;
            Tessellator tessellator = Tessellator.instance;
            float f4 = 4.0F;

            if (j1 == 1) {
                f4 = -1.0F;
            }

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(par2 + 0, par3 + par5, zLevel, (f2 + par5 * f4) * f, (f3 + par5) * f1);
            tessellator.addVertexWithUV(par2 + par4, par3 + par5, zLevel, (f2 + par4 + par5 * f4) * f, (f3 + par5) * f1);
            tessellator.addVertexWithUV(par2 + par4, par3 + 0, zLevel, (f2 + par4) * f, (f3 + 0.0F) * f1);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, zLevel, (f2 + 0.0F) * f, (f3 + 0.0F) * f1);
            tessellator.draw();
        }
    }

    public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5) {
        this.renderItemOverlayIntoGUI(par1FontRenderer, par2TextureManager, par3ItemStack, par4, par5, (String) null);
    }

    public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5, String par6Str) {
        if (par3ItemStack != null) {
            if (par3ItemStack.stackSize > 1 || par6Str != null) {
                String s1 = par6Str == null ? String.valueOf(par3ItemStack.stackSize) : par6Str;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                par1FontRenderer.drawStringWithShadow(s1, par4 + 19 - 2 - par1FontRenderer.getStringWidth(s1), par5 + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

            if (par3ItemStack.isItemDamaged()) {
                int k = (int) Math.round(13.0D - par3ItemStack.getItemDamageForDisplay() * 13.0D / par3ItemStack.getMaxDamage());
                int l = (int) Math.round(255.0D - par3ItemStack.getItemDamageForDisplay() * 255.0D / par3ItemStack.getMaxDamage());
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                Tessellator tessellator = Tessellator.instance;
                int i1 = 255 - l << 16 | l << 8;
                int j1 = (255 - l) / 4 << 16 | 16128;
                renderQuad(tessellator, par4 + 2, par5 + 13, 13, 2, 0);
                renderQuad(tessellator, par4 + 2, par5 + 13, 12, 1, j1);
                renderQuad(tessellator, par4 + 2, par5 + 13, k, 1, i1);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6) {
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setColorOpaque_I(par6);
        par1Tessellator.addVertex(par2 + 0, par3 + 0, 0.0D);
        par1Tessellator.addVertex(par2 + 0, par3 + par5, 0.0D);
        par1Tessellator.addVertex(par2 + par4, par3 + par5, 0.0D);
        par1Tessellator.addVertex(par2 + par4, par3 + 0, 0.0D);
        par1Tessellator.draw();
    }

    public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par5, zLevel, par3Icon.getMinU(), par3Icon.getMaxV());
        tessellator.addVertexWithUV(par1 + par4, par2 + par5, zLevel, par3Icon.getMaxU(), par3Icon.getMaxV());
        tessellator.addVertexWithUV(par1 + par4, par2 + 0, zLevel, par3Icon.getMaxU(), par3Icon.getMinV());
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, zLevel, par3Icon.getMinU(), par3Icon.getMinV());
        tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return this.getEntityTexture((EntityItem) par1Entity);
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        doRenderItem((EntityCookingItem) par1Entity, par2, par4, par6, par8, par9);
    }

    public boolean shouldSpreadItems() {
        return true;
    }

    public byte getMiniBlockCount(ItemStack stack) {
        byte ret = 1;
        if (stack.stackSize > 1) {
            ret = 2;
        }
        if (stack.stackSize > 5) {
            ret = 3;
        }
        if (stack.stackSize > 20) {
            ret = 4;
        }
        if (stack.stackSize > 40) {
            ret = 5;
        }
        return ret;
    }

    public byte getMiniItemCount(ItemStack stack) {
        byte ret = 1;
        if (stack.stackSize > 1) {
            ret = 2;
        }
        if (stack.stackSize > 15) {
            ret = 3;
        }
        if (stack.stackSize > 31) {
            ret = 4;
        }
        return ret;
    }
}