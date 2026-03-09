/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class PosAlwaysTrueTest extends PosRuleTest {
/*  8 */   public static final MapCodec<PosAlwaysTrueTest> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 10 */   public static final PosAlwaysTrueTest INSTANCE = new PosAlwaysTrueTest();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public boolean test(BlockPos inTemplatePos, BlockPos worldPos, BlockPos worldReference, RandomSource random) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   protected PosRuleTestType<?> getType() { return PosRuleTestType.ALWAYS_TRUE_TEST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\PosAlwaysTrueTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */