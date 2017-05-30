package ghostwolf.simplyloaders.tileentities;

import ghostwolf.simplyloaders.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityLoader extends TileEntityLoaderBase {
	
	@Override 
	public void tick() {
		TileEntity chest = getChest(); //checks if there is an inventory below the loader
		if (chest != null) {
			IItemHandler chestInventory = chest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inputSide.getOpposite());
			if (chestInventory != null) { //gets the chests inventory	
				if (chestHasItem(chestInventory)) {
					
					updateRedstone(false);
					EntityMinecart cart = getChestCart (); //gets the minecart
					if (cart != null) {
						IItemHandler cartInventory = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, outputSide.getOpposite()); //gets the carts inventory
						if (cartInventory != null ) {
								moveItem(chestInventory, cartInventory);
						} else {
							
							//carts has no chest 
							updateRedstone(true);
						}
					}
				} else {
					//chest is empty enable signal
					updateRedstone(true);
				}
			} 
			
		}
		
	}
	
    @Override
    public void setBlockState () {
    	IBlockState state = getWorld().getBlockState(getPos());
    	if (state.getBlock() == ModBlocks.loader) {
    		ModBlocks.loader.updateState(state, getPos(), getWorld());
    	}
    }
    

}
