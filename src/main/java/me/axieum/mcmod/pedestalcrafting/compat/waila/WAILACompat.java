package me.axieum.mcmod.pedestalcrafting.compat.waila;

import mcp.mobius.waila.api.*;
import me.axieum.mcmod.pedestalcrafting.PedestalCrafting;
import me.axieum.mcmod.pedestalcrafting.block.BlockPedestal;
import me.axieum.mcmod.pedestalcrafting.block.BlockPedestalCore;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipe;
import me.axieum.mcmod.pedestalcrafting.tile.TilePedestal;
import me.axieum.mcmod.pedestalcrafting.tile.TilePedestalCore;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import javax.annotation.Nonnull;
import java.util.List;

public class WAILACompat implements IWailaDataProvider
{
    private static boolean registered;

    public static void register()
    {
        if (registered)
            return;

        registered = true;
        FMLInterModComms.sendFunctionMessage(
                "waila",
                "register",
                "me.axieum.mcmod." + PedestalCrafting.MOD_ID + ".compat.waila.WAILACompat.register"
        );
    }

    public static void register(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new WAILACompat(), BlockPedestal.class);
        registrar.registerBodyProvider(new WAILACompat(), BlockPedestalCore.class);
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        Block block = accessor.getBlock();
        TileEntity te = accessor.getTileEntity();

        if (block instanceof BlockPedestal && te instanceof TilePedestal && !te.isInvalid())
        {
            TilePedestal tilePedestal = (TilePedestal) te;
            ItemStack itemShown = tilePedestal.inventory.getStackInSlot(0);
            if (!itemShown.isEmpty())
                tooltip.add(itemShown.getDisplayName());
        }

        if (block instanceof BlockPedestalCore && te instanceof TilePedestalCore && !te.isInvalid())
        {
            TilePedestalCore tilePedestalCore = (TilePedestalCore) te;
            ItemStack itemShown = tilePedestalCore.inventory.getStackInSlot(0);
            PedestalRecipe recipe = tilePedestalCore.getRecipe();

            String renderStr = "";

            if (!itemShown.isEmpty())
                renderStr += SpecialChars.getRenderString(
                        "waila.stack",
                        "1",
                        itemShown.getItem().getRegistryName().toString()
                        , "1", String.valueOf(itemShown.getItemDamage())
                );

            if (recipe != null)
            {
                renderStr += SpecialChars.getRenderString(
                        "waila.progress",
                        String.valueOf(tilePedestalCore.elapsed),
                        String.valueOf(recipe.getTicks())
                );

                renderStr += SpecialChars.getRenderString(
                        "waila.stack",
                        "1",
                        recipe.getOutput().getItem().getRegistryName().toString()
                        , "1", String.valueOf(recipe.getOutput().getItemDamage())
                );
            }

            tooltip.add(renderStr);

            if (recipe == null && tilePedestalCore.pedestalCount > 0)
            {
                tooltip.add("Pedestals: " + tilePedestalCore.pedestalCount);
            }
        }

        return tooltip;
    }
}
