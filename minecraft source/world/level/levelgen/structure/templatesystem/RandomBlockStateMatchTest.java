/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockStateMatchTest extends RuleTest {
/* 10 */   public static final MapCodec<RandomBlockStateMatchTest> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockState.CODEC
/* 11 */         .fieldOf("block_state").forGetter(()), Codec.FLOAT
/* 12 */         .fieldOf("probability").forGetter(()))
/* 13 */       .apply(i, RandomBlockStateMatchTest::new));
/*    */   
/*    */   private final BlockState blockState;
/*    */   private final float probability;
/*    */   
/*    */   public RandomBlockStateMatchTest(BlockState blockState, float probability) {
/* 19 */     this.blockState = blockState;
/* 20 */     this.probability = probability;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public boolean test(BlockState blockState, RandomSource random) { return (blockState == this.blockState && random.nextFloat() < this.probability); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected RuleTestType<?> getType() { return RuleTestType.RANDOM_BLOCKSTATE_TEST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RandomBlockStateMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */