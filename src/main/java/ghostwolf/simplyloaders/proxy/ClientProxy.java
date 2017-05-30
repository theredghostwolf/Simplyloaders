package ghostwolf.simplyloaders.proxy;

import ghostwolf.simplyloaders.init.ModBlocks;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	 @Override
	    public void preInit(FMLPreInitializationEvent e) {
	        super.preInit(e);
	        ModBlocks.initModels();
	    }
	


}
