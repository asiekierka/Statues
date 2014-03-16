package info.jbcs.minecraft.statues;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.init.Blocks;

public class ImageStatueBufferDownload implements IImageBuffer {
	EntityStatuePlayer player;
	Block block;
	int blockSide;
	int blockMeta;
	private int[]	imageData;
	private int		imageWidth;
	private int		imageHeight;
	String debugName;


	public ImageStatueBufferDownload(EntityStatuePlayer entityFakePlayer, Block b, int side, int meta,String dn) {
		player=entityFakePlayer;
		block=b;
		blockSide=side;
		blockMeta=meta;
		debugName=dn;
	}

	@Override
	public BufferedImage parseUserSkin(BufferedImage par1BufferedImage) {
		if (par1BufferedImage == null) {
			return null;
		} else {
			this.imageWidth = 64;
			this.imageHeight = 32;
			BufferedImage bufferedimage1 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
			Graphics graphics = bufferedimage1.getGraphics();
			graphics.drawImage(par1BufferedImage, 0, 0, (ImageObserver) null);
			graphics.dispose();
			imageData = ((DataBufferInt) bufferedimage1.getRaster().getDataBuffer()).getData();
			
			setAreaOpaque(0, 0, 32, 16);
			setAreaOpaque(0, 16, 64, 32);
			
			if(!block.equals(Blocks.bedrock))
				blendArea(0, 0, 64, 32, block, blockSide, blockMeta);
			
			if(Statues.debugImages)
			try {
				ImageIO.write(bufferedimage1, "PNG", new File(debugName+".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			return bufferedimage1;
		}
	}

	private void blendArea(int x1, int y1, int x2, int y2,Block block,int side,int meta) {
		ImageData data=ImageData.getBlockTexture(block, side, meta);
		if(data==null) data=ImageData.getBlockTexture(Blocks.stone, 0, 0);
		if(data==null) return;
		
		int stone[]=data.pixels;
		int w=data.w;
		int h=data.h;
		
		for (int x = x1; x < x2; ++x) {
			for (int y = y1; y < y2; ++y) {
				int rgba = imageData[x + y * imageWidth] & 0xffffffff;
				double a = ((rgba>>24)&0xff)/255.0;
				
				double L = (0.2125 * ((rgba >> 0) & 0xff) / 255.0) + (0.7154 * ((rgba >> 8) & 0xff) / 255.0) + (0.0721 * ((rgba >> 16) & 0xff) / 255.0);
				L*=4.0/3; if(L>1) L=1;
				int srgba=stone[(x%w) + (y%h) * w];
				
				double R=((srgba>> 0)&0xff)/255.0;
				double G=((srgba>> 8)&0xff)/255.0;
				double B=((srgba>>16)&0xff)/255.0;
				double A=((srgba>>24)&0xff)/255.0;
				
				/* overlay blending mode */
				R = R < 0.5 ? 2 * R * L : 1 - 2 * (1 - R) * (1 - L);
				G = G < 0.5 ? 2 * G * L : 1 - 2 * (1 - G) * (1 - L);
				B = B < 0.5 ? 2 * B * L : 1 - 2 * (1 - B) * (1 - L);
				
				int sr=(int) (R*0xff);
				int sg=(int) (G*0xff);
				int sb=(int) (B*0xff);
				
				imageData[x + y * imageWidth] = (sr<<0) | (sg<<8) | (sb<<16) | (((int)(a*A*0xff))<<24);
			}
		}
	}

	/**
	 * Makes the given area of the image transparent if it was previously
	 * completely opaque (used to remove the outer layer of a skin around the
	 * head if it was saved all opaque; this would be redundant so it's assumed
	 * that the skin maker is just using an image editor without an alpha
	 * channel)
	 */
	private void setAreaTransparent(int par1, int par2, int par3, int par4) {
		if (!this.hasTransparency(par1, par2, par3, par4)) {
			for (int i1 = par1; i1 < par3; ++i1) {
				for (int j1 = par2; j1 < par4; ++j1) {
					this.imageData[i1 + j1 * this.imageWidth] &= 16777215;
				}
			}
		}
	}

	/**
	 * Makes the given area of the image opaque
	 */
	private void setAreaOpaque(int par1, int par2, int par3, int par4) {
		for (int i1 = par1; i1 < par3; ++i1) {
			for (int j1 = par2; j1 < par4; ++j1) {
				this.imageData[i1 + j1 * this.imageWidth] |= -16777216;
			}
		}
	}

	/**
	 * Returns true if the given area of the image contains transparent pixels
	 */
	private boolean hasTransparency(int par1, int par2, int par3, int par4) {
		for (int i1 = par1; i1 < par3; ++i1) {
			for (int j1 = par2; j1 < par4; ++j1) {
				int k1 = this.imageData[i1 + j1 * this.imageWidth];

				if ((k1 >> 24 & 255) < 128) {
					return true;
				}
			}
		}

		return false;
	}

}
