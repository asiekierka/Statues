/**
 * Model Techne class for the showcase
 */

package info.jbcs.minecraft.statues;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelShowcase extends ModelBase {
	ModelRenderer	Shape1;
	ModelRenderer	Shape2;
	ModelRenderer	Shape3;
	ModelRenderer	Shape4;
	ModelRenderer	Below;
	ModelRenderer	Lid;
	ModelRenderer	cradle;

	public ModelShowcase() {
		textureWidth = 128;
		textureHeight = 64;

		Shape1 = new ModelRenderer(this, 0, 42);
		Shape1.addBox(-1F, 0F, 0F, 1, 12, 2);
		Shape1.setRotationPoint(16F, 12F, -8F);
		Shape1.setTextureSize(128, 64);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);

		Shape2 = new ModelRenderer(this, 0, 42);
		Shape2.addBox(0F, 0F, 0F, 1, 12, 2);
		Shape2.setRotationPoint(-16F, 12F, -8F);
		Shape2.setTextureSize(128, 64);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);

		Shape3 = new ModelRenderer(this, 6, 42);
		Shape3.addBox(0F, 0F, -2F, 1, 16, 2);
		Shape3.setRotationPoint(-16F, 8F, 8F);
		Shape3.setTextureSize(128, 64);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);

		Shape4 = new ModelRenderer(this, 6, 42);
		Shape4.addBox(-1F, 0F, -2F, 1, 16, 2);
		Shape4.setRotationPoint(16F, 8F, 8F);
		Shape4.setTextureSize(128, 64);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);

		Below = new ModelRenderer(this, 0, 0);
		Below.addBox(0F, -6F, 0F, 32, 6, 17);
		Below.setRotationPoint(-16F, 14F, -8F);
		Below.setTextureSize(128, 64);
		Below.mirror = true;
		setRotation(Below, 0.3548836F, 0F, 0F);

		Lid = new ModelRenderer(this, 0, 23);
		Lid.addBox(0F, -2F, -17F, 32, 2, 17);
		Lid.setRotationPoint(-16F, 2.5F, 6F);
		Lid.setTextureSize(128, 64);
		Lid.mirror = true;
		setRotation(Lid, 0.3548836F, 0F, 0F);

		cradle = new ModelRenderer(this, 12, 42);
		cradle.addBox(-15F, -2F, 0F, 30, 1, 15);
		cradle.setRotationPoint(0F, 14.5F, -6.666667F);
		cradle.setTextureSize(128, 64);
		cradle.mirror = true;
		setRotation(cradle, 0.3548836F, 0F, 0F);
	}

	/**
	 * Render a static model as defined in the constructor
	 */
	public void renderModel(float f) {
		Shape1.render(f);
		Shape2.render(f);
		Shape3.render(f);
		Shape4.render(f);
		GL11.glScalef(1.01f, 1.01f, 1.01f);
		Below.render(f);
		GL11.glScalef(1.01f, 1.01f, 1.01f);
		Lid.render(f);
		cradle.render(f);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		renderModel(f5);
	}

	/**
	 * Sets the model rotation.
	 */
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
