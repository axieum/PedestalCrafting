package me.axieum.mcmod.pedestalcrafting.util;

import me.axieum.mcmod.pedestalcrafting.PedestalCrafting;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block
{
    protected String name;

    public BlockBase(String name, Material material)
    {
        super(material);

        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public Item createItemBlock()
    {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab)
    {
        super.setCreativeTab(tab);
        return this;
    }

    @SideOnly(Side.CLIENT)
    public void registerItemModel()
    {
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this),
                0,
                new ModelResourceLocation(
                        PedestalCrafting.MOD_ID + ":" + getRegistryName(),
                        "inventory"
                )
        );
    }
}
