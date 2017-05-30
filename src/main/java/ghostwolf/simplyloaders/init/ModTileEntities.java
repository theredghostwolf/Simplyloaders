package ghostwolf.simplyloaders.init;

import ghostwolf.simplyloaders.Reference;

import ghostwolf.simplyloaders.tileentities.TileEntityLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityLoaderBase;
import ghostwolf.simplyloaders.tileentities.TileEntityUnloader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {

	public static void init () {
		GameRegistry.registerTileEntity(TileEntityUnloader.class, Reference.MOD_ID + ":TileEntityUnloader");
		GameRegistry.registerTileEntity(TileEntityLoader.class, Reference.MOD_ID + ":TileEntityLoader");
	
	}
	
}
