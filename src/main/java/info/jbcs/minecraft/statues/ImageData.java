package info.jbcs.minecraft.statues;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.HashMap;

public class ImageData {
	static HashMap<Integer,ImageData> blockTextures=new HashMap<Integer,ImageData>();

	int w,h;
	int pixels[];
	
	ImageData(int[] pp,int ww,int hh){
		pixels=pp; w=ww; h=hh;
	}
	
	static ImageData invalidData=new ImageData(null,0,0);
	
	static ImageData getBlockTexture(Block block,int side,int meta){
		int key=Block.getIdFromBlock(block)|(meta<<12)|(side<<16);
		ImageData res=blockTextures.get(key);
		
		if(res==invalidData) return null;
		if(res!=null) return res;
		
		
		try {
			ResourceLocation resource=GeneralStatueClient.getBlockIcon(block, side, meta);
			BufferedImage origImage=ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream());
			BufferedImage image = new BufferedImage(origImage.getWidth(), origImage.getHeight(), 2);
			Graphics graphics = image.getGraphics();
			graphics.drawImage(origImage, 0, 0, null);
			graphics.dispose();
			
			res=new ImageData(((DataBufferInt) image.getRaster().getDataBuffer()).getData(),origImage.getWidth(), origImage.getHeight());
			blockTextures.put(key, res);
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		blockTextures.put(key, invalidData);
		return null;
	}


}
