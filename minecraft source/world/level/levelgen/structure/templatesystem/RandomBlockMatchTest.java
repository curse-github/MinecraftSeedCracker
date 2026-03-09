/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockMatchTest extends RuleTest {
/* 12 */   public static final MapCodec<RandomBlockMatchTest> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK
/* 13 */         .byNameCodec().fieldOf("block").forGetter(()), Codec.FLOAT
/* 14 */         .fieldOf("probability").forGetter(()))
/* 15 */       .apply(i, RandomBlockMatchTest::new));
/*    */   
/*    */   private final Block block;
/*    */   private final float probability;
/*    */   
/*    */   public RandomBlockMatchTest(Block block, float probability) {
/* 21 */     this.block = block;
/* 22 */     this.probability = probability;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean test(BlockState blockState, RandomSource random) { return (blockState.is(this.block) && random.nextFloat() < this.probability); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected RuleTestType<?> getType() { return RuleTestType.RANDOM_BLOCK_TEST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RandomBlockMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */