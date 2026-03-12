/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ class AnyOfPredicate extends CombiningPredicate {
/* 10 */   public static final MapCodec<AnyOfPredicate> CODEC = codec(AnyOfPredicate::new);
/*    */ 
/*    */   
/* 13 */   public AnyOfPredicate(List<BlockPredicate> predicates) { super(predicates); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 18 */     for (BlockPredicate predicate : this.predicates) {
/* 19 */       if (predicate.test(level, origin)) {
/* 20 */         return true;
/*    */       }
/*    */     } 
/* 23 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public BlockPredicateType<?> type() { return BlockPredicateType.ANY_OF; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\AnyOfPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */