/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockMatchTest extends RuleTest {
/* 10 */   public static final MapCodec<BlockMatchTest> CODEC = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").xmap(BlockMatchTest::new, t -> t.block);
/*    */   
/*    */   private final Block block;
/*    */ 
/*    */   
/* 15 */   public BlockMatchTest(Block block) { this.block = block; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public boolean test(BlockState blockState, RandomSource random) { return blockState.is(this.block); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected RuleTestType<?> getType() { return RuleTestType.BLOCK_TEST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */