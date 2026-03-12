/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ConcretePowderBlock extends FallingBlock {
/* 19 */   public static final MapCodec<ConcretePowderBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK
/* 20 */         .byNameCodec().fieldOf("concrete").forGetter(()), 
/* 21 */         propertiesCodec())
/* 22 */       .apply(i, ConcretePowderBlock::new));
/*    */   
/*    */   private final Block concrete;
/*    */   
/* 26 */   public MapCodec<ConcretePowderBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConcretePowderBlock(Block concrete, BlockBehaviour.Properties properties) {
/* 32 */     super(properties);
/* 33 */     this.concrete = concrete;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedBlock, FallingBlockEntity entity) {
/* 38 */     if (shouldSolidify(level, pos, replacedBlock)) {
/* 39 */       level.setBlock(pos, this.concrete.defaultBlockState(), 3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 45 */     Level level1 = context.getLevel();
/* 46 */     BlockPos pos = context.getClickedPos();
/* 47 */     BlockState replacedBlock = level1.getBlockState(pos);
/*    */     
/* 49 */     if (shouldSolidify(level1, pos, replacedBlock)) {
/* 50 */       return this.concrete.defaultBlockState();
/*    */     }
/* 52 */     return super.getStateForPlacement(context);
/*    */   }
/*    */ 
/*    */   
/* 56 */   private static boolean shouldSolidify(BlockGetter level, BlockPos pos, BlockState replacedBlock) { return (canSolidify(replacedBlock) || touchesLiquid(level, pos)); }
/*    */ 
/*    */   
/*    */   private static boolean touchesLiquid(BlockGetter level, BlockPos pos) {
/* 60 */     boolean touchesLiquid = false;
/* 61 */     BlockPos.MutableBlockPos testPos = pos.mutable();
/* 62 */     for (Direction direction : Direction.values()) {
/* 63 */       BlockState blockState = level.getBlockState(testPos);
/* 64 */       if (direction != Direction.DOWN || canSolidify(blockState)) {
/*    */ 
/*    */         
/* 67 */         testPos.setWithOffset(pos, direction);
/* 68 */         blockState = level.getBlockState(testPos);
/* 69 */         if (canSolidify(blockState) && !blockState.isFaceSturdy(level, pos, direction.getOpposite())) {
/* 70 */           touchesLiquid = true; break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 74 */     return touchesLiquid;
/*    */   }
/*    */ 
/*    */   
/* 78 */   private static boolean canSolidify(BlockState state) { return state.getFluidState().is(FluidTags.WATER); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 83 */     if (touchesLiquid(level, pos)) {
/* 84 */       return this.concrete.defaultBlockState();
/*    */     }
/*    */     
/* 87 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 92 */   public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) { return (blockState.getMapColor(level, pos)).col; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ConcretePowderBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */