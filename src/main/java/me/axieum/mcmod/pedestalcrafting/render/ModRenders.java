package me.axieum.mcmod.pedestalcrafting.render;

import me.axieum.mcmod.pedestalcrafting.block.ModBlocks;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenders
{
    public static void init()
    {
        // Pedestal
        ClientRegistry.bindTileEntitySpecialRenderer(
                ModBlocks.PEDESTAL.getTileEntityClass(),
                new RenderPedestal()
        );


        // Pedestal Core
        ClientRegistry.bindTileEntitySpecialRenderer(
                ModBlocks.PEDESTAL_CORE.getTileEntityClass(),
                new RenderPedestalCore()
        );
    }
}
