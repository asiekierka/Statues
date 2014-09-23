/**
 * TileEntity class of the showcase
 */

package info.jbcs.minecraft.statues;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import pl.asie.lib.block.TileEntityInventory;

public class TileEntityShowcase extends TileEntityInventory  {
	public float	lidAngle;
	public float	prevLidAngle;
	private int		ticksSinceSync;
	public int		numUsingPlayers	= 0;

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return "Showcase";
	}

	/**
	 * Called when the container is opened
	 */
	@Override
	public void openInventory() {
		if (worldObj.isRemote)
			return;

		numUsingPlayers++;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	/**
	 * Called when the container is closed
	 */
	@Override
	public void closeInventory() {
		if (worldObj.isRemote)
			return;

		numUsingPlayers--;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if ((++ticksSinceSync % 20) * 4 == 0) {
			;
		}

		prevLidAngle = lidAngle;
		float f = 0.1F;

		if (numUsingPlayers > 0 && lidAngle == 0F) {
			double d = xCoord + 0.5D;
			double d1 = zCoord + 0.5D;

			worldObj.playSoundEffect(d, yCoord + 0.5D, d1, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if ((numUsingPlayers == 0 && lidAngle > 0F) || (numUsingPlayers > 0 && lidAngle < 1F)) {
			float f1 = lidAngle;

			if (numUsingPlayers > 0) {
				lidAngle += f;
			} else {
				lidAngle -= f;
			}

			if (lidAngle > 1F) {
				lidAngle = 1F;
			}
			if (lidAngle < 0F) {
				lidAngle = 0F;
			}

			float f2 = 0.5F;

			if (lidAngle < f2 && f1 >= f2) {
				double d2 = xCoord + 0.5D;
				double d3 = zCoord + 0.5D;

				worldObj.playSoundEffect(d2, yCoord + 0.5D, d3, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
		}
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public Packet getDescriptionPacket() {
		if ((worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 4) != 0)
			return null;

		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		tag.setInteger("users", numUsingPlayers);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
		numUsingPlayers = pkt.func_148857_g().getInteger("users");
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void onInventoryUpdate(int slot) {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

}
