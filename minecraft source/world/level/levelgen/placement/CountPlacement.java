/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class CountPlacement
/*    */   extends RepeatingPlacement
/*    */ {
/* 13 */   public static final MapCodec<CountPlacement> CODEC = IntProvider.codec(0, 256).fieldOf("count")
/* 14 */     .xmap(CountPlacement::new, c -> c.count);
/*    */   
/*    */   private final IntProvider count;
/*    */ 
/*    */   
/* 19 */   private CountPlacement(IntProvider count) { this.count = count; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static CountPlacement of(IntProvider count) { return new CountPlacement(count); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static CountPlacement of(int count) { return of(ConstantInt.of(count)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected int count(RandomSource random, BlockPos origin) { return this.count.sample(random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public PlacementModifierType<?> type() { return PlacementModifierType.COUNT; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\CountPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */