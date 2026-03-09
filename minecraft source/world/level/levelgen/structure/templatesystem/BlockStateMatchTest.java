/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockStateMatchTest extends RuleTest {
/*  8 */   public static final MapCodec<BlockStateMatchTest> CODEC = BlockState.CODEC.fieldOf("block_state").xmap(BlockStateMatchTest::new, t -> t.blockState);
/*    */   
/*    */   private final BlockState blockState;
/*    */ 
/*    */   
/* 13 */   public BlockStateMatchTest(BlockState blockState) { this.blockState = blockState; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public boolean test(BlockState blockState, RandomSource random) { return (blockState == this.blockState); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected RuleTestType<?> getType() { return RuleTestType.BLOCKSTATE_TEST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockStateMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */