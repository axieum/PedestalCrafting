package me.axieum.mcmod.pedestalcrafting.block;

import me.axieum.mcmod.pedestalcrafting.PedestalCrafting;
import me.axieum.mcmod.pedestalcrafting.tile.TilePedestalCore;
import me.axieum.mcmod.pedestalcrafting.util.BlockTileBase;
import me.axieum.mcmod.pedestalcrafting.util.IEnumVariant;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class BlockPedestalCore extends BlockTileBase<TilePedestalCore>
{
    public BlockPedestalCore(String name)
    {
        super(name, Material.ROCK);

        setHardness(2f);
        setResistance(4f);

        setCreativeTab(PedestalCrafting.CREATIVE_TAB);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || player.isSneaking())
            return true;

        TilePedestalCore tile = getTileEntity(world, pos);
        ItemStack heldItem = player.getHeldItem(hand);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);

        if (!itemHandler.getStackInSlot(0).isEmpty())
        {
            EntityItem item = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D,
                                             itemHandler.extractItem(0, 64, false)
            );
            item.setNoPickupDelay();
            world.spawnEntity(item);
        } else if (!heldItem.isEmpty())
        {
            ItemStack stackInsert = heldItem.copy();
            stackInsert.setCount(1);
            itemHandler.insertItem(0, stackInsert, false);

            ItemStack stackPlayer = heldItem.copy();
            stackPlayer.setCount(stackPlayer.getCount() - 1);
            player.setHeldItem(hand, stackPlayer);

            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        tile.markDirty();

        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
    {
        return getTileEntity(world, pos).inventory.getStackInSlot(0).isEmpty() ? 0 : 1;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TilePedestalCore tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        ItemStack itemStack = itemHandler.getStackInSlot(0);

        if (!itemStack.isEmpty())
            world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemStack));

        super.breakBlock(world, pos, state);
    }

    /**
     * Block Variants Implementation
     */

    public enum Variant implements IEnumVariant
    {
        DIAMOND(Material.IRON, SoundType.METAL),
        EMERALD(Material.IRON, SoundType.METAL),
        GLOWSTONE(Material.GLASS, SoundType.GLASS),
        GOLD(Material.IRON, SoundType.METAL),
        GRASS(Material.GRASS, SoundType.GROUND),
        IRON(Material.IRON, SoundType.METAL),
        LAPIS(Material.IRON, SoundType.METAL),
        LOGACACIA(Material.WOOD, SoundType.WOOD),
        LOGBIGOAK(Material.WOOD, SoundType.WOOD),
        LOGBIRCH(Material.WOOD, SoundType.WOOD),
        LOGJUNGLE(Material.WOOD, SoundType.WOOD),
        LOGOAK(Material.WOOD, SoundType.WOOD),
        LOGSPRUCE(Material.WOOD, SoundType.WOOD),
        OBSIDIAN(Material.ROCK, SoundType.STONE),
        QUARTZBLOCK(Material.IRON, SoundType.STONE),
        SANDSTONE(Material.ROCK, SoundType.STONE);

        private Material material;
        private SoundType sound;

        Variant(Material material, SoundType sound)
        {
            this.material = material;
            this.sound = sound;
        }

        public Material getMaterial()
        {
            return this.material;
        }

        public SoundType getSound()
        {
            return this.sound;
        }
    }

    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("type", Variant.class);

    @Override
    public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        if (meta >= Variant.values().length)
            meta = 0;

        Variant variant = Variant.values()[meta];
        return getDefaultState().withProperty(VARIANT, variant);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (Variant variant : Variant.values())
        {
            items.add(new ItemStack(this, 1, variant.ordinal()));
        }
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity)
    {
        return state.getValue(VARIANT).getSound();
    }

    @Override
    public Material getMaterial(IBlockState state)
    {
        return state.getValue(VARIANT).getMaterial();
    }

    @Override
    public int getLightValue(IBlockState state)
    {
        return state.getValue(VARIANT).getName().equals("glowstone") ? 15 : 0;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        final String type = blockState.getValue(VARIANT).getName();

        if (type.equals("obsidian"))
            return 45F;

        if (type.equals("grass"))
            return 0.75F;

        return super.getBlockHardness(blockState, worldIn, pos);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
    {
        return world.getBlockState(pos).getValue(VARIANT).getName().equals("obsidian") ? 1200F : super.getExplosionResistance(
                world,
                pos,
                exploder,
                explosion
        );
    }

    /**
     * Handle correct colouring of Grass Pedestal
     */

    @SubscribeEvent
    public static void onColorHandle(ColorHandlerEvent.Block event)
    {
        event.getBlockColors().registerBlockColorHandler(
                (state, worldIn, pos, tintIndex) ->
                {
                    if (state != ModBlocks.PEDESTAL_CORE.getDefaultState().withProperty(
                            VARIANT,
                            Variant.GRASS
                    ))
                        return -1;
                    return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(
                            worldIn,
                            pos
                    ) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
                }, ModBlocks.PEDESTAL_CORE);
    }

    /**
     * Item Block (w/ variants) Implementation
     */

    @Override
    public Item createItemBlock()
    {
        return new ItemMultiTexture(this, this, stack -> {
            int metadata = stack.getMetadata();
            Variant[] values = Variant.values();
            return metadata < values.length ? values[metadata].getName() : values[0].getName();
        }).setRegistryName(getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerItemModel()
    {
        for (Variant variant : Variant.values())
            ModelLoader.setCustomModelResourceLocation(
                    Item.getItemFromBlock(this),
                    variant.ordinal(),
                    new ModelResourceLocation(
                            getRegistryName(),
                            "type=" + variant.getName()
                    )
            );
    }

    /**
     * Tile Entity Implementation
     */

    @Override
    public Class<TilePedestalCore> getTileEntityClass()
    {
        return TilePedestalCore.class;
    }

    @Nullable
    @Override
    public TilePedestalCore createTileEntity(World world, IBlockState state)
    {
        return new TilePedestalCore();
    }
}
