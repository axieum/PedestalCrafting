package me.axieum.mcmod.pedestalcrafting.proxy;

import crafttweaker.CraftTweakerAPI;
import me.axieum.mcmod.pedestalcrafting.PedestalCrafting;
import me.axieum.mcmod.pedestalcrafting.compat.crafttweaker.Pedestal;
import me.axieum.mcmod.pedestalcrafting.compat.theoneprobe.TOPCompat;
import me.axieum.mcmod.pedestalcrafting.compat.waila.WAILACompat;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        if (Loader.isModLoaded("crafttweaker"))
            CraftTweakerAPI.registerClass(Pedestal.class);

        if (Loader.isModLoaded("theoneprobe"))
            TOPCompat.register();

        if (Loader.isModLoaded("waila"))
            WAILACompat.register();
    }

    public void init(FMLInitializationEvent event)
    {
        ConfigManager.sync(PedestalCrafting.MOD_ID, Config.Type.INSTANCE);
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        //
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(PedestalCrafting.MOD_ID))
            ConfigManager.sync(PedestalCrafting.MOD_ID, Config.Type.INSTANCE);
    }
}
