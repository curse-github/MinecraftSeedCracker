/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ public class BlockPredicateFilter
/*    */   extends PlacementFilter
/*    */ {
/* 14 */   public static final MapCodec<BlockPredicateFilter> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockPredicate.CODEC
/* 15 */         .fieldOf("predicate").forGetter(()))
/* 16 */       .apply(i, BlockPredicateFilter::new));
/*    */   
/*    */   private final BlockPredicate predicate;
/*    */ 
/*    */   
/* 21 */   private BlockPredicateFilter(BlockPredicate predicate) { this.predicate = predicate; }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static BlockPredicateFilter forPredicate(BlockPredicate predicate) { return new BlockPredicateFilter(predicate); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) { return this.predicate.test(context.getLevel(), origin); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PlacementModifierType<?> type() { return PlacementModifierType.BLOCK_PREDICATE_FILTER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\BlockPredicateFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */