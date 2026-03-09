/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DoubleBlockCombiner
/*    */ {
/*    */   public enum BlockType
/*    */   {
/* 16 */     SINGLE,
/* 17 */     FIRST,
/* 18 */     SECOND;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <S extends net.minecraft.world.level.block.entity.BlockEntity> NeighborCombineResult<S> combineWithNeigbour(BlockEntityType<S> entityType, Function<BlockState, BlockType> typeResolver, Function<BlockState, Direction> connectionResolver, Property<Direction> facingProperty, BlockState state, LevelAccessor level, BlockPos pos, BiPredicate<LevelAccessor, BlockPos> blockedChecker) {
/* 23 */     S blockEntity = (S)entityType.getBlockEntity(level, pos);
/* 24 */     if (blockEntity == null) {
/* 25 */       return Combiner::acceptNone;
/*    */     }
/*    */     
/* 28 */     if (blockedChecker.test(level, pos)) {
/* 29 */       return Combiner::acceptNone;
/*    */     }
/*    */     
/* 32 */     BlockType type = (BlockType)typeResolver.apply(state);
/*    */     
/* 34 */     boolean single = (type == BlockType.SINGLE);
/* 35 */     boolean isFirst = (type == BlockType.FIRST);
/*    */     
/* 37 */     if (single) {
/* 38 */       return new NeighborCombineResult.Single(blockEntity);
/*    */     }
/*    */     
/* 41 */     BlockPos neighborPos = pos.relative((Direction)connectionResolver.apply(state));
/* 42 */     BlockState neighbourState = level.getBlockState(neighborPos);
/* 43 */     if (neighbourState.is(state.getBlock())) {
/* 44 */       BlockType neighbourType = (BlockType)typeResolver.apply(neighbourState);
/* 45 */       if (neighbourType != BlockType.SINGLE && type != neighbourType && neighbourState.getValue(facingProperty) == state.getValue(facingProperty)) {
/* 46 */         if (blockedChecker.test(level, neighborPos)) {
/* 47 */           return Combiner::acceptNone;
/*    */         }
/*    */         
/* 50 */         S neighbour = (S)entityType.getBlockEntity(level, neighborPos);
/* 51 */         if (neighbour != null) {
/* 52 */           S first = isFirst ? blockEntity : neighbour;
/* 53 */           S second = isFirst ? neighbour : blockEntity;
/* 54 */           return new NeighborCombineResult.Double(first, second);
/*    */         } 
/*    */       } 
/*    */     } 
/* 58 */     return new NeighborCombineResult.Single(blockEntity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class Double<S>
/*    */     extends Object
/*    */     implements NeighborCombineResult<S>
/*    */   {
/*    */     private final S first;
/*    */ 
/*    */ 
/*    */     
/*    */     private final S second;
/*    */ 
/*    */ 
/*    */     
/*    */     public Double(S first, S second) {
/* 77 */       this.first = first;
/* 78 */       this.second = second;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 83 */     public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) { return (T)callback.acceptDouble(this.first, this.second); } } public static interface NeighborCombineResult<S> { <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> param1Combiner); public static final class Double<S> extends Object implements NeighborCombineResult<S> { public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) { return (T)callback.acceptDouble(this.first, this.second); }
/*    */       private final S first; private final S second;
/*    */       public Double(S first, S second) {
/*    */         this.first = first;
/*    */         this.second = second;
/*    */       } }
/*    */     public static final class Single<S> extends Object implements NeighborCombineResult<S> { private final S single;
/*    */       
/* 91 */       public Single(S single) { this.single = single; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 96 */       public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) { return (T)callback.acceptSingle(this.single); } } } public static final class Single<S> extends Object implements NeighborCombineResult<S> { private final S single; public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) { return (T)callback.acceptSingle(this.single); }
/*    */     
/*    */     public Single(S single) { this.single = single; } }
/*    */ 
/*    */   
/*    */   public static interface Combiner<S, T> {
/*    */     T acceptDouble(S param1S1, S param1S2);
/*    */     
/*    */     T acceptSingle(S param1S);
/*    */     
/*    */     T acceptNone();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DoubleBlockCombiner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */