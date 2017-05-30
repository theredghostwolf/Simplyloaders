package ghostwolf.simplyloaders;

import ghostwolf.simplyloaders.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {
	
	 	private static final String CATEGORY_GENERAL = "general";
	 	

	    // This values below you can access elsewhere in your mod:
	 	public static int LoaderTransferRate = 1;
	 	public static boolean LoaderEmitsToAllNearbyBlocks = false;
	 	public static boolean UnloaderEmitsToAllNearbyBlocks = false;
	 	
	    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
	    // exist yet and read the values if it does exist.
	    public static void readConfig() {
	        Configuration cfg = CommonProxy.config;
	        try {
	            cfg.load();
	            initGeneralConfig(cfg);
	        } catch (Exception e1) {
	          System.out.println("ERROR LOADING CONFIG");
	        } finally {
	            if (cfg.hasChanged()) {
	                cfg.save();
	            }
	        }
	    }

	    private static void initGeneralConfig(Configuration cfg) {
	        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
	        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
	        LoaderTransferRate = cfg.getInt("LoaderTransferRate", CATEGORY_GENERAL, LoaderTransferRate, 1, 64, "sets the transfer rate of the loader / unloader, NOTE: might be unstable at values higher then 1 (it shouldnt but im not 100% sure)");
	    
	        LoaderEmitsToAllNearbyBlocks = cfg.getBoolean("LoaderEmitsSignalToAllNearbyBlocks", CATEGORY_GENERAL, LoaderEmitsToAllNearbyBlocks, "loader will emit a signal to all sides if true, if false it will only emit signal towards the cart");
	        
	        UnloaderEmitsToAllNearbyBlocks = cfg.getBoolean("UnloaderEmitsSignalToAllNearbyBlocks", CATEGORY_GENERAL, UnloaderEmitsToAllNearbyBlocks, "unloader will emit a signal to all sides if true, if false it will only emit signal towards the cart");
	    }
}


