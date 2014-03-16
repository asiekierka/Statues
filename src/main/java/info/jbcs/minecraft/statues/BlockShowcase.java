/**
 * Block class for the statue
 */

package info.jbcs.minecraft.statues;


import java.util.ArrayList;

import pl.asie.lib.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockShowcase extends BlockContainer {
	public BlockShowcase(Material material) {
		super(material);
	}

    @Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int meta=world.getBlockMetadata(x, y, z);
        if((meta&4)!=0) return ret;
       
        ret.add(new ItemStack(Statues.itemShowcase,1));

		return ret;
    }

	
 /*   @Override
	@SideOnly(Side.CLIENT)
    public Item itemPicked(World par1World, int par2, int par3, int par4){
        return Statues.itemShowcase.itemID;
    }*/ // TODO

    @Override
	public int getMobilityFlag(){
        return 2;
    }

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return -1;
	}

	/**
	 * return false if the block isn't a full 1*1 cube
	 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * return false if the block mustn't be rendered as a normal block
	 */
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
    	int meta=world.getBlockMetadata(x, y, z);
    	
    	
    	switch(meta){
    	case 0|4: setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.5f, 1.0F); break;
    	case 2|4: setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.5f, 1.0F); break;
    	case 1|4: setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5f, 0.5F); break;
    	case 3|4: setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.5f, 1.0F); break;
	    default: setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5f, 1.0F); break;
    	}
    }
    
    @Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack par6ItemStack) {
		int meta = MathHelper.floor_double((entityliving.rotationYaw * 4F) / 360F + 0.5D) & 3;
		int dx,dz;
		
		switch(meta){
		default:
		case 0: case 2: dx=1; dz=0; break;
		case 1: case 3: dx=0; dz=1; break;
		}
		
		world.setBlockMetadataWithNotify(x, y, z, meta, 3);
		if(meta>=2) meta-=2;
		world.setBlock(x+dx, y, z+dz, this, meta|4, 3);
		world.setBlock(x-dx, y, z-dz, this, (meta+2)|4, 3);
    }

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		int meta=world.getBlockMetadata(x, y, z);
		
		if((meta&4)!=0){
			switch(meta&3){
			default:
			case 0: case 2:
				if(isCenterBlock(world,x-1,y,z)) x--;
				else if(isCenterBlock(world,x+1,y,z)) x++;
				break;
			case 1: case 3:
				if(isCenterBlock(world,x,y,z-1)) z--;
				else if(isCenterBlock(world,x,y,z+1)) z++;
				break;
			}
		}

		if(! isCenterBlock(world,x,y,z))
			return true;
		
		TileEntityShowcase teshowcase = (TileEntityShowcase) world.getTileEntity(x, y, z);
		if (teshowcase instanceof TileEntityShowcase && !world.isRemote)
			Statues.guiShowcase.open(entityplayer, world, x, y, z);
		
		return true;
	}

	boolean isCenterBlock(World world, int x, int y, int z){
		return (world.getBlock(x, y, z).equals(this)) && (world.getBlockMetadata(x, y, z)&4)==0;
	}
	
	boolean isCenterBlock(Block block,int meta){
		return (block.equals(this)) && (meta&4)==0;
	}
	
	
	@Override
	public void breakBlock(World world, int xx, int yy, int zz, Block block, int meta) {
		int x=xx,y=yy,z=zz;
		boolean found=true;
				
		if(isCenterBlock(block,meta)){
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile instanceof TileEntityShowcase) ItemUtils.dropItems(world, xx, yy, zz, ((TileEntityShowcase) tile));
		} else{
			switch(meta&3){
			default:
			case 0: case 2:
				if(isCenterBlock(world,x-1,y,z))
					x--;
				else if(isCenterBlock(world,x+1,y,z))
					x++;
				else
					found=false;
				break;
			case 1: case 3:
				if(isCenterBlock(world,x,y,z-1))
					z--;
				else if(isCenterBlock(world,x,y,z+1))
					z++;
				else
					found=false;
				break;
			}
		}
		
		if(!found && ! isCenterBlock(block,meta))
			return;
		
		world.setBlock(x,y,z,Blocks.air);
		
		switch(meta&3){
		case 0: case 2:
			world.setBlock(x+1,y,z,Blocks.air);
			world.setBlock(x-1,y,z,Blocks.air);
			break;
		case 1: case 3:
			world.setBlock(x,y,z+1,Blocks.air);
			world.setBlock(x,y,z-1,Blocks.air);
			break;
		}
		
		super.breakBlock(world, xx, yy, zz, block, meta);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int metadata) {
		return new TileEntityShowcase();
	}
	
    @Override
	public IIcon getIcon(int side, int meta){
        return Blocks.planks.getIcon(2,0);
    }

}
