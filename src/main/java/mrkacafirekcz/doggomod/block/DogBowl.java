package mrkacafirekcz.doggomod.block;

import mrkacafirekcz.doggomod.DoggoMod;
import mrkacafirekcz.doggomod.block.entity.DogBowlEntity;
import mrkacafirekcz.doggomod.entity.DoggoWolf;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DogBowl extends BlockWithEntity implements BlockEntityProvider {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.5, 0.0, 2.5, 13.5, 4.0, 13.5);
	
	public DogBowl() {
		super(FabricBlockSettings.of(Material.STONE).strength(2.0f).requiresTool());
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DogBowlEntity(pos, state);
	}
	
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}


	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if(state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DogBowlEntity) {
                ItemScatterer.spawn(world, pos, (DogBowlEntity)blockEntity);
                // update comparators
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if(itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof DogBowlEntity) {
				((DogBowlEntity) blockEntity).setCustomName(itemStack.getName());
			}
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(!world.isClient) {
			NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
			
			if(screenHandlerFactory != null) {
				player.openHandledScreen(screenHandlerFactory);
			}
		}
		
		return ActionResult.SUCCESS;
	}
	
	public static boolean isEqual(Block block) {
		return block instanceof DogBowl;
	}
	
	public static boolean canEatFromBowl(BlockPos pos, DoggoWolf wolfEntity) {
		if(isEqual(wolfEntity.world.getBlockState(pos).getBlock())) {
			DogBowlEntity dogBowlEntity = (DogBowlEntity) wolfEntity.world.getBlockEntity(pos);
			
			if(dogBowlEntity.hasCustomName()) {
				if(wolfEntity.hasCustomName() && wolfEntity.getDisplayName().asString().equals(dogBowlEntity.getCustomName().asString())) {
					return true;
				}
				
				return false;
			}
			
			return true;
		}
		
		return false;
	}
}
