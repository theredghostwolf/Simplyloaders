package ghostwolf.simplyloaders.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {
	
	public static void init () {
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.unloader), "CCC","CcC","ChC",'C',Blocks.COBBLESTONE,'c',Blocks.CHEST,'h',Blocks.HOPPER);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.loader), "ChC","CcC","CCC",'C',Blocks.COBBLESTONE,'c',Blocks.CHEST,'h',Blocks.HOPPER);
	}
	

}
