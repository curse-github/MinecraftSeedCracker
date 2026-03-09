/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class AlwaysTrueTest extends RuleTest {
/*  8 */   public static final MapCodec<AlwaysTrueTest> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 10 */   public static final AlwaysTrueTest INSTANCE = new AlwaysTrueTest();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public boolean test(BlockState blockState, RandomSource random) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   protected RuleTestType<?> getType() { return RuleTestType.ALWAYS_TRUE_TEST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\AlwaysTrueTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */