package me.axieum.mcmod.pedestalcrafting.compat.justenoughitems;

import me.axieum.mcmod.pedestalcrafting.block.ModBlocks;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipe;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipeManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class PedestalCraftingJEIPlugin implements IModPlugin
{
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        registry.addRecipeCategories(new PedestalJEICategory(registry.getJeiHelpers()));
    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipeCatalyst(
                new ItemStack(ModBlocks.PEDESTAL_CORE, 1, OreDictionary.WILDCARD_VALUE),
                PedestalJEICategory.UID
        );

        registry.handleRecipes(
                PedestalRecipe.class,
                recipe -> new PedestalJEIWrapper(registry.getJeiHelpers(), recipe),
                PedestalJEICategory.UID
        );

        registry.addRecipes(PedestalRecipeManager.getInstance().getRecipes(), PedestalJEICategory.UID);
    }
}
