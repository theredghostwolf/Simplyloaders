package ghostwolf.simplyloaders.creativetabs;

import ghostwolf.simplyloaders.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SimplyloadersTab {
	
	public static final CreativeTabs simplyloadersTab = new CreativeTabs("simplyloadersTab") {
	    @Override public ItemStack getTabIconItem() {
	        return new ItemStack(ModBlocks.loader);
	    }
	};

}
