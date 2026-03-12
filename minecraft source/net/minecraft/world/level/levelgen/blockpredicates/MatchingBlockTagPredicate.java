/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MatchingBlockTagPredicate extends StateTestingPredicate {
/* 14 */   public static final MapCodec<MatchingBlockTagPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).and(
/* 15 */         TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(()))
/* 16 */       .apply(i, MatchingBlockTagPredicate::new)); final TagKey<Block> tag;
/*    */   
/*    */   protected MatchingBlockTagPredicate(Vec3i offset, TagKey<Block> tag) {
/* 19 */     super(offset);
/* 20 */     this.tag = tag;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected boolean test(BlockState state) { return state.is(this.tag); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public BlockPredicateType<?> type() { return BlockPredicateType.MATCHING_BLOCK_TAG; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingBlockTagPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */