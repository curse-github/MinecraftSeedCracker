/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class PlaceOnGroundDecorator extends TreeDecorator {
/* 19 */   public static final MapCodec<PlaceOnGroundDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.POSITIVE_INT
/* 20 */         .fieldOf("tries").orElse(Integer.valueOf(128)).forGetter(()), ExtraCodecs.NON_NEGATIVE_INT
/* 21 */         .fieldOf("radius").orElse(Integer.valueOf(2)).forGetter(()), ExtraCodecs.NON_NEGATIVE_INT
/* 22 */         .fieldOf("height").orElse(Integer.valueOf(1)).forGetter(()), BlockStateProvider.CODEC
/* 23 */         .fieldOf("block_state_provider").forGetter(()))
/* 24 */       .apply(i, PlaceOnGroundDecorator::new));
/*    */   
/*    */   private final int tries;
/*    */   private final int radius;
/*    */   private final int height;
/*    */   private final BlockStateProvider blockStateProvider;
/*    */   
/*    */   public PlaceOnGroundDecorator(int tries, int radius, int height, BlockStateProvider blockStateProvider) {
/* 32 */     this.tries = tries;
/* 33 */     this.radius = radius;
/* 34 */     this.height = height;
/* 35 */     this.blockStateProvider = blockStateProvider;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected TreeDecoratorType<?> type() { return TreeDecoratorType.PLACE_ON_GROUND; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 45 */     List<BlockPos> blockPositions = TreeFeature.getLowestTrunkOrRootOfTree(context);
/*    */     
/* 47 */     if (blockPositions.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     BlockPos origin = (BlockPos)blockPositions.getFirst();
/* 52 */     int minY = origin.getY();
/* 53 */     int minX = origin.getX();
/* 54 */     int maxX = origin.getX();
/* 55 */     int minZ = origin.getZ();
/* 56 */     int maxZ = origin.getZ();
/* 57 */     for (BlockPos position : blockPositions) {
/* 58 */       if (position.getY() == minY) {
/* 59 */         minX = Math.min(minX, position.getX());
/* 60 */         maxX = Math.max(maxX, position.getX());
/* 61 */         minZ = Math.min(minZ, position.getZ());
/* 62 */         maxZ = Math.max(maxZ, position.getZ());
/*    */       } 
/*    */     } 
/*    */     
/* 66 */     RandomSource random = context.random();
/* 67 */     BoundingBox bb = (new BoundingBox(minX, minY, minZ, maxX, minY, maxZ)).inflatedBy(this.radius, this.height, this.radius);
/* 68 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*    */     
/* 70 */     for (int i = 0; i < this.tries; i++) {
/* 71 */       pos.set(random.nextIntBetweenInclusive(bb.minX(), bb.maxX()), random.nextIntBetweenInclusive(bb.minY(), bb.maxY()), random.nextIntBetweenInclusive(bb.minZ(), bb.maxZ()));
/* 72 */       attemptToPlaceBlockAbove(context, pos);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void attemptToPlaceBlockAbove(TreeDecorator.Context context, BlockPos pos) {
/* 77 */     BlockPos abovePos = pos.above();
/* 78 */     if (context.level().isStateAtPosition(abovePos, state -> (state.isAir() || state.is(Blocks.VINE))) && context
/* 79 */       .checkBlock(pos, BlockBehaviour.BlockStateBase::isSolidRender) && context
/* 80 */       .level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY() <= abovePos.getY())
/* 81 */       context.setBlock(abovePos, this.blockStateProvider.getState(context.random(), abovePos)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\PlaceOnGroundDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */