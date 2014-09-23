/**
 * Block class for the statue
 */

package info.jbcs.minecraft.statues;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pl.asie.lib.util.ItemUtils;

import java.util.Random;

public class BlockStatue extends BlockContainer {
	public BlockStatue(Material material) {
		super(material);
		
		setLightOpacity(0);
	}

    @Override
	public int quantityDropped(Random par1Random){
        return 0;
    }

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
	
    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
    	int meta=world.getBlockMetadata(x, y, z);
    	
    	if((meta&4)==0){
    		setBlockBounds(0.1F, 0F, 0.1F, 0.9F, 2F, 0.9F);
    	} else{
    		setBlockBounds(0.1F, -1F, 0.1F, 0.9F, 1F, 0.9F);
    	}
    }
    
    public static boolean isMainBlock(IBlockAccess world, int x, int y, int z){
    	return (world.getBlockMetadata(x, y, z)&4)==0;
    }
    
    public static boolean isStatue(IBlockAccess world, int x, int y, int z){
    	return world.getBlock(x, y, z) instanceof BlockStatue;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		if(world.isRemote) return true;
   	
    	if(! isMainBlock(world, x, y, z))
    		y--;
		
		TileEntityStatue statue = (TileEntityStatue) world.getTileEntity(x, y, z);
		if (statue instanceof TileEntityStatue)
			Statues.guiStatue.open(entityplayer, world, x, y, z);
		
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if((meta&4)==0){
			world.setBlock(x,y+1,z,Blocks.air);
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile instanceof TileEntityStatue) {
				ItemUtils.dropItems(world, x, y, z, (TileEntityStatue)tile);
			}
		} else{
			world.setBlock(x,y-1,z,Blocks.air);
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int metadata) {
		return new TileEntityStatue();
	}
	
	
    @Override
	public IIcon getIcon(int side, int meta){
        return Blocks.stone.getIcon(0,0);
    }

    @Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side){
		TileEntity te = world.getTileEntity(x, y, z);
		if (! (te instanceof TileEntityStatue))
			return Blocks.stone.getIcon(0,0);
		TileEntityStatue statue=(TileEntityStatue) te;
		
		Block block=statue.block;
		if(block==null) return Blocks.stone.getIcon(0,0);
		
		return block.getIcon(side, statue.meta);
    }

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
    	while(isStatue(world, x, y, z) && ! isMainBlock(world, x, y, z)) y--;
    	if(! isStatue(world, x, y, z)) return 0;
    	
		TileEntity te = world.getTileEntity(x, y, z);
		if (! (te instanceof TileEntityStatue))
			return 0;
		TileEntityStatue statue=(TileEntityStatue) te;
   	
		return statue.block.getLightValue();
	}

    @Override
	public boolean canProvidePower(){
        return true;
    }

    @Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side){
    	int ox=x, oy=y, oz=z;
    	while(isStatue(world, x, y, z) && ! isMainBlock(world, x, y, z)) y--;
    	if(! isStatue(world, x, y, z)) return 0;
    	
		TileEntity te = world.getTileEntity(x, y, z);
		if (! (te instanceof TileEntityStatue))
			return 0;
		TileEntityStatue statue=(TileEntityStatue) te;

		if(statue.block==null) return 0;
		return statue.block.isProvidingWeakPower(world, ox, oy, oz, side);
    }
}
