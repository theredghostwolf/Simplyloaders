package ghostwolf.simplyloaders.blocks;

import java.util.List;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.creativetabs.SimplyloadersTab;
import ghostwolf.simplyloaders.tileentities.TileEntityLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityLoaderBase;
import ghostwolf.simplyloaders.tileentities.TileEntityUnloader;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLoaderBase extends Block  {
	
	private String Type;
	
	public static final PropertyInteger inputSide = PropertyInteger.create("inputside", 0, 6);
	public static final PropertyInteger outputSide = PropertyInteger.create("outputside", 0, 6);

	public BlockLoaderBase(String name, String type) {
		super(Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setRegistryName(name);
		setUnlocalizedName(Reference.MOD_ID + "." + name);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setCreativeTab(SimplyloadersTab.simplyloadersTab);
		setHardness(1.2F);
		setResistance(10F);
		setSoundType(blockSoundType.METAL);	
		 
		if (type != null) {
			Type = type;
		} else {
			Type = "";
		}
		
		setDefaultState(blockState.getBaseState());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		if (Type == "loader") {
			return new TileEntityLoader();
		} else if (Type == "unloader") {
			return new TileEntityUnloader();
		} else {
			return null;
		}
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		
		TileEntity te = blockAccess.getTileEntity(pos);
	    if (te instanceof TileEntityLoaderBase) {
	        if (((TileEntityLoaderBase) te).isEmittingRedstone ) {
	        	if (Type == "loader") {
	        	
	        	if (Config.LoaderEmitsToAllNearbyBlocks) {
	        		return 15;
	        	} else {
	        		if ( side.getOpposite() == ((TileEntityLoaderBase) te).getOutputSide()) {
	        			return 15;
	        		}
	        		}
	        	}
	        	if (Type == "unloader") {
	        		if (Config.UnloaderEmitsToAllNearbyBlocks) {
		        		return 15;
		        	} else {
		        		if ( side.getOpposite() == ((TileEntityLoaderBase) te).getOutputSide()) {
		        			return 15;
		        		}
		        	}
	        	}
	        } 
	    } 
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par2List, boolean par4)
	{
		if (Type == "loader") {
	par2List.add("\u00A77" + "Place loader under rails, to load items into passing carts");

	par2List.add("\u00A77" + "Place chest below loader, with the items you want to load");
		}
		if (Type == "unloader") {
			par2List.add("\u00A77" + "Place loader under rails, to unload items from passing carts");

			par2List.add("\u00A77" + "Place chest below unloader, to store the unloaded items in");

		}
	par2List.add("");
	
	par2List.add("\u00A77" + "Right click with empty hand to change output");
	
	par2List.add("\u00A77" + "Shift-Right click with empty hand to change input");


	}
	
	@Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    	TileEntity te = worldIn.getTileEntity(pos);
     	if (te instanceof TileEntityLoaderBase) {
     		return state.withProperty(inputSide, ((TileEntityLoaderBase) te).inputSideToInt() ).withProperty(outputSide, ((TileEntityLoaderBase) te).outputSideToInt());
 
     	} else {
     		return state;
     	}
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, inputSide, outputSide);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
       updateState(state, pos, world);
    }
    
   @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
       updateState(state, pos, worldIn);
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.MODEL;
    }
    
    public void updateState (IBlockState state, BlockPos pos, World world) {
    		TileEntity te = world.getTileEntity(pos);
         	if (te instanceof TileEntityLoaderBase) {
         		world.setBlockState(pos, getActualState(state, world, pos) ) ;
         	}
    }
    
   @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
    		EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    		if (playerIn.getHeldItemMainhand().isEmpty()) {
    			if (! worldIn.isRemote) {
    				// handle stuff serverside
    				onActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    			}
    			return true;
    	} else {
    		return false;
    	}
    }
    
    public void onActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
    		EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	//override this to handle on clicked
    }
    
}
