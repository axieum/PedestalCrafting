package me.axieum.mcmod.pedestalcrafting.compat.justenoughitems;

import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PedestalJEIWrapper implements IRecipeWrapper
{
    private IJeiHelpers helpers;
    private final PedestalRecipe recipe;

    public PedestalJEIWrapper(IJeiHelpers helpers, PedestalRecipe recipe)
    {
        this.helpers = helpers;
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        IStackHelper helper = this.helpers.getStackHelper();
        ItemStack output = this.recipe.getOutput();

        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(helper.toItemStackList(this.recipe.getCore()));
        inputs.addAll(helper.expandRecipeItemStackInputs(this.recipe.getInput()));

        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        if (mouseX > 67 && mouseX < 83 && mouseY > 104 && mouseY < 127)
        {
            int seconds = this.recipe.getTicks() / 20;
            final int hours = (int) TimeUnit.SECONDS.toHours(seconds);
            seconds -= hours * 60 * 60;
            final int minutes = (int) TimeUnit.SECONDS.toMinutes(seconds);
            seconds -= minutes * 60;
            String formatString = "";

            if (hours > 0)
                formatString += "h'h' ";
            if (minutes > 0)
                formatString += "m'm' ";
            if (seconds > 0)
                formatString += "s's'";

            SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);

            return Arrays.asList(dateFormat.format(new Date((long) (this.recipe.getTicks() / 20 * 1000))));
        }

        return Collections.emptyList();
    }
}
