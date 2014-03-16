package info.jbcs.minecraft.statues;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

public class RenderPlayerStatue extends RendererLivingEntity {
	public static final ResourceLocation	steveTextures	= new ResourceLocation("textures/entity/steve.png");
	public final ModelBipedStatue			modelBipedMain;
	public final ModelBipedStatue			modelArmorChestplate;
	public final ModelBipedStatue			modelArmor;

	public RenderPlayerStatue() {
		super(new ModelBipedStatue(0.0F), 0.5f);
		modelBipedMain = (ModelBipedStatue) mainModel;
		modelArmorChestplate = new ModelBipedStatue(1.0F);
		modelArmor = new ModelBipedStatue(0.5F);
		shadowSize = 0.5f;

		setRenderManager(RenderManager.instance);
	}

	/**
	 * Set the specified armor model as the player model. Args: player,
	 * armorSlot, partialTick
	 */
	protected int setArmorModel(EntityStatuePlayer par1EntityFakePlayer, int par2, float par3) {
		ItemStack itemstack = par1EntityFakePlayer.inventory.armorItemInSlot(3 - par2);

		if (itemstack != null) {
			Item item = itemstack.getItem();

			if (item instanceof ItemArmor) {
				ItemArmor itemarmor = (ItemArmor) item;
				bindTexture(RenderBiped.getArmorResource(par1EntityFakePlayer, itemstack, par2, null));
				ModelBiped modelbiped = par2 == 2 ? modelArmor : modelArmorChestplate;
				modelbiped.bipedHead.showModel = par2 == 0;
				modelbiped.bipedHeadwear.showModel = par2 == 0;
				modelbiped.bipedBody.showModel = par2 == 1 || par2 == 2;
				modelbiped.bipedRightArm.showModel = par2 == 1;
				modelbiped.bipedLeftArm.showModel = par2 == 1;
				modelbiped.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
				modelbiped.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;
				modelbiped = ForgeHooksClient.getArmorModel(par1EntityFakePlayer, itemstack, par2, modelbiped);
				setRenderPassModel(modelbiped);
				modelbiped.onGround = mainModel.onGround;
				modelbiped.isRiding = mainModel.isRiding;
				modelbiped.isChild = mainModel.isChild;
				float f1 = 1.0F;

				// Move outside if to allow for more then just CLOTH
				int j = itemarmor.getColor(itemstack);
				if (j != -1) {
					float f2 = (j >> 16 & 255) / 255.0F;
					float f3 = (j >> 8 & 255) / 255.0F;
					float f4 = (j & 255) / 255.0F;
					GL11.glColor3f(f1 * f2, f1 * f3, f1 * f4);

					if (itemstack.isItemEnchanted()) {
						return 31;
					}

					return 16;
				}

				GL11.glColor3f(f1, f1, f1);

				if (itemstack.isItemEnchanted()) {
					return 15;
				}

				return 1;
			}
		}

		return -1;
	}

	protected void func_130220_b(EntityStatuePlayer par1EntityFakePlayer, int par2, float par3) {
		ItemStack itemstack = par1EntityFakePlayer.inventory.armorItemInSlot(3 - par2);

		if (itemstack != null) {
			Item item = itemstack.getItem();

			if (item instanceof ItemArmor) {
				bindTexture(RenderBiped.getArmorResource(par1EntityFakePlayer, itemstack, par2, "overlay"));
				float f1 = 1.0F;
				GL11.glColor3f(f1, f1, f1);
			}
		}
	}
	
	public void renderItemInRightArm(EntityStatuePlayer player){
		GL11.glPushMatrix();
		modelBipedMain.bipedRightArm.postRender(0.0625F);
		GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
		
		renderItemInArm(player,player.inventory.getStackInSlot(0),player.pose.itemRightA);
		
		GL11.glPopMatrix();
	}
	
	public void renderItemInLeftArm(EntityStatuePlayer player){
		GL11.glPushMatrix();
		modelBipedMain.bipedLeftArm.postRender(0.0625F);
		GL11.glTranslatef(0.0625F, 0.4375F, 0.0625F);
		
		renderItemInArm(player,player.inventory.getStackInSlot(1),1.0f-player.pose.itemLeftA);
		
		GL11.glPopMatrix();
	}
	
	public void renderItemInArm(EntityStatuePlayer player,ItemStack stack,float angle){
		if(stack==null) return;

		if (player.fishEntity != null) {
			stack = new ItemStack(Items.stick);
		}

		EnumAction enumaction = null;

		if (player.getItemInUseCount() > 0) {
			enumaction = stack.getItemUseAction();
		}

		float f11;

		IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(stack, EQUIPPED);
		boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, stack, BLOCK_3D));
		boolean isBlock = stack.getItem() instanceof ItemBlock;

		GL11.glTranslatef(0.0f, 0.0f, -0.35f*(0.5f-Math.abs(0.5f-angle)));
		GL11.glTranslatef(0.0f, 0.25f*angle, 0.0f);
		GL11.glRotatef(angle*180f, 1.0f, 0.0f, 0.0f);
		
		if (is3D || (isBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType()))) {
			f11 = 0.5F;
			GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
			f11 *= 0.75F;
			GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(-f11, -f11, f11);
		} else if (stack.getItem().equals(Items.bow)) {
			f11 = 0.625F;
			GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
			GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f11, -f11, f11);
			GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		} else if (stack.getItem().isFull3D()) {
			f11 = 0.625F;

			if (stack.getItem().shouldRotateAroundWhenRendering()) {
				GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(0.0F, -0.125F, 0.0F);
			}

			if (player.getItemInUseCount() > 0 && enumaction == EnumAction.block) {
				GL11.glTranslatef(0.05F, 0.0F, -0.1F);
				GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
			}

			GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
			GL11.glScalef(f11, -f11, f11);
			GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		} else {
			f11 = 0.375F;
			GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
			GL11.glScalef(f11, f11, f11);
			GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
		}

		float f12;
		float f13;
		int j;
		
		if (stack.getItem().requiresMultipleRenderPasses()) {
			for (j = 0; j < stack.getItem().getRenderPasses(stack.getItemDamage()); ++j) {
				int k = stack.getItem().getColorFromItemStack(stack, j);
				f13 = (k >> 16 & 255) / 255.0F;
				f12 = (k >> 8 & 255) / 255.0F;
				float f6 = (k & 255) / 255.0F;
				GL11.glColor4f(f13, f12, f6, 1.0F);
				renderManager.itemRenderer.renderItem(player, stack, j);
			}
		} else {
			j = stack.getItem().getColorFromItemStack(stack, 0);
			float f14 = (j >> 16 & 255) / 255.0F;
			f13 = (j >> 8 & 255) / 255.0F;
			f12 = (j & 255) / 255.0F;
			GL11.glColor4f(f14, f13, f12, 1.0F);
			renderManager.itemRenderer.renderItem(player, stack, 0);
		}
		
	}

	/**
	 * Method for adding special render rules
	 */
	protected void renderSpecials(EntityStatuePlayer player, float par2) {
		float f1 = 1.0F;
		GL11.glColor3f(f1, f1, f1);
		super.renderEquippedItems(player, par2);
		super.renderArrowsStuckInEntity(player, par2);
		ItemStack stack = player.inventory.armorItemInSlot(3);

		if (stack != null) {
			GL11.glPushMatrix();
			modelBipedMain.bipedHead.postRender(0.0625F);
			float f2;

			if (stack != null && stack.getItem() instanceof ItemBlock) {
				IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(stack, EQUIPPED);
				boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, stack, BLOCK_3D));

				if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType())) {
					f2 = 0.625F;
					GL11.glTranslatef(0.0F, -0.25F, 0.0F);
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(f2, -f2, -f2);
				}

				renderManager.itemRenderer.renderItem(player, stack, 0);
			} else if (stack.getItem().equals(Items.skull)) {
				f2 = 1.0625F;
				GL11.glScalef(f2, -f2, -f2);
				String s = "";

				if (stack.hasTagCompound() && stack.getTagCompound().hasKey("SkullOwner")) {
					s = stack.getTagCompound().getString("SkullOwner");
				}

				TileEntitySkullRenderer.field_147536_b.func_147530_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, stack.getItemDamage(), s);
			}

			GL11.glPopMatrix();
		}
		
		renderItemInRightArm(player);
		renderItemInLeftArm(player);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
		return setArmorModel((EntityStatuePlayer) par1EntityLivingBase, par2, par3);
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2) {
		renderSpecials((EntityStatuePlayer) par1EntityLivingBase, par2);
	}
	
	static final float multiplier=(float) (Math.PI/180);
	void setupModel(ModelBipedStatue model,EntityStatuePlayer player){
		model.bipedRightArm.rotateAngleX =	multiplier * (  0 - 180 * player.pose.armRightB);
		model.bipedRightArm.rotateAngleY =	multiplier * ( 90 - 135 * player.pose.armRightA);
		model.bipedRightArm.rotateAngleZ =	multiplier * 0;
		model.bipedLeftArm.rotateAngleX =	multiplier * (    + 180 * (1.0f-player.pose.armLeftB));
		model.bipedLeftArm.rotateAngleY =	multiplier * (270 - 135 * player.pose.armLeftA);
		model.bipedLeftArm.rotateAngleZ =	multiplier * 180;
		model.bipedRightLeg.rotateAngleX =	multiplier * (120 - 240 * player.pose.legRightB);
		model.bipedRightLeg.rotateAngleY =	multiplier * ( 90 -  90 * player.pose.legRightA);
		model.bipedRightLeg.rotateAngleZ =	0;
		model.bipedLeftLeg.rotateAngleX =	multiplier * (120 - 240 * player.pose.legLeftB);
		model.bipedLeftLeg.rotateAngleY =	multiplier * (-90 +  90 * player.pose.legLeftA);
		model.bipedLeftLeg.rotateAngleZ =	0;
		model.bipedHead.rotateAngleX =		multiplier * (-45 +  90 * player.pose.headB);
		model.bipedHead.rotateAngleY =		multiplier * ( 45 -  90 * player.pose.headA);
		model.bipedHead.rotateAngleZ =		0;
		model.bipedHeadwear.rotateAngleX =	model.bipedHead.rotateAngleX;
		model.bipedHeadwear.rotateAngleY =	model.bipedHead.rotateAngleY;
		model.bipedHeadwear.rotateAngleZ =	model.bipedHead.rotateAngleZ;
	}
	
	float calcHeight(ModelRenderer leg){
		double cos=Math.cos(leg.rotateAngleX); if(cos<0) cos=0;
		float res=(float) (1.0f-Math.abs(cos*Math.cos(leg.rotateAngleZ)));
		if(res<0) res=0;
		return res;
	}

	@Override
	public void doRender(EntityLivingBase par1EntityLivingBase, double x, double y, double z, float unk, float frame) {
		EntityStatuePlayer player = (EntityStatuePlayer) par1EntityLivingBase;

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 0;

		ItemStack itemstack = player.inventory.getCurrentItem();
		if (itemstack != null && player.getItemInUseCount() > 0) {
			EnumAction enumaction = itemstack.getItemUseAction();

			if (enumaction == EnumAction.block) {
				modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 3;
			} else if (enumaction == EnumAction.bow) {
				modelArmorChestplate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = true;
			}
		}
		
		setupModel(modelBipedMain,player);
		setupModel(modelArmor,player);
		setupModel(modelArmorChestplate,player);
		
		float voffl=calcHeight(modelBipedMain.bipedLeftLeg);
		float voffr=calcHeight(modelBipedMain.bipedRightLeg);
		float voff=voffr<voffl?voffr:voffl;
	
		GL11.glRotatef(-45+player.pose.bodyA*90, 0.0f, 1.0f, 0.0f);
		GL11.glTranslatef(0.0f, -player.yOffset, 0.0f);
		GL11.glRotatef(-30+player.pose.bodyB*60, 1.0f, 0.0f, 0.0f);
//        GL11.glScalef(2.0F, 2.0F, 2.0F);
		GL11.glTranslatef(0.0f, player.yOffset, 0.0f);
		
		super.doRender(player, x, y - player.yOffset*(1+voff*0.41f), z, unk, frame);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return ((EntityStatuePlayer) par1Entity).getLocationSkin();
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		doRender((EntityLivingBase) par1Entity, par2, par4, par6, par8, par9);
	}
}
