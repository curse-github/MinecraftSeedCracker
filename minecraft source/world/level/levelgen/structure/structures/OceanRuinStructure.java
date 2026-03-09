/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class OceanRuinStructure extends Structure {
/* 18 */   public static final MapCodec<OceanRuinStructure> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 19 */         settingsCodec(i), Type.CODEC
/* 20 */         .fieldOf("biome_temp").forGetter(()), 
/* 21 */         Codec.floatRange(0.0F, 1.0F).fieldOf("large_probability").forGetter(()), 
/* 22 */         Codec.floatRange(0.0F, 1.0F).fieldOf("cluster_probability").forGetter(()))
/* 23 */       .apply(i, OceanRuinStructure::new));
/*    */   
/*    */   public final Type biomeTemp;
/*    */   public final float largeProbability;
/*    */   public final float clusterProbability;
/*    */   
/*    */   public OceanRuinStructure(Structure.StructureSettings settings, Type biomeTemp, float largeProbability, float clusterProbability) {
/* 30 */     super(settings);
/* 31 */     this.biomeTemp = biomeTemp;
/* 32 */     this.largeProbability = largeProbability;
/* 33 */     this.clusterProbability = clusterProbability;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) { return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, builder -> generatePieces(builder, context)); }
/*    */ 
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 42 */     BlockPos offset = new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ());
/* 43 */     Rotation rotation = Rotation.getRandom(context.random());
/* 44 */     OceanRuinPieces.addPieces(context.structureTemplateManager(), offset, rotation, builder, context.random(), this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public StructureType<?> type() { return StructureType.OCEAN_RUIN; }
/*    */   
/*    */   public enum Type
/*    */     implements StringRepresentable {
/* 53 */     WARM("warm"),
/* 54 */     COLD("cold"); public static final Codec<Type> CODEC;
/*    */     
/*    */     static  {
/* 57 */       CODEC = StringRepresentable.fromEnum(Type::values);
/*    */ 
/*    */       
/* 60 */       LEGACY_CODEC = ExtraCodecs.legacyEnum(Type::valueOf);
/*    */     }
/*    */     @Deprecated
/*    */     public static final Codec<Type> LEGACY_CODEC;
/*    */     
/* 65 */     Type(String name) { this.name = name; }
/*    */     
/*    */     private final String name;
/*    */     
/* 69 */     public String getName() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 74 */     public String getSerializedName() { return this.name; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanRuinStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */