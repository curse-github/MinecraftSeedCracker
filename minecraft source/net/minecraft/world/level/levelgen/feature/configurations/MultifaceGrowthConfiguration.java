/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
/*    */ 
/*    */ public class MultifaceGrowthConfiguration implements FeatureConfiguration {
/* 21 */   public static final Codec<MultifaceGrowthConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BuiltInRegistries.BLOCK
/* 22 */         .byNameCodec().fieldOf("block").flatXmap(MultifaceGrowthConfiguration::apply, DataResult::success).orElse((MultifaceSpreadeableBlock)Blocks.GLOW_LICHEN).forGetter(()), 
/* 23 */         Codec.intRange(1, 64).fieldOf("search_range").orElse(Integer.valueOf(10)).forGetter(()), Codec.BOOL
/* 24 */         .fieldOf("can_place_on_floor").orElse(Boolean.valueOf(false)).forGetter(()), Codec.BOOL
/* 25 */         .fieldOf("can_place_on_ceiling").orElse(Boolean.valueOf(false)).forGetter(()), Codec.BOOL
/* 26 */         .fieldOf("can_place_on_wall").orElse(Boolean.valueOf(false)).forGetter(()), 
/* 27 */         Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spreading").orElse(Float.valueOf(0.5F)).forGetter(()), 
/* 28 */         RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_be_placed_on").forGetter(()))
/* 29 */       .apply(i, MultifaceGrowthConfiguration::new)); public final MultifaceSpreadeableBlock placeBlock; public final int searchRange;
/*    */   
/*    */   private static DataResult<MultifaceSpreadeableBlock> apply(Block block) {
/* 32 */     MultifaceSpreadeableBlock multifaceBlock = (MultifaceSpreadeableBlock)block; return (block instanceof MultifaceSpreadeableBlock) ? 
/* 33 */       DataResult.success(multifaceBlock) : 
/* 34 */       DataResult.error(() -> "Growth block should be a multiface spreadeable block");
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean canPlaceOnFloor;
/*    */   
/*    */   public final boolean canPlaceOnCeiling;
/*    */   
/*    */   public final boolean canPlaceOnWall;
/*    */   
/*    */   public final float chanceOfSpreading;
/*    */   public final HolderSet<Block> canBePlacedOn;
/*    */   private final ObjectArrayList<Direction> validDirections;
/*    */   
/*    */   public MultifaceGrowthConfiguration(MultifaceSpreadeableBlock placeBlock, int searchRange, boolean canPlaceOnFloor, boolean canPlaceOnCeiling, boolean canPlaceOnWall, float chanceOfSpreading, HolderSet<Block> canBePlacedOn) {
/* 49 */     this.placeBlock = placeBlock;
/* 50 */     this.searchRange = searchRange;
/* 51 */     this.canPlaceOnFloor = canPlaceOnFloor;
/* 52 */     this.canPlaceOnCeiling = canPlaceOnCeiling;
/* 53 */     this.canPlaceOnWall = canPlaceOnWall;
/* 54 */     this.chanceOfSpreading = chanceOfSpreading;
/* 55 */     this.canBePlacedOn = canBePlacedOn;
/*    */     
/* 57 */     this.validDirections = new ObjectArrayList(6);
/* 58 */     if (canPlaceOnCeiling) {
/* 59 */       this.validDirections.add(Direction.UP);
/*    */     }
/* 61 */     if (canPlaceOnFloor) {
/* 62 */       this.validDirections.add(Direction.DOWN);
/*    */     }
/* 64 */     if (canPlaceOnWall) {
/* 65 */       Objects.requireNonNull(this.validDirections); Direction.Plane.HORIZONTAL.forEach(this.validDirections::add);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 70 */   public List<Direction> getShuffledDirectionsExcept(RandomSource random, Direction excludeDirection) { return Util.toShuffledList(this.validDirections.stream().filter(direction -> (direction != excludeDirection)), random); }
/*    */ 
/*    */ 
/*    */   
/* 74 */   public List<Direction> getShuffledDirections(RandomSource random) { return Util.shuffledCopy(this.validDirections, random); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\MultifaceGrowthConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */