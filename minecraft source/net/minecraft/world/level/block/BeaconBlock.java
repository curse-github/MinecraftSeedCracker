/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BeaconBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class BeaconBlock extends BaseEntityBlock implements BeaconBeamBlock {
/* 19 */   public static final MapCodec<BeaconBlock> CODEC = simpleCodec(BeaconBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 23 */   public MapCodec<BeaconBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public BeaconBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public DyeColor getColor() { return DyeColor.WHITE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new BeaconBlockEntity(worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return createTickerHelper(type, BlockEntityType.BEACON, BeaconBlockEntity::tick); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 47 */     if (!level.isClientSide()) { BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof BeaconBlockEntity) { BeaconBlockEntity beacon = (BeaconBlockEntity)blockEntity;
/* 48 */         player.openMenu(beacon);
/* 49 */         player.awardStat(Stats.INTERACT_WITH_BEACON); }
/*    */        }
/* 51 */      return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BeaconBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */