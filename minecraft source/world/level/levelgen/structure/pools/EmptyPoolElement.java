/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public class EmptyPoolElement
/*    */   extends StructurePoolElement {
/* 20 */   public static final MapCodec<EmptyPoolElement> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 22 */   public static final EmptyPoolElement INSTANCE = new EmptyPoolElement();
/*    */ 
/*    */   
/* 25 */   private EmptyPoolElement() { super(StructureTemplatePool.Projection.TERRAIN_MATCHING); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) { return Vec3i.ZERO; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, RandomSource random) { return Collections.emptyList(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation) { throw new IllegalStateException("Invalid call to EmptyPoolElement.getBoundingBox, filter me!"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos position, BlockPos referencePos, Rotation rotation, BoundingBox chunkBB, RandomSource random, LiquidSettings liquidSettings, boolean keepJigsaws) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public StructurePoolElementType<?> getType() { return StructurePoolElementType.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public String toString() { return "Empty"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\EmptyPoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */