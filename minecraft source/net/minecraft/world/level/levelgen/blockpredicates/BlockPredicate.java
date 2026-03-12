/*     */ package net.minecraft.world.level.levelgen.blockpredicates;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.BiPredicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public interface BlockPredicate
/*     */   extends BiPredicate<WorldGenLevel, BlockPos> {
/*  21 */   public static final Codec<BlockPredicate> CODEC = BuiltInRegistries.BLOCK_PREDICATE_TYPE.byNameCodec().dispatch(BlockPredicate::type, BlockPredicateType::codec);
/*     */ 
/*     */ 
/*     */   
/*  25 */   public static final BlockPredicate ONLY_IN_AIR_PREDICATE = matchesBlocks(new Block[] { Blocks.AIR });
/*  26 */   public static final BlockPredicate ONLY_IN_AIR_OR_WATER_PREDICATE = matchesBlocks(new Block[] { Blocks.AIR, Blocks.WATER });
/*     */ 
/*     */   
/*     */   BlockPredicateType<?> type();
/*     */   
/*  31 */   static BlockPredicate allOf(List<BlockPredicate> predicates) { return new AllOfPredicate(predicates); }
/*     */ 
/*     */ 
/*     */   
/*  35 */   static BlockPredicate allOf(BlockPredicate... predicates) { return allOf(List.of(predicates)); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   static BlockPredicate allOf(BlockPredicate a, BlockPredicate b) { return allOf(List.of(a, b)); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   static BlockPredicate anyOf(List<BlockPredicate> predicates) { return new AnyOfPredicate(predicates); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   static BlockPredicate anyOf(BlockPredicate... predicates) { return anyOf(List.of(predicates)); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   static BlockPredicate anyOf(BlockPredicate a, BlockPredicate b) { return anyOf(List.of(a, b)); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   static BlockPredicate matchesBlocks(Vec3i offset, List<Block> blocks) { return new MatchingBlocksPredicate(offset, HolderSet.direct(Block::builtInRegistryHolder, blocks)); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   static BlockPredicate matchesBlocks(List<Block> blocks) { return matchesBlocks(Vec3i.ZERO, blocks); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   static BlockPredicate matchesBlocks(Vec3i offset, Block... blocks) { return matchesBlocks(offset, List.of(blocks)); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   static BlockPredicate matchesBlocks(Block... blocks) { return matchesBlocks(Vec3i.ZERO, blocks); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   static BlockPredicate matchesTag(Vec3i offset, TagKey<Block> tag) { return new MatchingBlockTagPredicate(offset, tag); }
/*     */ 
/*     */ 
/*     */   
/*  75 */   static BlockPredicate matchesTag(TagKey<Block> tag) { return matchesTag(Vec3i.ZERO, tag); }
/*     */ 
/*     */ 
/*     */   
/*  79 */   static BlockPredicate matchesFluids(Vec3i offset, List<Fluid> fluids) { return new MatchingFluidsPredicate(offset, HolderSet.direct(Fluid::builtInRegistryHolder, fluids)); }
/*     */ 
/*     */ 
/*     */   
/*  83 */   static BlockPredicate matchesFluids(Vec3i offset, Fluid... fluids) { return matchesFluids(offset, List.of(fluids)); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   static BlockPredicate matchesFluids(Fluid... fluids) { return matchesFluids(Vec3i.ZERO, fluids); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   static BlockPredicate not(BlockPredicate predicate) { return new NotPredicate(predicate); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   static BlockPredicate replaceable(Vec3i offset) { return new ReplaceablePredicate(offset); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   static BlockPredicate replaceable() { return replaceable(Vec3i.ZERO); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   static BlockPredicate wouldSurvive(BlockState state, Vec3i offset) { return new WouldSurvivePredicate(offset, state); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   static BlockPredicate hasSturdyFace(Vec3i offset, Direction direction) { return new HasSturdyFacePredicate(offset, direction); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   static BlockPredicate hasSturdyFace(Direction direction) { return hasSturdyFace(Vec3i.ZERO, direction); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   static BlockPredicate solid(Vec3i offset) { return new SolidPredicate(offset); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   static BlockPredicate solid() { return solid(Vec3i.ZERO); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   static BlockPredicate noFluid() { return noFluid(Vec3i.ZERO); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   static BlockPredicate noFluid(Vec3i offset) { return matchesFluids(offset, new Fluid[] { Fluids.EMPTY }); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   static BlockPredicate insideWorld(Vec3i offset) { return new InsideWorldBoundsPredicate(offset); }
/*     */ 
/*     */ 
/*     */   
/* 135 */   static BlockPredicate alwaysTrue() { return TrueBlockPredicate.INSTANCE; }
/*     */ 
/*     */ 
/*     */   
/* 139 */   static BlockPredicate unobstructed(Vec3i offset) { return new UnobstructedPredicate(offset); }
/*     */ 
/*     */ 
/*     */   
/* 143 */   static BlockPredicate unobstructed() { return unobstructed(Vec3i.ZERO); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\BlockPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */