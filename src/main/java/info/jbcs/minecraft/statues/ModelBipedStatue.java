package info.jbcs.minecraft.statues;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class ModelBipedStatue extends ModelBiped {
	public ModelBipedStatue(float par1) {
		super(par1);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {

		if (this.isChild) {
			float f6 = 2.0F;
			GL11.glPushMatrix();
			GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
			GL11.glTranslatef(0.0F, 16.0F * par7, 0.0F);
			this.bipedHead.render(par7);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
			this.bipedBody.render(par7);
			this.bipedRightArm.render(par7);
			this.bipedLeftArm.render(par7);
			this.bipedRightLeg.render(par7);
			this.bipedLeftLeg.render(par7);
			this.bipedHeadwear.render(par7);
			GL11.glPopMatrix();
		} else {
			this.bipedHead.render(par7);
			this.bipedBody.render(par7);
			this.bipedRightArm.render(par7);
			this.bipedLeftArm.render(par7);
			this.bipedRightLeg.render(par7);
			this.bipedLeftLeg.render(par7);
			this.bipedHeadwear.render(par7);
		}
	}

}
