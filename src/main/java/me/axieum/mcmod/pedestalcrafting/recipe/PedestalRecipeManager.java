package me.axieum.mcmod.pedestalcrafting.recipe;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class PedestalRecipeManager
{
    private static final PedestalRecipeManager INSTANCE = new PedestalRecipeManager();

    private ArrayList<PedestalRecipe> recipes = new ArrayList<PedestalRecipe>();

    public static PedestalRecipeManager getInstance()
    {
        return INSTANCE;
    }

    public PedestalRecipe addRecipe(ItemStack output, int ticks, ItemStack core, Object... inputs)
    {
        PedestalRecipe recipe = new PedestalRecipe(output, ticks, core, inputs);
        recipes.add(recipe);
        return recipe;
    }

    public PedestalRecipe addRecipe(PedestalRecipe recipe)
    {
        recipes.add(recipe);
        return recipe;
    }

    public ArrayList<PedestalRecipe> getRecipes()
    {
        return recipes;
    }

    public PedestalRecipe getRecipe(ItemStack itemStack)
    {
        for (PedestalRecipe recipe : recipes)
        {
            if (recipe.getOutput().isItemEqual(itemStack))
            {
                return recipe;
            }
        }
        return null;
    }

    public ArrayList<PedestalRecipe> getValidRecipes(ItemStack itemStack)
    {
        ArrayList<PedestalRecipe> validRecipes = new ArrayList<PedestalRecipe>();

        if (!itemStack.isEmpty())
        {
            for (PedestalRecipe recipe : this.getRecipes())
            {
                if (!recipe.getCore().isEmpty() && recipe.getCore().isItemEqual(itemStack))
                    validRecipes.add(recipe);
            }
        }
        return validRecipes;
    }

    public boolean removeRecipe(ItemStack itemStack)
    {
        for (PedestalRecipe recipe : recipes)
        {
            if (recipe.getOutput().isItemEqual(itemStack))
            {
                recipes.remove(itemStack);
                return true;
            }
        }
        return false;
    }
}
