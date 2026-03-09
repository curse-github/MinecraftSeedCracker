/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ class NotPredicate implements BlockPredicate {
/*  9 */   public static final MapCodec<NotPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockPredicate.CODEC
/* 10 */         .fieldOf("predicate").forGetter(()))
/* 11 */       .apply(i, NotPredicate::new));
/*    */   
/*    */   private final BlockPredicate predicate;
/*    */ 
/*    */   
/* 16 */   public NotPredicate(BlockPredicate predicate) { this.predicate = predicate; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public boolean test(WorldGenLevel level, BlockPos origin) { return !this.predicate.test(level, origin); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public BlockPredicateType<?> type() { return BlockPredicateType.NOT; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\NotPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */