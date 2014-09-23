/**
 * TileEntity class of the statue
 */

package info.jbcs.minecraft.statues;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import pl.asie.lib.block.TileEntityInventory;

import java.util.Random;

public class TileEntityStatue extends TileEntityInventory {
	private EntityPlayer	clientPlayer;
	public String			skinName	= "";
	public StatueParameters	pose		= new StatueParameters();

	public Block			block		= Blocks.stone;
	public int				meta		= 0;
	public int				facing		= 0;
	boolean					updated		= true;

	void randomize(Random rand){
	}

	@Override
	public int getSizeInventory() {
		return 6;
	}

	public EntityStatuePlayer getStatue(){
		if(clientPlayer==null){
			EntityStatuePlayer player=new EntityStatuePlayer(worldObj, skinName);
			player.ticksExisted=10;
			player.pose=pose;
			player.applySkin(skinName, block, facing, meta);

			clientPlayer=player;
			for(int i = 0; i < 6; i++) {
				this.onInventoryUpdate(i);
			}
		}
		
		return (EntityStatuePlayer)clientPlayer;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		skinName = nbttagcompound.getString("skin");
		pose.readFromNBT(nbttagcompound);
		
		block=Block.getBlockById(nbttagcompound.getShort("blockId"));
		if(block==null) block=Blocks.stone;
		meta=nbttagcompound.getByte("meta");
		facing=nbttagcompound.getByte("face");
		
		updateModel();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		
		nbttagcompound.setString("skin", skinName);
		pose.writeToNBT(nbttagcompound);
		
		nbttagcompound.setShort("blockId",(short)Block.getIdFromBlock(block));
		nbttagcompound.setByte("meta",(byte)meta);
		nbttagcompound.setByte("face",(byte)facing);
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {

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
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
		
		if(worldObj.isRemote && Minecraft.getMinecraft().currentScreen instanceof GuiStatue){
			GuiStatue gui=(GuiStatue) Minecraft.getMinecraft().currentScreen;
			pose.itemLeftA=gui.ila;
			pose.itemRightA=gui.ira;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}
	
	public void updateModel() {
		if(clientPlayer!=null && worldObj!=null && worldObj.isRemote){
			((EntityStatuePlayer)clientPlayer).applySkin(skinName, block, facing, meta);
		}
		
		updated=false;
	}
	
    @Override
	public void updateEntity(){
    	if(updated) return;
    	updated=true;
    	
    }

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void onInventoryUpdate(int slot) {
		if(clientPlayer != null) {		
			clientPlayer.inventory.mainInventory[0]=getStackInSlot(4);
			clientPlayer.inventory.mainInventory[1]=getStackInSlot(5);
			clientPlayer.inventory.armorInventory[0]=getStackInSlot(3);
			clientPlayer.inventory.armorInventory[1]=getStackInSlot(2);
			clientPlayer.inventory.armorInventory[2]=getStackInSlot(1);
			clientPlayer.inventory.armorInventory[3]=getStackInSlot(0);
		}

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

}
