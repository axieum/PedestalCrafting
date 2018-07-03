package me.axieum.mcmod.pedestalcrafting.tile;

import me.axieum.mcmod.pedestalcrafting.block.BlockPedestal;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipe;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipeManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TilePedestalCore extends TileEntity implements ITickable
{
    public int elapsed = 0;
    public int pedestalCount = 0;
    public ItemStackHandler inventory = new ItemStackHandler(1)
    {
        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            TilePedestalCore.this.markDirty();

            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    };

    @Override
    public void update()
    {
        if (this.getWorld().isRemote)
            return;

        ArrayList<BlockPos> pedestalLocs = this.findPedestals();
        PedestalRecipe recipe = this.getRecipe(pedestalLocs);
        if (recipe != null)
        {
            ArrayList<TilePedestal> pedestals = this.getActivePedestals(recipe, pedestalLocs);
            if (this.process(recipe))
            {
                for (TilePedestal pedestal : pedestals)
                {
                    pedestal.inventory.setStackInSlot(0, ItemStack.EMPTY);
                    pedestal.markDirty();
                    if (recipe.getParticles().size() >= 3)
                    {
                        int particleIndex = new Random().nextInt(recipe.getParticles().get(2).size());
                        EnumParticleTypes particle = (EnumParticleTypes) recipe.getParticles().get(2).keySet().toArray()[particleIndex];
                        ((WorldServer) this.getWorld()).spawnParticle(
                                particle,
                                false,
                                pedestal.getPos().getX() + 0.5D,
                                pedestal.getPos().getY() + 1.4D,
                                pedestal.getPos().getZ() + 0.5D,
                                recipe.getParticles().get(2).get(particle),
                                0,
                                0,
                                0, 0.1D
                        );
                    }
                }

                this.inventory.setStackInSlot(0, recipe.getOutput().copy());
                this.elapsed = 0;
                this.markDirty();

                if (recipe.getParticles().size() >= 2)
                {
                    int particleIndex = new Random().nextInt(recipe.getParticles().get(1).size());
                    EnumParticleTypes particle = (EnumParticleTypes) recipe.getParticles().get(1).keySet().toArray()[particleIndex];
                    ((WorldServer) this.getWorld()).spawnParticle(
                            particle,
                            false,
                            this.getPos().getX() + 0.5D,
                            this.getPos().getY() + 1.5D,
                            this.getPos().getZ() + 0.5D,
                            recipe.getParticles().get(1).get(particle),
                            0,
                            0,
                            0, 0.1D
                    );
                }
            } else
            {
                if (recipe.getParticles().size() >= 1)
                {
                    int particleIndex = new Random().nextInt(recipe.getParticles().get(0).size());
                    EnumParticleTypes particle = (EnumParticleTypes) recipe.getParticles().get(0).keySet().toArray()[particleIndex];
                    ((WorldServer) this.getWorld()).spawnParticle(
                            particle,
                            false,
                            this.getPos().getX() + 0.5D,
                            this.getPos().getY() + 1.5D,
                            this.getPos().getZ() + 0.5D,
                            recipe.getParticles().get(0).get(particle),
                            0,
                            0,
                            0, 0.1D
                    );
                }
            }
        } else
        {
            this.elapsed = 0;
        }
    }

    private boolean process(PedestalRecipe recipe)
    {
        this.elapsed += 1;

        if (this.elapsed % 5 == 0)
        {
            this.markDirty();

            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }

        return this.elapsed >= recipe.getTicks();
    }

    public PedestalRecipe getRecipe()
    {
        return getRecipe(findPedestals());
    }

    private PedestalRecipe getRecipe(ArrayList<BlockPos> locs)
    {
        ArrayList<PedestalRecipe> recipes = PedestalRecipeManager.getInstance().getValidRecipes(this.inventory.getStackInSlot(
                0));

        if (!recipes.isEmpty())
            for (PedestalRecipe recipe : recipes)
                if (this.getActivePedestals(recipe, locs) != null)
                    return recipe;

        return null;
    }

    private ArrayList<BlockPos> findPedestals()
    {
        ArrayList<BlockPos> pedestalLocs = new ArrayList<BlockPos>();
        Iterable<BlockPos> blocksToCheck = this.getPos().getAllInBox(
                this.getPos().add(-3, -1, -3),
                this.getPos().add(3, 1, 3)
        );

        for (BlockPos blockPos : blocksToCheck)
            if (this.getWorld().getBlockState(blockPos).getBlock() instanceof BlockPedestal)
                pedestalLocs.add(blockPos);

        this.pedestalCount = pedestalLocs.size();
        return pedestalLocs;
    }

    private ArrayList<TilePedestal> getActivePedestals(PedestalRecipe recipe, ArrayList<BlockPos> locs)
    {
        if (locs.isEmpty())
            return null;

        ArrayList<Object> remainingItems = new ArrayList<Object>(recipe.getInput());
        ArrayList<TilePedestal> pedestals = new ArrayList<TilePedestal>();

        for (BlockPos pos : locs)
        {
            TileEntity tileEntity = this.getWorld().getTileEntity(pos);

            if (!(tileEntity instanceof TilePedestal))
                break;

            TilePedestal pedestal = (TilePedestal) tileEntity;
            Iterator<Object> remainingIterator = remainingItems.iterator();
            while (remainingIterator.hasNext())
            {
                boolean isMatch = false;
                Object remainingItem = remainingIterator.next();
                ItemStack pedestalItem = pedestal.inventory.getStackInSlot(0);

                if (remainingItem instanceof ItemStack)
                {
                    boolean itemMatches = OreDictionary.itemMatches((ItemStack) remainingItem, pedestalItem, false);
                    boolean tagMatches = !((ItemStack) remainingItem).hasTagCompound() || ItemStack.areItemStackTagsEqual(
                            (ItemStack) remainingItem,
                            pedestalItem
                    );
                    isMatch = itemMatches && tagMatches;
                } else if (remainingItem instanceof List)
                {
                    for (ItemStack itemStack : (List<ItemStack>) remainingItem)
                    {
                        isMatch = OreDictionary.itemMatches(itemStack, pedestalItem, false);
                        if (isMatch)
                            break;
                    }
                }

                if (isMatch)
                {
                    pedestals.add(pedestal);
                    remainingItems.remove(remainingItem);
                    break;
                }
            }
        }

        if (!remainingItems.isEmpty() || pedestals.size() != recipe.getInput().size())
            return null;

        return pedestals;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 0, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());

        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setInteger("elapsed", elapsed);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        elapsed = compound.getInteger("elapsed");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory : super.getCapability(
                capability,
                facing
        );
    }
}
