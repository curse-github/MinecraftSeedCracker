/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TagMatchTest extends RuleTest {
/* 11 */   public static final MapCodec<TagMatchTest> CODEC = TagKey.codec(Registries.BLOCK).fieldOf("tag").xmap(TagMatchTest::new, t -> t.tag);
/*    */   
/*    */   private final TagKey<Block> tag;
/*    */ 
/*    */   
/* 16 */   public TagMatchTest(TagKey<Block> tag) { this.tag = tag; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public boolean test(BlockState blockState, RandomSource random) { return blockState.is(this.tag); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected RuleTestType<?> getType() { return RuleTestType.TAG_TEST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\TagMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */