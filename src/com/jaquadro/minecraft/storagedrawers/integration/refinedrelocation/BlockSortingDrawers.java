package com.jaquadro.minecraft.storagedrawers.integration.refinedrelocation;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import com.jaquadro.minecraft.storagedrawers.integration.RefinedRelocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWood;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSortingDrawers extends BlockDrawers
{
    @SideOnly(Side.CLIENT)
    IIcon[] iconSort;

    public BlockSortingDrawers (String blockName, int drawerCount, boolean halfDepth) {
        super(blockName, drawerCount, halfDepth);

        setCreativeTab(RefinedRelocation.tabStorageDrawers);
    }

    public static boolean upgradeToSorting (World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (!(tile instanceof TileEntityDrawersStandard))
            return false;

        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        TileEntityDrawersStandard oldDrawer = (TileEntityDrawersStandard)tile;
        TileSortingDrawersStandard newDrawer = new TileSortingDrawersStandard();

        NBTTagCompound tag = new NBTTagCompound();
        oldDrawer.writeToNBT(tag);
        newDrawer.readFromNBT(tag);

        world.removeTileEntity(x, y, z);
        world.setBlockToAir(x, y, z);

        if (block == ModBlocks.fullDrawers1)
            world.setBlock(x, y, z, RefinedRelocation.fullDrawers1, meta, 3);
        else if (block == ModBlocks.fullDrawers2)
            world.setBlock(x, y, z, RefinedRelocation.fullDrawers2, meta, 3);
        else if (block == ModBlocks.fullDrawers4)
            world.setBlock(x, y, z, RefinedRelocation.fullDrawers4, meta, 3);
        else if (block == ModBlocks.halfDrawers2)
            world.setBlock(x, y, z, RefinedRelocation.halfDrawers2, meta, 3);
        else if (block == ModBlocks.halfDrawers4)
            world.setBlock(x, y, z, RefinedRelocation.halfDrawers4, meta, 3);

        world.setTileEntity(x, y, z, newDrawer);

        return true;
    }

    @Override
    public TileSortingDrawersStandard createNewTileEntity (World world, int meta) {
        return new TileSortingDrawersStandard();
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        if (side == 1 && !halfDepth)
            return iconSort[meta];

        return super.getIcon(side, meta);
    }

    @SideOnly(Side.CLIENT)
    protected IIcon getIcon (IBlockAccess blockAccess, int x, int y, int z, int side, int level) {
        int meta = blockAccess.getBlockMetadata(x, y, z) % BlockWood.field_150096_a.length;
        if (side == 1 && !halfDepth)
            return iconSort[meta];

        return super.getIcon(blockAccess, x, y, z, side, level);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        super.registerBlockIcons(register);

        String[] subtex = BlockWood.field_150096_a;

        iconSort = new IIcon[subtex.length];

        for (int i = 0; i < subtex.length; i++) {
            iconSort[i] = register.registerIcon(StorageDrawers.MOD_ID + ":drawers_" + subtex[i] + "_sort");
        }
    }
}
