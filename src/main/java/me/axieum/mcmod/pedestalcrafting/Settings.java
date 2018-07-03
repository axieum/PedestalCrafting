package me.axieum.mcmod.pedestalcrafting;

import net.minecraftforge.common.config.Config;

@Config(modid = PedestalCrafting.MOD_ID)
public class Settings
{
    @Config.LangKey("config.displaySpeed")
    @Config.RangeDouble
    public static double displaySpeed = 0.125D;
}
