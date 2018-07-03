package me.axieum.mcmod.pedestalcrafting.recipe;

import jline.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedestalRecipe
{
    private ItemStack core, output;
    private ArrayList<Object> input = new ArrayList<Object>();
    private int ticks;
    private ArrayList<HashMap<EnumParticleTypes, Integer>> particles = new ArrayList<HashMap<EnumParticleTypes, Integer>>(
            3);

    public PedestalRecipe(ItemStack output, int ticks, ItemStack core, Object... inputs)
    {
        this.core = core;
        this.ticks = ticks > 0 ? ticks : 3;
        this.output = output;

        for (Object input : inputs)
        {
            if (input instanceof ItemStack)
                this.input.add(((ItemStack) input).copy());
            else if (input instanceof Item)
                this.input.add(new ItemStack((Item) input));
            else if (input instanceof Block)
                this.input.add(new ItemStack((Block) input));
            else if (input instanceof String)
                this.input.add(OreDictionary.getOres((String) input));
            else if (input instanceof List)
                this.input.add(input);
            else
                throw new RuntimeException("Invalid inputs for Pedestal Crafting recipe with core item: " + core.getDisplayName());
        }

        // Initialise default particle effects, in case not overriden
        this.initParticles();
        this.addParticleCrafting(EnumParticleTypes.END_ROD, 2);
        this.addParticlePostCraftCore(EnumParticleTypes.CLOUD, 25);
        this.addParticlePostCraftPedestal(EnumParticleTypes.SMOKE_NORMAL, 15);
    }

    private void initParticles()
    {
        this.particles.add(0, new HashMap<EnumParticleTypes, Integer>());
        this.particles.add(1, new HashMap<EnumParticleTypes, Integer>());
        this.particles.add(2, new HashMap<EnumParticleTypes, Integer>());
    }

    public PedestalRecipe setParticles(@Nullable HashMap<EnumParticleTypes, Integer> particlesCrafting, @Nullable HashMap<EnumParticleTypes, Integer> particlesPostCraftCore, @Nullable HashMap<EnumParticleTypes, Integer> particlesPostCraftPedestal)
    {
        this.particles.set(0, particlesCrafting);
        this.particles.set(1, particlesPostCraftCore);
        this.particles.set(2, particlesPostCraftPedestal);

        return this;
    }

    public PedestalRecipe addParticleCrafting(HashMap<EnumParticleTypes, Integer>... particles)
    {
        for (HashMap<EnumParticleTypes, Integer> particle : particles)
            this.particles.get(0).putAll(particle);

        return this;
    }

    public PedestalRecipe addParticlePostCraftCore(HashMap<EnumParticleTypes, Integer>... particles)
    {
        for (HashMap<EnumParticleTypes, Integer> particle : particles)
            this.particles.get(1).putAll(particle);

        return this;
    }

    public PedestalRecipe addParticlePostCraftPedestal(HashMap<EnumParticleTypes, Integer>... particles)
    {
        for (HashMap<EnumParticleTypes, Integer> particle : particles)
            this.particles.get(2).putAll(particle);

        return this;
    }

    public PedestalRecipe addParticleCrafting(EnumParticleTypes particle, int count)
    {
        this.particles.get(0).put(particle, count);
        return this;
    }

    public PedestalRecipe addParticlePostCraftCore(EnumParticleTypes particle, int count)
    {
        this.particles.get(1).put(particle, count);
        return this;
    }

    public PedestalRecipe addParticlePostCraftPedestal(EnumParticleTypes particle, int count)
    {
        this.particles.get(2).put(particle, count);
        return this;
    }

    public ItemStack getCore()
    {
        return core;
    }

    public ArrayList<Object> getInput()
    {
        return input;
    }

    public int getTicks()
    {
        return ticks;
    }

    public ItemStack getOutput()
    {
        return output;
    }

    public ArrayList<HashMap<EnumParticleTypes, Integer>> getParticles()
    {
        return particles;
    }
}
