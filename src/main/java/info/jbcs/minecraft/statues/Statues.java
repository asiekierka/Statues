package info.jbcs.minecraft.statues;


import info.jbcs.minecraft.utilities.DummyContainer;
import info.jbcs.minecraft.utilities.General;
import info.jbcs.minecraft.utilities.GuiHandler;

import java.io.File;

import pl.asie.lib.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "statues", name = "Statues", version = "2.1.3", dependencies = "required-after:asielib")
public class Statues{
	static Configuration				config;

	@Instance("Statues")
	public static Statues instance;

	@SidedProxy(clientSide = "info.jbcs.minecraft.statues.ProxyClient", serverSide = "info.jbcs.minecraft.statues.Proxy")
	public static Proxy proxy;

	public static PacketHandler packet;
	
	public static Block			statue;
	public static Block			showcase;
	public static Item			hammer;
	public static Item			itemPalette;
	public static Item			itemStatue;
	public static Item			itemShowcase;

	public static IIcon			slotHand;
	
	public static GuiHandler	guiShowcase;
	public static GuiHandler	guiStatue;
	public static GuiHandler	guiSculpt;

	public static String skinServerLocation;
	
	public static boolean		debugImages;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configFile=event.getSuggestedConfigurationFile();
		config = new Configuration(configFile);
		config.load();

		statue				= new BlockStatue(Material.rock).setHardness(1F).setResistance(1F).setBlockName("statues.statue").setStepSound(Block.soundTypeStone);
		showcase			= new BlockShowcase(Material.wood).setHardness(1F).setResistance(1F).setBlockName("statues.showcase").setStepSound(Block.soundTypeWood);
		hammer				= new ItemMarteau().setTextureName("statues:marteau").setUnlocalizedName("statues.marteau");
		itemShowcase		= new ItemShowcase(showcase).setTextureName("statues:itemshowcase").setUnlocalizedName("statues.showcase");
		itemPalette			= new ItemPalette().setTextureName("statues:palette").setUnlocalizedName("statues.palette");
		
		GameRegistry.registerBlock(statue, "statues.statue");
		GameRegistry.registerBlock(showcase, "statues.showcase");
		GameRegistry.registerItem(hammer, "statues.marteau");
		GameRegistry.registerItem(itemShowcase, "statues.item.showcase");
		GameRegistry.registerItem(itemPalette, "statues.item.palette");
		
		proxy.preInit();
 	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		skinServerLocation = config.get("general", "skin server location", "http://skins.minecraft.net/MinecraftSkins/", "Download skins from this path.").getString();
		debugImages = config.get("general", "debug skins", false, "Save generated skins to files.").getBoolean(false);
		
		packet = new PacketHandler("statues", new Packets(), new Packets());
		
		/*LanguageRegistry.addName(statue, "Statue");
		LanguageRegistry.addName(showcase, "Showcase");
		LanguageRegistry.addName(itemShowcase, "Showcase");
		
		LanguageRegistry.addName(hammer, "Hammer");
		LanguageRegistry.addName(itemPalette, "Palette");*/
		
        GameRegistry.registerTileEntity(TileEntityStatue.class, "TileEntityStatue");
        GameRegistry.registerTileEntity(TileEntityShowcase.class, "TileEntityShowcase");
        
		GameRegistry.addShapedRecipe(new ItemStack(hammer, 1), new Object[] { " I ", " SI", "S  ", Character.valueOf('S'), Items.stick, Character.valueOf('I'), Items.iron_ingot });
		GameRegistry.addShapedRecipe(new ItemStack(itemShowcase, 1), new Object[] { "GGG", "W W", "S S", Character.valueOf('S'), Items.stick, Character.valueOf('W'), Blocks.planks, Character.valueOf('G'), Blocks.glass_pane});
		GameRegistry.addShapedRecipe(new ItemStack(itemPalette, 1), new Object[] { "GB", "RW",
			Character.valueOf('W'), Blocks.planks,
			Character.valueOf('R'), new ItemStack(Items.dye,1,1),
			Character.valueOf('G'), new ItemStack(Items.dye,1,2),
			Character.valueOf('B'), new ItemStack(Items.dye,1,4),
		});

		// TODO
		//MinecraftForge.setBlockHarvestLevel(showcase, "axe", 0);

        guiShowcase=new GuiHandler("showcase"){
			@Override
			public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		        TileEntity tileEntity = world.getTileEntity(x, y, z);

		        if(! (tileEntity instanceof TileEntityShowcase))
		        	return null;
		        
		        TileEntityShowcase e=(TileEntityShowcase) tileEntity;
		        
		        return new ContainerShowcase(player.inventory, e);
			}

			@Override
			public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);

		        if(! (tileEntity instanceof TileEntityShowcase))
		        	return null;
		        
		        TileEntityShowcase e=(TileEntityShowcase) tileEntity;

                return new GuiShowcase(player.inventory,e,world,x,y,z);
			}
		};
        
        guiStatue=new GuiHandler("statue"){
			@Override
			public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		        TileEntity tileEntity = world.getTileEntity(x, y, z);

		        if(! (tileEntity instanceof TileEntityStatue))
		        	return null;
		        
		        TileEntityStatue e=(TileEntityStatue) tileEntity;
		        
		        return new ContainerStatue(player.inventory, e);
			}

			@Override
			public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);

		        if(! (tileEntity instanceof TileEntityStatue))
		        	return null;
		        
		        TileEntityStatue e=(TileEntityStatue) tileEntity;

                return new GuiStatue(player.inventory,e,world,x,y,z);
			}
		};
		
		guiSculpt=new GuiHandler("sculpt"){
			@Override
			public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		        return new DummyContainer();
			}

			@Override
			public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
				int face = MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3;

                return new GuiSculpt(world,x,y,z,player,face);
			}
		};

		GuiHandler.register(this);
        
		// TODO
		//PacketHandler.register(this);
		
        proxy.init();
        
        config.save();
	}

	public static boolean canSculpt(Block block,World world,int x,int y,int z) {
		if(block==null) return false;
		if(block.equals(Blocks.bedrock)) return false;
		if(block.getMaterial() == Material.circuits) return false;
		if(block.getMaterial() == Material.fire) return false;
		if(block.getMaterial() == Material.lava) return false;
		if(block.getMaterial() == Material.water) return false;
		if(world.getTileEntity(x, y, z)!=null) return false;
		
		return true;
	}
}