package info.jbcs.minecraft.statues;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.tileentity.TileEntity;
import pl.asie.lib.network.MessageHandlerBase;
import pl.asie.lib.network.Packet;

import java.io.IOException;

public class Packets extends MessageHandlerBase {
	public static final int SCULPTURE_CREATION = 1;
	public static final int SCULPTURE_ADJUSTMENT = 2;
	public static final int SCULPTED = 3;
	
	public void onSculptureCreation(Packet packet, EntityPlayer player) throws IOException {
		final int x = packet.readInt();
		final int y = packet.readInt();
		final int z = packet.readInt();
		int face = packet.readByte();
		
		final Block block = player.worldObj.getBlock(x,y,z);
		if(!Statues.canSculpt(block, player.worldObj,x,y,z)) return;

		final int meta=player.worldObj.getBlockMetadata(x,y,z);
		if(!block.equals(player.worldObj.getBlock(x,y+1,z)) || meta !=player.worldObj.getBlockMetadata(x,y+1,z)) return;
		
		player.worldObj.setBlock(x, y, z, Statues.statue,face,3);
		
		TileEntity tileEntity = player.worldObj.getTileEntity(x, y, z);
		if (!(tileEntity instanceof TileEntityStatue))
			return;
		TileEntityStatue entity = (TileEntityStatue) tileEntity;

		entity.skinName=packet.readString();
		entity.pose.read(packet);
		entity.block=block;
		entity.meta=meta;
		entity.facing=2;
		
		player.worldObj.setBlock(x, y+1, z, Statues.statue, face|4,3);
		player.worldObj.notifyBlocksOfNeighborChange(x, y, z, Statues.statue);
		player.worldObj.markBlockForUpdate(x, y, z);
		
		Packet sculpted = Statues.packet.create(Packets.SCULPTED)
				.writeInt(x).writeInt(y).writeInt(z).writeShort((short)Block.getIdFromBlock(block)).writeByte((byte)meta);
		Statues.packet.sendToAllAround(sculpted, player, 64);
	}

	public void onSculptureAdjustment(Packet packet, EntityPlayer player) throws IOException {
		final int x = packet.readInt();
		final int y = packet.readInt();
		final int z = packet.readInt();
		float itemLeftA = packet.readFloat();
		float itemRightA = packet.readFloat();
		
		TileEntity tileEntity = player.worldObj.getTileEntity(x, y, z);
		if (!(tileEntity instanceof TileEntityStatue))
			return;
		TileEntityStatue entity = (TileEntityStatue) tileEntity;

		entity.pose.itemLeftA=itemLeftA;
		entity.pose.itemRightA=itemRightA;
		player.worldObj.markBlockForUpdate(x, y, z);
	}

	public void onSculpted(Packet packet, EntityPlayer player) throws IOException {
		final int x=packet.readInt();
		final int y=packet.readInt();
		final int z=packet.readInt();
		final Block block=Block.getBlockById(packet.readShort());
		final byte meta=packet.readByte();
		
		GeneralStatueClient.spawnSculptEffect(x, y, z, block, meta);
		GeneralStatueClient.spawnSculptEffect(x, y+1, z, block, meta);
	}

	@Override
	public void onMessage(pl.asie.lib.network.Packet packet,
			INetHandler handler, EntityPlayer player, int command)
			throws IOException {
		switch(command) {
			case SCULPTURE_CREATION:
				onSculptureCreation(packet, player);
				break;
			case SCULPTURE_ADJUSTMENT:
				onSculptureAdjustment(packet, player);
				break;
			case SCULPTED:
				onSculpted(packet, player);
				break;
		}
	}

}
