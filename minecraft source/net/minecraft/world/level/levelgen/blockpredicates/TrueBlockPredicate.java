/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ class TrueBlockPredicate implements BlockPredicate {
/*  8 */   public static TrueBlockPredicate INSTANCE = new TrueBlockPredicate();
/*  9 */   public static final MapCodec<TrueBlockPredicate> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public boolean test(WorldGenLevel level, BlockPos origin) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public BlockPredicateType<?> type() { return BlockPredicateType.TRUE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\TrueBlockPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */