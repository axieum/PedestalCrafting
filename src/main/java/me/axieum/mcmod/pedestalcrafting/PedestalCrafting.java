package me.axieum.mcmod.pedestalcrafting;

import me.axieum.mcmod.pedestalcrafting.block.BlockPedestalCore;
import me.axieum.mcmod.pedestalcrafting.block.ModBlocks;
import me.axieum.mcmod.pedestalcrafting.proxy.CommonProxy;
import me.axieum.mcmod.pedestalcrafting.recipe.ModRecipes;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
        modid = PedestalCrafting.MOD_ID,
        name = PedestalCrafting.MOD_NAME,
        version = PedestalCrafting.MOD_VERSION,
        dependencies = PedestalCrafting.MOD_DEPENDENCIES,
        useMetadata = true
)
public class PedestalCrafting
{
    public static final String MOD_ID = "pedestalcrafting";
    public static final String MOD_NAME = "Pedestal Crafting";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_DEPENDENCIES = "required-after: crafttweaker; after: jei";

    @Mod.Instance(PedestalCrafting.MOD_ID)
    public static PedestalCrafting instance;

    @SidedProxy(
            clientSide = "me.axieum.mcmod.pedestalcrafting.proxy.ClientProxy",
            serverSide = "me.axieum.mcmod.pedestalcrafting.proxy.ServerProxy"
    )
    public static CommonProxy proxy;

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID)
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ModBlocks.PEDESTAL_CORE, 1, BlockPedestalCore.Variant.QUARTZBLOCK.ordinal());
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event)
        {
            ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
            ModBlocks.registerItems(event.getRegistry());
        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event)
        {
            ModBlocks.registerModels();
        }

        @SubscribeEvent
        public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
        {
            ModRecipes.init();
        }
    }
}
