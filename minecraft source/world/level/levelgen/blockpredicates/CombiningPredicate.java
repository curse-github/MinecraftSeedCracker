/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ abstract class CombiningPredicate
/*    */   implements BlockPredicate {
/*    */   protected final List<BlockPredicate> predicates;
/*    */   
/* 13 */   protected CombiningPredicate(List<BlockPredicate> predicates) { this.predicates = predicates; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static <T extends CombiningPredicate> MapCodec<T> codec(Function<List<BlockPredicate>, T> constructor) { return RecordCodecBuilder.mapCodec(i -> i.group(BlockPredicate.CODEC
/* 18 */           .listOf().fieldOf("predicates").forGetter(()))
/* 19 */         .apply(i, constructor)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\CombiningPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */