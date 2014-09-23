package info.jbcs.minecraft.statues;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

public class EntityTextureFX extends Entity{
	AbstractTexture texture;
	private int	particleAge;
	public int particleMaxAge;
	static Random rand=new Random();
	public float scaleU,scaleV;
	float pu,pv;
	
	public EntityTextureFX(World world, double x, double y, double z, double vx, double vy, double vz,AbstractTexture tex,float particleScaleU,float particleScaleV) {
		super(world);
        
		setPosition(x,y,z);
		setSize(0.2f,0.2f);
		
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
        motionX = vx;
        motionY = vy;
        motionZ = vz;
        
        texture=tex;	
        
        particleAge=0;
        particleMaxAge=2+rand.nextInt(12);
        scaleU=particleScaleU;
        scaleV=particleScaleV;
        
        pu=rand.nextFloat()*(1.0f-scaleU);
        pv=rand.nextFloat()*(1.0f-scaleV);
	}

	@Override
	protected void entityInit() {
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge) {
			setDead();
		}

		motionY -= 0.1;
		moveEntity(motionX, motionY, motionZ);
//		motionX *= 0.9800000190734863D;
//		motionY *= 0.9800000190734863D;
//		motionZ *= 0.9800000190734863D;

//		if (onGround) {
//			motionX *= 0.699999988079071D;
//			motionZ *= 0.699999988079071D;
//		}
	}
}
