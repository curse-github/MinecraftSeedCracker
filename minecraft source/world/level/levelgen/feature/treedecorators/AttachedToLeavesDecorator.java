/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class AttachedToLeavesDecorator extends TreeDecorator {
/* 18 */   public static final MapCodec<AttachedToLeavesDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 19 */         Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()), 
/* 20 */         Codec.intRange(0, 16).fieldOf("exclusion_radius_xz").forGetter(()), 
/* 21 */         Codec.intRange(0, 16).fieldOf("exclusion_radius_y").forGetter(()), BlockStateProvider.CODEC
/* 22 */         .fieldOf("block_provider").forGetter(()), 
/* 23 */         Codec.intRange(1, 16).fieldOf("required_empty_blocks").forGetter(()), 
/* 24 */         ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter(()))
/* 25 */       .apply(i, AttachedToLeavesDecorator::new));
/*    */   
/*    */   protected final float probability;
/*    */   protected final int exclusionRadiusXZ;
/*    */   protected final int exclusionRadiusY;
/*    */   protected final BlockStateProvider blockProvider;
/*    */   protected final int requiredEmptyBlocks;
/*    */   protected final List<Direction> directions;
/*    */   
/*    */   public AttachedToLeavesDecorator(float probability, int exclusionRadiusXZ, int exclusionRadiusY, BlockStateProvider blockProvider, int requiredEmptyBlocks, List<Direction> directions) {
/* 35 */     this.probability = probability;
/* 36 */     this.exclusionRadiusXZ = exclusionRadiusXZ;
/* 37 */     this.exclusionRadiusY = exclusionRadiusY;
/* 38 */     this.blockProvider = blockProvider;
/* 39 */     this.requiredEmptyBlocks = requiredEmptyBlocks;
/* 40 */     this.directions = directions;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 45 */     Set<BlockPos> propaguleBlacklist = new HashSet<BlockPos>();
/*    */     
/* 47 */     RandomSource random = context.random();
/* 48 */     for (BlockPos leafPos : Util.shuffledCopy(context.leaves(), random)) {
/* 49 */       Direction direction = (Direction)Util.getRandom(this.directions, random);
/* 50 */       BlockPos placementPos = leafPos.relative(direction);
/* 51 */       if (propaguleBlacklist.contains(placementPos)) {
/*    */         continue;
/*    */       }
/* 54 */       if (random.nextFloat() < this.probability && 
/* 55 */         hasRequiredEmptyBlocks(context, leafPos, direction)) {
/*    */         
/* 57 */         BlockPos corner1 = placementPos.offset(-this.exclusionRadiusXZ, -this.exclusionRadiusY, -this.exclusionRadiusXZ);
/* 58 */         BlockPos corner2 = placementPos.offset(this.exclusionRadiusXZ, this.exclusionRadiusY, this.exclusionRadiusXZ);
/* 59 */         for (BlockPos inPos : BlockPos.betweenClosed(corner1, corner2)) {
/* 60 */           propaguleBlacklist.add(inPos.immutable());
/*    */         }
/*    */         
/* 63 */         context.setBlock(placementPos, this.blockProvider.getState(random, placementPos));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean hasRequiredEmptyBlocks(TreeDecorator.Context context, BlockPos leafPos, Direction direction) {
/* 70 */     for (int i = 1; i <= this.requiredEmptyBlocks; i++) {
/* 71 */       BlockPos offsetPos = leafPos.relative(direction, i);
/* 72 */       if (!context.isAir(offsetPos)) {
/* 73 */         return false;
/*    */       }
/*    */     } 
/* 76 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 81 */   protected TreeDecoratorType<?> type() { return TreeDecoratorType.ATTACHED_TO_LEAVES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\AttachedToLeavesDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */