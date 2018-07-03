package me.axieum.mcmod.pedestalcrafting.render;

import me.axieum.mcmod.pedestalcrafting.Settings;
import me.axieum.mcmod.pedestalcrafting.block.BlockPedestal;
import me.axieum.mcmod.pedestalcrafting.tile.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * @author BlakeBr0, manipulated by Axieum
 */

@SideOnly(Side.CLIENT)
public class RenderPedestal extends TileEntitySpecialRenderer<TilePedestal>
{
    @Override
    public void render(TilePedestal tile, double x, double y, double z, float partialTick, int destroyStage,
                       float alpha)
    {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());

        if (state == null || !(state.getBlock() instanceof BlockPedestal))
            return;

        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        if (!itemHandler.getStackInSlot(0).isEmpty())
        {
            final double tick = Minecraft.getSystemTime() / (1 / Settings.displaySpeed * 100);
            final float scale = (float) (itemHandler.getStackInSlot(0).getItem() instanceof ItemBlock ? 0.85F : 0.65);

            GlStateManager.pushMatrix();

            GlStateManager.translate(x + 0.5D, y + 1.4D, z + 0.5D);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            GlStateManager.rotate((float) (((tick * 40.0D) % 360)), 0, 1, 0);

            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getRenderItem().renderItem(itemHandler.getStackInSlot(0), TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();

            GlStateManager.popMatrix();
        }
    }
}