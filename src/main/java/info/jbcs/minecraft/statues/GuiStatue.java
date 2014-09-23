/**
 * GUI class for the basic GUI of the statue
 */

package info.jbcs.minecraft.statues;

import info.jbcs.minecraft.gui.GuiScreenPlus;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import pl.asie.lib.network.Packet;

public class GuiStatue extends GuiScreenPlus {
	public final InventoryPlayer	invg;
	public final TileEntityStatue	tile;
	int wx,wy,wz;
	float ila,ira;

	public GuiStatue(InventoryPlayer inv, final TileEntityStatue te, World par2, int x, int y, int z) {
		super(new ContainerStatue(inv, te),176,226,"statues:textures/gui-statue.png");
		invg = inv;
		tile = te;
		wx=x; wy=y; wz=z;
		
		addChild(new Gui2dScroller(121, 92, 43, 13, "statues:textures/gui-sculpt.png",13,13,243,0,ira=te.pose.itemRightA,0) {
			@Override void onChange(){
				ira=te.pose.itemRightA=(float) u;
				te.updateModel();
			}
		});
		
		addChild(new Gui2dScroller(12, 92, 43, 13, "statues:textures/gui-sculpt.png",13,13,243,0,ila=te.pose.itemLeftA,0) {
			@Override void onChange(){
				ila=te.pose.itemLeftA=(float) u;
				te.updateModel();
			}
		});
	}
	
    @Override
	public void onGuiClosed(){
    	try {
    		Packet adjustStatue = Statues.packet.create(Packets.SCULPTURE_ADJUSTMENT)
    			.writeInt(wx).writeInt(wy).writeInt(wz).writeFloat(tile.pose.itemLeftA).writeFloat(tile.pose.itemRightA);
    		Statues.packet.sendToServer(adjustStatue);
    	} catch(Exception e) { e.printStackTrace(); }
    	
    	super.onGuiClosed();
    }

}
