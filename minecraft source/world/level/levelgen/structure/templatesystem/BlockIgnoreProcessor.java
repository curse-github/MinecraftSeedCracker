/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockIgnoreProcessor
/*    */   extends StructureProcessor {
/* 16 */   public static final MapCodec<BlockIgnoreProcessor> CODEC = BlockState.CODEC
/* 17 */     .xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState)
/* 18 */     .listOf()
/* 19 */     .fieldOf("blocks")
/* 20 */     .xmap(BlockIgnoreProcessor::new, p -> p.toIgnore);
/*    */   
/* 22 */   public static final BlockIgnoreProcessor STRUCTURE_BLOCK = new BlockIgnoreProcessor(ImmutableList.of(Blocks.STRUCTURE_BLOCK));
/* 23 */   public static final BlockIgnoreProcessor AIR = new BlockIgnoreProcessor(ImmutableList.of(Blocks.AIR));
/* 24 */   public static final BlockIgnoreProcessor STRUCTURE_AND_AIR = new BlockIgnoreProcessor(ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK));
/*    */   
/*    */   private final ImmutableList<Block> toIgnore;
/*    */ 
/*    */   
/* 29 */   public BlockIgnoreProcessor(List<Block> toIgnore) { this.toIgnore = ImmutableList.copyOf(toIgnore); }
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 34 */     if (this.toIgnore.contains(processedBlockInfo.state().getBlock())) {
/* 35 */       return null;
/*    */     }
/* 37 */     return processedBlockInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   protected StructureProcessorType<?> getType() { return StructureProcessorType.BLOCK_IGNORE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockIgnoreProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */