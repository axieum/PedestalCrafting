package me.axieum.mcmod.pedestalcrafting.util;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public interface IEnumVariant extends IStringSerializable
{
    @Override
    default String getName()
    {
        return ((Enum) this).name().toLowerCase(Locale.ROOT);
    }
}
