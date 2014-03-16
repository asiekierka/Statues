/**
 * Render class for the showcase
 */

package info.jbcs.minecraft.statues;

import info.jbcs.minecraft.utilities.GeneralClient;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderShowcase extends TileEntitySpecialRenderer {
	private final ModelShowcase	showcase;
	RenderItem renderer = new RenderItem();

	public RenderShowcase() {
		showcase = new ModelShowcase();
		
		renderer.setRenderManager(RenderManager.instance);
	}

	/**
	 * Render a model at the given coordinates
	 */
	public void renderAModelAt(TileEntityShowcase tileentity1, double x, double y, double z, float f) {
		int meta = tileentity1.getBlockMetadata();
		Block block = tileentity1.getBlockType();
		
		if((meta&4)!=0) return;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		
		switch(meta){
		case 0: GL11.glRotatef(  0, 0.0F, 1.0F, 0.0F); break;
		case 1: GL11.glRotatef( 90, 0.0F, 1.0F, 0.0F); break;
		case 2: GL11.glRotatef(180, 0.0F, 1.0F, 0.0F); break;
		case 3: GL11.glRotatef(270, 0.0F, 1.0F, 0.0F); break;
		}
		
		switch(meta){
		case 0: GL11.glRotatef(180f, 0.0F, 0.0F, 1.0F); break;
		case 1: GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F); break;
		case 2: GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F); break;
		case 3: GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F); break;
		}

		float rotateLid = tileentity1.prevLidAngle + (tileentity1.lidAngle - tileentity1.prevLidAngle) * f;

		rotateLid = 1.1F - rotateLid;
		rotateLid = 1.1F - rotateLid * rotateLid * rotateLid;

		GeneralClient.bind("statues:textures/showcase.png");
		showcase.Lid.rotateAngleX = -((rotateLid * (float) Math.PI) / 2.0F);
		showcase.renderModel(0.0625F);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();

		ItemStack stack = tileentity1.getStackInSlot(0);
		if (stack != null){
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F-0.585F, (float) z + 0.5F);
			renderItem(tileentity1, Minecraft.getMinecraft().thePlayer, stack,meta, f);
			GL11.glPopMatrix();
		}
	}

	public void renderItem(TileEntityShowcase tile, EntityLivingBase par1EntityLiving, ItemStack stack,int meta,float frame) {
		switch(meta){
		case 0: GL11.glRotatef(  0, 0.0F, 1.0F, 0.0F); break;
		case 1: GL11.glRotatef( 90, 0.0F, 1.0F, 0.0F); break;
		case 2: GL11.glRotatef(180, 0.0F, 1.0F, 0.0F); break;
		case 3: GL11.glRotatef(270, 0.0F, 1.0F, 0.0F); break;
		}
		
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			GL11.glScalef(1.2f, 1.2f, 1.2f);
			GL11.glRotatef(((meta == 0 || meta == 2) ? -20.33f : 20.33f), 1.0f, 0.0f, 0.0f);
		} else {
			GL11.glScalef(1.5f, 1.5f, 1.5f);
			
			boolean rotate=false;
			
			if(item instanceof ItemSword) rotate=true;

			switch (meta) {
			case 0: case 2:
				GL11.glTranslatef(0.0f, -0.1f, -0.22f);
				GL11.glRotatef(90 - 20.33f, 1.0f, 0.0f, 0.0f);
				break;
			case 1: case 3:
				GL11.glTranslatef(0.0f, -0.1f, 0.22f);
				GL11.glRotatef(-90 + 20.33f, 1.0f, 0.0f, 0.0f);
				break;
			}
			
			if (rotate) {
				GL11.glTranslatef(0.15f, 0.05f, 0.0f);
				GL11.glRotatef(45, 0.0f, 0.0f, 1.0f);
			}
		}
		
		EntityItem entity = new EntityItem(null, tile.xCoord, tile.yCoord, tile.zCoord, stack);
		entity.hoverStart = 0;
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		try {
			renderer.doRender(entity, 0, 0, 0, 0, 0.0f);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

	/**
	 * Render a TileEntity at the given coordinates
	 */
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		renderAModelAt((TileEntityShowcase) tileentity, d, d1, d2, f);
	}
}