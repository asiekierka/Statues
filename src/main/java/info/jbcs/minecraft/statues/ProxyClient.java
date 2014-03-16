package info.jbcs.minecraft.statues;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ProxyClient extends Proxy {
	@Override
	public void preInit() {

	}

	@Override
	public void init() {

		TileEntityRendererDispatcher.instance.mapSpecialRenderers.put(TileEntityStatue.class, new RenderStatue());
		TileEntityRendererDispatcher.instance.mapSpecialRenderers.put(TileEntityShowcase.class, new RenderShowcase());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityTextureFX.class, new RenderTextureFX());
		
//		RenderingRegistry.registerBlockHandler(new BlockCarpetRenderer());

//		RenderingRegistry.registerEntityRenderingHandler(EntityCloudInABottle.class, new RenderSnowball(Chisel.itemCloudInABottle));
		
//		MinecraftForgeClient.registerItemRenderer(Chisel.needle.itemID, renderer);
	}
}
