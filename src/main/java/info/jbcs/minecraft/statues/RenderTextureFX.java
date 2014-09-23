package info.jbcs.minecraft.statues;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderTextureFX extends Render {

	@Override
	public void doRender(Entity entity, double x, double y, double z, float unk, float frame) {
		EntityTextureFX e=(EntityTextureFX) entity;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, e.texture.getGlTextureId());
		Tessellator tessellator = Tessellator.instance;
		
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        
        float u0 = e.pu;
        float u1 = e.pu+e.scaleU;
        float v0 = e.pv;
        float v1 = e.pv+e.scaleV;
        double s=0.14;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV(  s,   s, 0.0D, u1, v1);
        tessellator.addVertexWithUV(0.0,   s, 0.0D, u0, v1);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0D, u0, v0);
        tessellator.addVertexWithUV(  s, 0.0, 0.0D, u1, v0);
        tessellator.draw();
        
        GL11.glPopMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}
