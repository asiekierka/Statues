/**
 * Item class for the statue
 */

package info.jbcs.minecraft.statues;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemShowcase extends ItemReed {
	public ItemShowcase(Block par2Block) {
		super(par2Block);
		setHasSubtypes(true);
		setMaxDamage(0);
		maxStackSize = 64;
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int par1) {
		return par1;
	}

	/**
	 * When this method is called, your block should register all the icons it
	 * needs with the given IconRegister. This is the only chance you get to
	 * register icons.
	 */
	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);

		Statues.slotHand = register.registerIcon("statues:slothand");
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
		if (world.isRemote) return false;
		
		int meta = MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
		int dx,dz;
		
		switch(meta){
		default:
		case 0: case 2: dx=1; dz=0; break;
		case 1: case 3: dx=0; dz=1; break;
		}
		
		switch (side) {
		case 0: y--; break;
		case 1: y++; break;
		case 2: z--; break;
		case 3: z++; break;
		case 4: x--; break;
		case 5: x++; break;
		}

		
		if(! player.canPlayerEdit(x, y, z, side, stack)) return false;
		if(! player.canPlayerEdit(x+dx, y, z+dz, side, stack)) return false;
		if(! player.canPlayerEdit(x-dx, y, z-dz, side, stack)) return false;
		
		if(! world.isAirBlock(x, y, z)) return false;
		if(! world.isAirBlock(x+dx, y, z+dz)) return false;
		if(! world.isAirBlock(x-dx, y, z-dz)) return false;
		
		world.setBlock(x, y, z, Statues.showcase, meta, 3);
		if(meta>=2) meta-=2;
		world.setBlock(x+dx, y, z+dz, Statues.showcase, meta|4, 3);
		world.setBlock(x-dx, y, z-dz, Statues.showcase, (meta+2)|4, 3);
		
        --stack.stackSize;
		return true;
	}

}