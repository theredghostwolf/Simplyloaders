package ghostwolf.simplyloaders.init;


import ghostwolf.simplyloaders.blocks.BlockLoader;
import ghostwolf.simplyloaders.blocks.BlockUnloader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
	
	public static BlockUnloader unloader;
	public static BlockLoader loader;
	
	
	public static void init () {
		unloader = new BlockUnloader();
		loader = new BlockLoader();

	}
	
	@SideOnly(Side.CLIENT)
	public  static void initModels () {
		unloader.initModel();
		loader.initModel();

	}

}
