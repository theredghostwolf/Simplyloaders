package ghostwolf.simplyloaders.tileentities;


import java.util.List;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityLoaderBase extends TileEntity implements ICapabilityProvider, ITickable{
	
	public boolean isEmittingRedstone = false;
	public int transferRate = Config.LoaderTransferRate;
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return (oldState.getBlock() != newState.getBlock());
	}
	
	public EnumFacing inputSide = EnumFacing.DOWN;
	public EnumFacing outputSide = EnumFacing.UP;
		
	public TileEntity getChest () {
		
		if (inputSide == null) {
			return null;
		}
		
		TileEntity Te = null;
		
		if (inputSide == EnumFacing.UP) {
			 Te = getWorld().getTileEntity(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
		} else if (inputSide == EnumFacing.DOWN) {
			 Te = getWorld().getTileEntity(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
		} else if (inputSide == EnumFacing.NORTH) {
			 Te = getWorld().getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1));
		} else if (inputSide == EnumFacing.SOUTH) {
			 Te = getWorld().getTileEntity(new BlockPos(pos.getX(), pos.getY() , pos.getZ() + 1));
		}else if (inputSide == EnumFacing.WEST) {
			 Te = getWorld().getTileEntity(new BlockPos(pos.getX() - 1, pos.getY() , pos.getZ()));
		} else {
			Te = getWorld().getTileEntity(new BlockPos(pos.getX() + 1, pos.getY() , pos.getZ()));
		}
		
		if (Te != null) {
			IItemHandler itemHandler = Te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inputSide.getOpposite());
			if (itemHandler != null) {
				return Te;
			}
		}
		if (! isEmittingRedstone) {
			updateRedstone(true);
		}
		return null;
	}
	
	public boolean chestHasItem (IItemHandler chest) {
		for (int i = 0; i < chest.getSlots(); i++) {
			ItemStack inslot = chest.getStackInSlot(i);
			if (inslot != null && ! inslot.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public EntityMinecart getChestCart () {
		if (outputSide == null) {
			return null;
		}
		AxisAlignedBB box = new AxisAlignedBB(getPos(),getPos());

		if (outputSide == EnumFacing.UP) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
		} else if (outputSide == EnumFacing.DOWN) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() - 1, pos.getZ() + 1);
		} else if (outputSide == EnumFacing.NORTH) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() -1);
		} else if (outputSide == EnumFacing.SOUTH) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 2);
		}else if (outputSide == EnumFacing.WEST) {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() - 1, pos.getY() + 1 , pos.getZ() + 1);
		} else  {
			box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 2, pos.getY() + 1, pos.getZ() + 1);
		}
	

		List<EntityMinecart> L = getWorld().getEntitiesWithinAABB(EntityMinecart.class, box);
		if (! L.isEmpty()) {
			EntityMinecart cart = L.get(0);
			return cart;
		}
		return null;
	}
	
	//chests represents input chest and cart represents output chest so its moves items from value1 to value2
	public void moveItem (IItemHandler chest, IItemHandler cart) {
		
		boolean itemMoved = false;
		for (int i = 0; i < chest.getSlots(); i++) {
			if (! chest.getStackInSlot(i).isEmpty()) {
				// found item
				
				ItemStack extractedItems = chest.extractItem(i, transferRate, true);
				//attemps to add the item to the cart until its succesfull, if its fails assume cart is full and enable redstone
				for (int x = 0; x < cart.getSlots(); x++) {
					ItemStack insertedItems = cart.insertItem(x, extractedItems, true);
					if (insertedItems.isEmpty()) {
						//insertion successfull proceed to add item
						ItemStack exI = chest.extractItem(i, transferRate, false);
						ItemStack inI = cart.insertItem(x, exI, false);
						itemMoved = true;
						break;
					}
				} 
			} if (itemMoved) {
				//if an item was moved  break loop to prevent multiple stacks being  transfered per tick
				break;
			} 
		} if (! itemMoved) {
			//cart is full
			updateRedstone(true);
		}
	}
	
	public void updateRedstone (boolean value) {
		if (! getWorld().isRemote) {
			if (isEmittingRedstone != value) {
				isEmittingRedstone = value;
				updateNeighbors();
			}
		}
	}

	@Override
	public void update() {
		if (! getWorld().isRemote) {
			tick();
		}
	}
	
	//override this for update function
	public void tick () {
		
	}
	
	public void setTransferRate (int rate) {
		transferRate = rate;
	}
	
	public void setInputSide (EnumFacing side) {
		if (side != inputSide) {
			if (side == outputSide) {
				outputSide = null;
			}
			inputSide = side;
			updateNeighbors();
			markDirty();
			setBlockState();
		}

	}
	
	public void setOutputSide (EnumFacing side) {
		if (side != outputSide) {
			if (side == inputSide) {
				inputSide = null;
			}
			outputSide = side;
			updateNeighbors();
			markDirty();
			setBlockState();
		}
	}
	
	 @Override
	    public void readFromNBT(NBTTagCompound compound) {
	        super.readFromNBT(compound);
	        if (compound.getString("inputside") != "none") {
	        	inputSide = EnumFacing.byName(compound.getString("inputside"));
	        } else {
	        	inputSide = null;
	        }
	        
	        if (compound.getString("outputside") != "none") {
	        	outputSide = EnumFacing.byName(compound.getString("outputside"));
	        } else {
	        	outputSide = null;
	        }
	    }

	    @Override
	    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
	        super.writeToNBT(compound);
	       if (inputSide != null) {
	    	   compound.setString("inputside", inputSide.toString());
	       } else {
	    		  compound.setString("inputside", "none"); 
	    	   }
	       
	       if (outputSide != null) {
	    	   compound.setString("outputside", outputSide.toString());
	       } else {
	    	   compound.setString("outputside", "none");
	       }
	        return compound;
	    }
	    
	    public EnumFacing getInputSide () {
	    	return inputSide;
	    }
	    
	    public EnumFacing getOutputSide () {
	    	return outputSide;
	    }
	    
	    
	    private void updateNeighbors () {
	    	getWorld().notifyNeighborsOfStateChange(getPos(), ModBlocks.loader, false);
	    }
	    
	    public int inputSideToInt () {
	    	if (inputSide == null) {
	    		return 0;
	    	} else if (inputSide == EnumFacing.UP) {
	    		return 1;
	    	} else if (inputSide == EnumFacing.DOWN) {
	    		return 2;
	    	} else if (inputSide == EnumFacing.EAST) {
	    		return 3;
	    	} else if (inputSide == EnumFacing.WEST) {
	    		return 4;
	    	} else if (inputSide == EnumFacing.SOUTH) {
	    		return 5;
	    	} else if (inputSide == EnumFacing.NORTH) {
	    		return 6;
	    	}
	    	
	    	return 0;
	    }
	    
	    public int outputSideToInt () {
	    	if (outputSide == null) {
	    		return 0;
	    	} else if (outputSide == EnumFacing.UP) {
	    		return 1;
	    	} else if (outputSide == EnumFacing.DOWN) {
	    		return 2;
	    	} else if (outputSide == EnumFacing.EAST) {
	    		return 3;
	    	} else if (outputSide == EnumFacing.WEST) {
	    		return 4;
	    	} else if (outputSide == EnumFacing.SOUTH) {
	    		return 5;
	    	} else if (outputSide == EnumFacing.NORTH) {
	    		return 6;
	    	}
	    	
	    	return 0;
	    }
	    
	    public void setBlockState() {
	    	//override this to set the state.
	    }
	    
	    @Override
	    public void handleUpdateTag(NBTTagCompound tag) {
	    	readFromNBT(tag);
	    	setBlockState();
	    }
	    
	    @Override
	    public NBTTagCompound getUpdateTag() {
	    	return writeToNBT(new NBTTagCompound());
	    }
	    
	    @Override
	    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
	    	super.onDataPacket(net, pkt);
	    	handleUpdateTag(pkt.getNbtCompound());
	    }
	 
	    @Override
	    public SPacketUpdateTileEntity getUpdatePacket() {
	         return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
	    }
	    
	    
	      
}
