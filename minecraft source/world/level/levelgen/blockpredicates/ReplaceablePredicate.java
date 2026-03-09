/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class ReplaceablePredicate extends StateTestingPredicate {
/*  9 */   public static final MapCodec<ReplaceablePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).apply(i, ReplaceablePredicate::new));
/*    */ 
/*    */   
/* 12 */   public ReplaceablePredicate(Vec3i offset) { super(offset); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   protected boolean test(BlockState state) { return state.canBeReplaced(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public BlockPredicateType<?> type() { return BlockPredicateType.REPLACEABLE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\ReplaceablePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */