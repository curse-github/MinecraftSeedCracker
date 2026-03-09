/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class MatchingBlocksPredicate extends StateTestingPredicate {
/* 15 */   public static final MapCodec<MatchingBlocksPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).and(
/* 16 */         RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(()))
/* 17 */       .apply(i, MatchingBlocksPredicate::new)); private final HolderSet<Block> blocks;
/*    */   
/*    */   public MatchingBlocksPredicate(Vec3i offset, HolderSet<Block> blocks) {
/* 20 */     super(offset);
/* 21 */     this.blocks = blocks;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected boolean test(BlockState state) { return state.is(this.blocks); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public BlockPredicateType<?> type() { return BlockPredicateType.MATCHING_BLOCKS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingBlocksPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */