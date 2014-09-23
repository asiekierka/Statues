package info.jbcs.minecraft.statues;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Random;

public class GeneralStatueClient {
	static Random rand=new Random();
	
	public static EntityDiggingFX addBlockHitEffects(World world, int x, int y, int z, Block block, int meta, int side) {
		EffectRenderer renderer = Minecraft.getMinecraft().effectRenderer;

		if(block==null) return null;
		
		float f = 0.1F;
		double d0 = x + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - f * 2.0F) + f + block.getBlockBoundsMinX();
		double d1 = y + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - f * 2.0F) + f + block.getBlockBoundsMinY();
		double d2 = z + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - f * 2.0F) + f + block.getBlockBoundsMinZ();

		switch (side) {
		case 0:
			d1 = y + block.getBlockBoundsMinY() - f;
			break;
		case 1:
			d1 = y + block.getBlockBoundsMaxY() + f;
			break;
		case 2:
			d2 = z + block.getBlockBoundsMinZ() - f;
			break;
		case 3:
			d2 = z + block.getBlockBoundsMaxZ() + f;
			break;
		case 4:
			d0 = x + block.getBlockBoundsMinX() - f;
			break;
		case 5:
			d0 = x + block.getBlockBoundsMaxX() + f;
			break;
		}

		EntityDiggingFX res = new EntityDiggingFX(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, block, meta, side);
		res.motionX = d0 - (x + 0.5);
		res.motionY = d1 - (y + 0.5);
		res.motionZ = d2 - (z + 0.5);

		renderer.addEffect(res);

		return res;
	}

	public static void spawnSculptEffect(int x, int y, int z, Block block, byte meta) {
		if(block==null) return;
		
		World world = Minecraft.getMinecraft().theWorld;
		for (int side = 0; side < 6; side++) {
			for (int j = 0; j < 32; j++) {
				EntityDiggingFX fx = addBlockHitEffects(Minecraft.getMinecraft().theWorld, x, y, z, block, meta, side);
				if(fx==null) return;
				
				fx.multipleParticleScaleBy(0.25f + 0.5f * rand.nextFloat());
				fx.multiplyVelocity(0.3f * rand.nextFloat());
			}
		}

		Minecraft.getMinecraft().theWorld.playSound(x + 0.5, y + 0.5, z + 0.5, block.stepSound.getBreakSound(), 1.0f, 0.6f + 0.4f * rand.nextFloat(), true);
	}
	
	public static ResourceLocation getBlockIcon(Block block, int side, int meta) {
		String textureName=block.getIcon(side,meta).getIconName();
		String domain="";
		int index=textureName.indexOf(":"); if(index!=-1){
			domain=textureName.substring(0,index+1).toLowerCase();
			textureName=textureName.substring(index+1);
		}
		return new ResourceLocation(domain+"textures/blocks/"+textureName+".png");
	}

	
	public static EntityTextureFX addTextureEffects(World world, int x, int y, int z, AbstractTexture texture, int side,float u,float v) {
		Block block = world.getBlock(x, y, z);
		if(block==null) return null;
		
		double w = block.getBlockBoundsMaxX() - block.getBlockBoundsMinX();
		double h = block.getBlockBoundsMaxY() - block.getBlockBoundsMinY();
		double l = block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ();
		double px = rand.nextDouble() * w;
		double py = rand.nextDouble() * h;
		double pz = rand.nextDouble() * l;
		
		float f = 0.25F;
		double d0 = x + block.getBlockBoundsMinX() + px;
		double d1 = y + block.getBlockBoundsMinY() + py;
		double d2 = z + block.getBlockBoundsMinZ() + pz;

		switch (side) {
		case 0:
			d1 = y + block.getBlockBoundsMinY() - f;
			break;
		case 1:
			d1 = y + block.getBlockBoundsMaxY() + f;
			break;
		case 2:
			d2 = z + block.getBlockBoundsMinZ() - f;
			break;
		case 3:
			d2 = z + block.getBlockBoundsMaxZ() + f;
			break;
		case 4:
			d0 = x + block.getBlockBoundsMinX() - f;
			break;
		case 5:
			d0 = x + block.getBlockBoundsMaxX() + f;
			break;
		}

		double motion=0.5;
		EntityTextureFX res = new EntityTextureFX(world, d0, d1, d2, motion*(px/w-0.5), motion*(py/h-0.5), motion*(pz/l-0.5), texture, u, v);

		return res;
	}

	public static void spawnPaintEffect(World world,int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (! (te instanceof TileEntityStatue))
			return;
		TileEntityStatue statue=(TileEntityStatue) te;
		EntityStatuePlayer player=statue.getStatue();
		
		AbstractTexture tex=player.getTextureSkin();
		
		for(int side=2;side<6;side++){
			for(int i=0;i<80;i++){
				EntityTextureFX p=addTextureEffects(world,x,y+i%2,z,tex,side,0.05f,0.1f);
				if(p!=null) world.spawnEntityInWorld(p);
			}
		}
		
	}

	public static void spawnCopyEffect(World world, int x, int y, int z, int side, float hx, float hy, float hz, TileEntityStatue statue) {
		world.playSound(x + 0.5, y + 0.5, z + 0.5, "statues:copy", 2.0F, world.rand.nextFloat()*0.4f+0.8f, false);

		for(int i=0;i<8;i++){
			EntityDiggingFX fx = addBlockHitEffects(Minecraft.getMinecraft().theWorld, x, y, z, statue.block, statue.meta, side);
			if(fx==null) return;
			
			fx.setPosition(x+hx, y+hy, z+hz);
			fx.multipleParticleScaleBy(0.15f + 0.7f * rand.nextFloat());
			fx.multiplyVelocity(0.3f * rand.nextFloat());		
		}
	}


}
