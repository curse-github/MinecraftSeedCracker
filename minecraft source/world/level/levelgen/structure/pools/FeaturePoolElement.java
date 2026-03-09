/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.FrontAndTop;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.JigsawBlock;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public class FeaturePoolElement extends StructurePoolElement {
/* 30 */   public static final MapCodec<FeaturePoolElement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PlacedFeature.CODEC
/* 31 */         .fieldOf("feature").forGetter(()), 
/* 32 */         projectionCodec())
/* 33 */       .apply(i, FeaturePoolElement::new));
/*    */   
/* 35 */   private static final Identifier DEFAULT_JIGSAW_NAME = Identifier.withDefaultNamespace("bottom");
/*    */   
/*    */   private final Holder<PlacedFeature> feature;
/*    */   private final CompoundTag defaultJigsawNBT;
/*    */   
/*    */   protected FeaturePoolElement(Holder<PlacedFeature> feature, StructureTemplatePool.Projection projection) {
/* 41 */     super(projection);
/* 42 */     this.feature = feature;
/* 43 */     this.defaultJigsawNBT = fillDefaultJigsawNBT();
/*    */   }
/*    */   
/*    */   private CompoundTag fillDefaultJigsawNBT() {
/* 47 */     CompoundTag tag = new CompoundTag();
/* 48 */     tag.store("name", Identifier.CODEC, DEFAULT_JIGSAW_NAME);
/* 49 */     tag.putString("final_state", "minecraft:air");
/*    */ 
/*    */     
/* 52 */     tag.store("pool", JigsawBlockEntity.POOL_CODEC, Pools.EMPTY);
/* 53 */     tag.store("target", Identifier.CODEC, JigsawBlockEntity.EMPTY_ID);
/* 54 */     tag.store("joint", JigsawBlockEntity.JointType.CODEC, JigsawBlockEntity.JointType.ROLLABLE);
/*    */     
/* 56 */     return tag;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) { return Vec3i.ZERO; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, RandomSource random) { return List.of(StructureTemplate.JigsawBlockInfo.of(new StructureTemplate.StructureBlockInfo(position, (BlockState)Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH)), this.defaultJigsawNBT))); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation) {
/* 71 */     Vec3i size = getSize(structureTemplateManager, rotation);
/* 72 */     return new BoundingBox(position.getX(), position.getY(), position.getZ(), position.getX() + size.getX(), position.getY() + size.getY(), position.getZ() + size.getZ());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos position, BlockPos referencePos, Rotation rotation, BoundingBox chunkBB, RandomSource random, LiquidSettings liquidSettings, boolean keepJigsaws) { return ((PlacedFeature)this.feature.value()).place(level, generator, random, position); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   public StructurePoolElementType<?> getType() { return StructurePoolElementType.FEATURE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 87 */   public String toString() { return "Feature[" + String.valueOf(this.feature) + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\FeaturePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */