/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ @Deprecated
/*    */ public class SolidPredicate extends StateTestingPredicate {
/* 10 */   public static final MapCodec<SolidPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).apply(i, SolidPredicate::new));
/*    */ 
/*    */   
/* 13 */   public SolidPredicate(Vec3i offset) { super(offset); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   protected boolean test(BlockState state) { return state.isSolid(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public BlockPredicateType<?> type() { return BlockPredicateType.SOLID; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\SolidPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */