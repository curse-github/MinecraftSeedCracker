/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnvironmentScanPlacement
/*    */   extends PlacementModifier
/*    */ {
/*    */   private final Direction directionOfSearch;
/*    */   private final BlockPredicate targetCondition;
/*    */   private final BlockPredicate allowedSearchCondition;
/*    */   private final int maxSteps;
/* 29 */   public static final MapCodec<EnvironmentScanPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Direction.VERTICAL_CODEC
/* 30 */         .fieldOf("direction_of_search").forGetter(()), BlockPredicate.CODEC
/* 31 */         .fieldOf("target_condition").forGetter(()), BlockPredicate.CODEC
/* 32 */         .optionalFieldOf("allowed_search_condition", BlockPredicate.alwaysTrue()).forGetter(()), 
/* 33 */         Codec.intRange(1, 32).fieldOf("max_steps").forGetter(()))
/* 34 */       .apply(i, EnvironmentScanPlacement::new));
/*    */   
/*    */   private EnvironmentScanPlacement(Direction directionOfSearch, BlockPredicate targetCondition, BlockPredicate allowedSearchCondition, int maxSteps) {
/* 37 */     this.directionOfSearch = directionOfSearch;
/* 38 */     this.targetCondition = targetCondition;
/* 39 */     this.allowedSearchCondition = allowedSearchCondition;
/* 40 */     this.maxSteps = maxSteps;
/*    */   }
/*    */ 
/*    */   
/* 44 */   public static EnvironmentScanPlacement scanningFor(Direction directionOfSearch, BlockPredicate targetCondition, BlockPredicate allowedSearchCondition, int maxSteps) { return new EnvironmentScanPlacement(directionOfSearch, targetCondition, allowedSearchCondition, maxSteps); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public static EnvironmentScanPlacement scanningFor(Direction directionOfSearch, BlockPredicate targetCondition, int maxSteps) { return scanningFor(directionOfSearch, targetCondition, BlockPredicate.alwaysTrue(), maxSteps); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 53 */     BlockPos.MutableBlockPos pos = origin.mutable();
/* 54 */     WorldGenLevel level = context.getLevel();
/* 55 */     if (!this.allowedSearchCondition.test(level, pos)) {
/* 56 */       return Stream.of(new BlockPos[0]);
/*    */     }
/*    */     
/* 59 */     for (int i = 0; i < this.maxSteps; i++) {
/* 60 */       if (this.targetCondition.test(level, pos)) {
/* 61 */         return Stream.of(pos);
/*    */       }
/* 63 */       pos.move(this.directionOfSearch);
/* 64 */       if (level.isOutsideBuildHeight(pos.getY())) {
/* 65 */         return Stream.of(new BlockPos[0]);
/*    */       }
/* 67 */       if (!this.allowedSearchCondition.test(level, pos)) {
/*    */         break;
/*    */       }
/*    */     } 
/* 71 */     if (this.targetCondition.test(level, pos)) {
/* 72 */       return Stream.of(pos);
/*    */     }
/* 74 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public PlacementModifierType<?> type() { return PlacementModifierType.ENVIRONMENT_SCAN; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\EnvironmentScanPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */