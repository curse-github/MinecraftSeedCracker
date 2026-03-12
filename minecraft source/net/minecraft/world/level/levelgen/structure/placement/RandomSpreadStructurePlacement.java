/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RandomSpreadStructurePlacement
/*    */   extends StructurePlacement
/*    */ {
/* 23 */   public static final MapCodec<RandomSpreadStructurePlacement> CODEC = RecordCodecBuilder.mapCodec(i -> placementCodec(i).and(i.group(
/* 24 */           Codec.intRange(0, 4096).fieldOf("spacing").forGetter(RandomSpreadStructurePlacement::spacing), 
/* 25 */           Codec.intRange(0, 4096).fieldOf("separation").forGetter(RandomSpreadStructurePlacement::separation), RandomSpreadType.CODEC
/* 26 */           .optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(RandomSpreadStructurePlacement::spreadType)))
/* 27 */       .apply(i, RandomSpreadStructurePlacement::new)).validate(RandomSpreadStructurePlacement::validate);
/*    */   
/*    */   private static DataResult<RandomSpreadStructurePlacement> validate(RandomSpreadStructurePlacement c) {
/* 30 */     if (c.spacing <= c.separation) {
/* 31 */       return DataResult.error(() -> "Spacing has to be larger than separation");
/*    */     }
/* 33 */     return DataResult.success(c);
/*    */   }
/*    */   
/*    */   private final int spacing;
/*    */   private final int separation;
/*    */   private final RandomSpreadType spreadType;
/*    */   
/*    */   public RandomSpreadStructurePlacement(Vec3i locateOffset, StructurePlacement.FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<StructurePlacement.ExclusionZone> exclusionZone, int spacing, int separation, RandomSpreadType spreadType) {
/* 41 */     super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone);
/* 42 */     this.spacing = spacing;
/* 43 */     this.separation = separation;
/* 44 */     this.spreadType = spreadType;
/*    */   }
/*    */ 
/*    */   
/* 48 */   public RandomSpreadStructurePlacement(int spacing, int separation, RandomSpreadType spreadType, int salt) { this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, salt, Optional.empty(), spacing, separation, spreadType); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int spacing() { return this.spacing; }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public int separation() { return this.separation; }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public RandomSpreadType spreadType() { return this.spreadType; }
/*    */ 
/*    */   
/*    */   public ChunkPos getPotentialStructureChunk(long seed, int sourceX, int sourceZ) {
/* 64 */     int spacedGridX = Math.floorDiv(sourceX, this.spacing);
/* 65 */     int spacedGridZ = Math.floorDiv(sourceZ, this.spacing);
/*    */     
/* 67 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
/* 68 */     random.setLargeFeatureWithSalt(seed, spacedGridX, spacedGridZ, salt());
/*    */     
/* 70 */     int limit = this.spacing - this.separation;
/* 71 */     int spreadX = this.spreadType.evaluate(random, limit);
/* 72 */     int spreadZ = this.spreadType.evaluate(random, limit);
/*    */     
/* 74 */     return new ChunkPos(spacedGridX * this.spacing + spreadX, spacedGridZ * this.spacing + spreadZ);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
/* 82 */     ChunkPos chunkPos = getPotentialStructureChunk(state.getLevelSeed(), sourceX, sourceZ);
/* 83 */     return (chunkPos.x == sourceX && chunkPos.z == sourceZ);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 88 */   public StructurePlacementType<?> type() { return StructurePlacementType.RANDOM_SPREAD; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\placement\RandomSpreadStructurePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */