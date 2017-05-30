package ghostwolf.simplyloaders.proxy;

import java.io.File;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.SimplyloadersMod;
import ghostwolf.simplyloaders.init.ModBlocks;
import ghostwolf.simplyloaders.init.ModRecipes;
import ghostwolf.simplyloaders.init.ModTileEntities;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	
	// Config instance
    public static Configuration config;
	
public void preInit(FMLPreInitializationEvent e) {
		
    	File directory = e.getModConfigurationDirectory();
    	config = new Configuration(new File(directory.getPath(), Reference.ConfigFile));
    	Config.readConfig();
	
		ModBlocks.init();
		ModTileEntities.init();

    }

    public void init(FMLInitializationEvent e) {

    	ModRecipes.init();

    }

    public void postInit(FMLPostInitializationEvent e) {
    	   if (config.hasChanged()) {
               config.save();
           }
    }


}
