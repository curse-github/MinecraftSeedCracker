/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ class AllOfPredicate extends CombiningPredicate {
/* 10 */   public static final MapCodec<AllOfPredicate> CODEC = codec(AllOfPredicate::new);
/*    */ 
/*    */   
/* 13 */   public AllOfPredicate(List<BlockPredicate> predicates) { super(predicates); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 18 */     for (BlockPredicate predicate : this.predicates) {
/* 19 */       if (!predicate.test(level, origin)) {
/* 20 */         return false;
/*    */       }
/*    */     } 
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public BlockPredicateType<?> type() { return BlockPredicateType.ALL_OF; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\AllOfPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */