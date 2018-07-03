package me.axieum.mcmod.pedestalcrafting.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks
{
    public static final BlockPedestal PEDESTAL = new BlockPedestal("pedestal");
    public static final BlockPedestalCore PEDESTAL_CORE = new BlockPedestalCore("pedestal_core");

    public static void register(IForgeRegistry<Block> registry)
    {
        registry.registerAll(
                PEDESTAL,
                PEDESTAL_CORE
        );

        registerTiles();
    }

    public static void registerTiles()
    {
        // Pedestal
        GameRegistry.registerTileEntity(
                PEDESTAL.getTileEntityClass(),
                PEDESTAL.getRegistryName().toString()
        );

        // Pedestal Core
        GameRegistry.registerTileEntity(
                PEDESTAL_CORE.getTileEntityClass(),
                PEDESTAL_CORE.getRegistryName().toString()
        );
    }

    public static void registerItems(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                PEDESTAL.createItemBlock(),
                PEDESTAL_CORE.createItemBlock()
        );
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels()
    {
        PEDESTAL.registerItemModel();
        PEDESTAL_CORE.registerItemModel();
    }
}
