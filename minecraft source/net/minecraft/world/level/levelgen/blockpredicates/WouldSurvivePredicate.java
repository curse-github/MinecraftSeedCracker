/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WouldSurvivePredicate implements BlockPredicate {
/* 11 */   public static final MapCodec<WouldSurvivePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 12 */         Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(()), BlockState.CODEC
/* 13 */         .fieldOf("state").forGetter(()))
/* 14 */       .apply(i, WouldSurvivePredicate::new));
/*    */   
/*    */   private final Vec3i offset;
/*    */   private final BlockState state;
/*    */   
/*    */   protected WouldSurvivePredicate(Vec3i offset, BlockState state) {
/* 20 */     this.offset = offset;
/* 21 */     this.state = state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean test(WorldGenLevel level, BlockPos origin) { return this.state.canSurvive(level, origin.offset(this.offset)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public BlockPredicateType<?> type() { return BlockPredicateType.WOULD_SURVIVE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\WouldSurvivePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */