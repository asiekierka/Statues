/**
 * Render class for the statue
 */

package info.jbcs.minecraft.statues;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RenderStatue extends TileEntitySpecialRenderer {
	RenderPlayerStatue renderer=new RenderPlayerStatue();
	
	public RenderStatue() {
		renderer.setRenderManager(RenderManager.instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float frame) {
		TileEntityStatue tile=(TileEntityStatue)tileentity;
		int meta=tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
		
		if((meta&4)!=0)
			return;

		EntityStatuePlayer player=tile.getStatue();
		if(player==null) return;

		GL11.glPushMatrix();
		
		if(meta==0) meta=2;
		else if(meta==2) meta=0;
		
		GL11.glTranslatef(0.5f, 1.65f, 0.5f);
		GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(90*meta, 0.0F, 1.0F, 0.0F);
		
		renderer.doRender(player, 0, 0, 0, 0, frame);
		GL11.glPopMatrix();
	}
}
