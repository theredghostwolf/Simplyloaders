package ghostwolf.simplyloaders.blocks;

import java.util.List;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.creativetabs.SimplyloadersTab;
import ghostwolf.simplyloaders.tileentities.TileEntityLoaderBase;
import ghostwolf.simplyloaders.tileentities.TileEntityUnloader;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUnloader extends BlockLoaderBase {
	
	public BlockUnloader() {
		super("unloader", "unloader");
	
	}

	@Override
	public void onActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand
            , EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityLoaderBase) {
     	   if (player.isSneaking()) {
     		   ((TileEntityLoaderBase) te).setOutputSide(side);
     	   } else {
     		   ((TileEntityLoaderBase) te).setInputSide(side);     	      
     	   }
        }
	}
	
}
