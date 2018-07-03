package me.axieum.mcmod.pedestalcrafting.compat.justenoughitems;

import me.axieum.mcmod.pedestalcrafting.PedestalCrafting;
import me.axieum.mcmod.pedestalcrafting.block.BlockPedestalCore;
import me.axieum.mcmod.pedestalcrafting.block.ModBlocks;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class PedestalJEICategory implements IRecipeCategory<PedestalJEIWrapper>
{
    public static final String UID = "pedestalcrafting:pedestal";

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            PedestalCrafting.MOD_ID,
            "textures/jei/pedestal_category.png"
    );
    private final IDrawable BACKGROUND;
    private final IDrawable ICON;

    public PedestalJEICategory(IJeiHelpers helpers)
    {
        this.BACKGROUND = helpers.getGuiHelper().createDrawable(TEXTURE, 0, 0, 152, 171);
        this.ICON = helpers.getGuiHelper().createDrawableIngredient(
                new ItemStack(
                        ModBlocks.PEDESTAL_CORE,
                        1,
                        BlockPedestalCore.Variant.QUARTZBLOCK.ordinal()
                )
        );
    }

    @Override
    public String getUid()
    {
        return UID;
    }

    @Override
    public String getTitle()
    {
        return I18n.format("gui.jei.category.pedestal");
    }

    @Override
    public String getModName()
    {
        return PedestalCrafting.MOD_NAME;
    }

    @Override
    public IDrawable getBackground()
    {
        return BACKGROUND;
    }

    @Nullable
    @Override
    public IDrawable getIcon()
    {
        return ICON;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, PedestalJEIWrapper wrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemStacks = layout.getItemStacks();

        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class).get(0);

        // Output
        itemStacks.init(0, false, 67, 137);
        itemStacks.set(0, outputs);

        // Core
        itemStacks.init(1, true, 67, 46);
        itemStacks.set(1, inputs.get(0));

        // Inputs
        double angle = 360 / (inputs.size() - 1);
        Point point = new Point(67, 9);
        Point center = new Point(67, 46);

        for (int i = 2; i < inputs.size() + 1; i++)
        {
            itemStacks.init(i, true, point.x, point.y);
            itemStacks.set(i, inputs.get(i - 1));
            point = rotatePointAbout(point, center, angle);
        }
    }

    /**
     * @see https://github.com/AgriCraft/AgriCraft/commit/4cfc7527adf41cb5f0d3a1b0de67c8b202bc1eaa#diff-2716014db01489421463eb15b0f11c28R102
     */
    private Point rotatePointAbout(Point in, Point about, double degrees)
    {
        double rad = degrees * Math.PI / 180.0;
        double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
        double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
        return new Point(((int) newX), ((int) newY));
    }
}
