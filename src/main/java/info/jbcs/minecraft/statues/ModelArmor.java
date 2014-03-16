/**
 * Model Techne class for the statue armor
 */

package info.jbcs.minecraft.statues;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelArmor extends ModelBase
{
	ModelRenderer jambe_droite;
	ModelRenderer jambe_gauche;
	ModelRenderer corps;
	ModelRenderer bras_droit;
	ModelRenderer bras_gauche;
	ModelRenderer tete;
	
	public ModelArmor()
	{
		this(0.0F);
	}
	
	public ModelArmor(float par1)
	{
		textureWidth = 64;
		textureHeight = 32;
		
		jambe_gauche = new ModelRenderer(this, 0, 16);
		jambe_gauche.mirror = true;
		jambe_gauche.addBox(0F, 0F, 0F, 4, 12, 4, par1);
		jambe_gauche.setRotationPoint(0F, 4F, -2F);
		setRotation(jambe_gauche, 0F, 0F, 0F);
		
		jambe_droite = new ModelRenderer(this, 0, 16);
		jambe_droite.addBox(0F, 0F, 0F, 4, 12, 4, par1);
		jambe_droite.setRotationPoint(-4F, 4F, -2F);
		setRotation(jambe_droite, 0F, 0F, 0F);
		
		corps = new ModelRenderer(this, 16, 16);
		corps.addBox(0F, 0F, 0F, 8, 12, 4, par1);
		corps.setRotationPoint(-4F, -8F, -2F);
		setRotation(corps, 0F, 0F, 0F);
		
		bras_gauche = new ModelRenderer(this, 40, 16);
		bras_gauche.mirror = true;
		bras_gauche.addBox(0F, 0F, 0F, 4, 12, 4, par1);
		bras_gauche.setRotationPoint(4F, -8F, -2F);
		setRotation(bras_gauche, 0F, 0F, 0F);
		
		bras_droit = new ModelRenderer(this, 40, 16);
		bras_droit.addBox(0F, 0F, 0F, 4, 12, 4, par1);
		bras_droit.setRotationPoint(-8F, -8F, -2F);
		setRotation(bras_droit, 0F, 0F, 0F);
		
		tete = new ModelRenderer(this, 0, 0);
		tete.addBox(0F, 0F, 0F, 8, 8, 8, par1);
		tete.setRotationPoint(-4F, -16F, -4F);
		setRotation(tete, 0F, 0F, 0F);
	}

	/**
	 * Render a static model as defined in the constructor
	 */
	public void renderModel(float f)
	{
		bras_gauche.render(f);
		bras_droit.render(f);
		jambe_gauche.render(f);
		jambe_droite.render(f);
		corps.render(f);
		tete.render(f);
	}
	
	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		jambe_droite.render(f5);
		jambe_gauche.render(f5);
		corps.render(f5);
		bras_droit.render(f5);
		bras_gauche.render(f5);
		tete.render(f5);
	}

	/**
	 * Sets the model rotation.
	 */
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	/**
	 * Change the position of the statue depending of if there is a step or not.
	 */
	public void stepExists(boolean exists)
	{
		if(!exists)
		{
			this.jambe_droite.rotationPointY = 12F;
			this.jambe_gauche.rotationPointY = 12F;
			this.corps.rotationPointY = 0F;
			this.bras_droit.rotationPointY = 0F;
			this.bras_gauche.rotationPointY = 0F;
			this.tete.rotationPointY = -8F;
		}
		else
		{
			this.jambe_droite.rotationPointY = 4F;
			this.jambe_gauche.rotationPointY = 4F;
			this.corps.rotationPointY = -8F;
			this.bras_droit.rotationPointY = -8F;
			this.bras_gauche.rotationPointY = -8F;
			this.tete.rotationPointY = -16F;
		}
	}
	
}