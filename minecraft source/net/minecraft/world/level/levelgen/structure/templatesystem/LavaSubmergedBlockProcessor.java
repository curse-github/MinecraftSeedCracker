/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LavaSubmergedBlockProcessor
/*    */   extends StructureProcessor
/*    */ {
/* 15 */   public static final MapCodec<LavaSubmergedBlockProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
/* 16 */   public static final LavaSubmergedBlockProcessor INSTANCE = new LavaSubmergedBlockProcessor();
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 20 */     BlockPos pos = processedBlockInfo.pos();
/* 21 */     boolean wasLavaBefore = level.getBlockState(pos).is(Blocks.LAVA);
/* 22 */     if (wasLavaBefore && !Block.isShapeFullBlock(processedBlockInfo.state().getShape(level, pos))) {
/* 23 */       return new StructureTemplate.StructureBlockInfo(pos, Blocks.LAVA.defaultBlockState(), processedBlockInfo.nbt());
/*    */     }
/* 25 */     return processedBlockInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected StructureProcessorType<?> getType() { return StructureProcessorType.LAVA_SUBMERGED_BLOCK; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LavaSubmergedBlockProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */