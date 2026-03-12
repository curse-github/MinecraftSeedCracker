/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SlabBlock;
/*    */ import net.minecraft.world.level.block.StairBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Half;
/*    */ import net.minecraft.world.level.block.state.properties.SlabType;
/*    */ 
/*    */ public class BlackstoneReplaceProcessor extends StructureProcessor {
/* 20 */   public static final MapCodec<BlackstoneReplaceProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 22 */   public static final BlackstoneReplaceProcessor INSTANCE = new BlackstoneReplaceProcessor();
/*    */   
/*    */   private final Map<Block, Block> replacements;
/*    */   
/*    */   private BlackstoneReplaceProcessor() {
/* 27 */     this.replacements = (Map)Util.make(Maps.newHashMap(), map -> {
/* 28 */           map.put(Blocks.COBBLESTONE, Blocks.BLACKSTONE);
/* 29 */           map.put(Blocks.MOSSY_COBBLESTONE, Blocks.BLACKSTONE);
/*    */           
/* 31 */           map.put(Blocks.STONE, Blocks.POLISHED_BLACKSTONE);
/*    */           
/* 33 */           map.put(Blocks.STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/* 34 */           map.put(Blocks.MOSSY_STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*    */           
/* 36 */           map.put(Blocks.COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/* 37 */           map.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/*    */           
/* 39 */           map.put(Blocks.STONE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
/*    */           
/* 41 */           map.put(Blocks.STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/* 42 */           map.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*    */           
/* 44 */           map.put(Blocks.COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/* 45 */           map.put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/*    */           
/* 47 */           map.put(Blocks.SMOOTH_STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/* 48 */           map.put(Blocks.STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/*    */           
/* 50 */           map.put(Blocks.STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/* 51 */           map.put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*    */           
/* 53 */           map.put(Blocks.STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/* 54 */           map.put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*    */           
/* 56 */           map.put(Blocks.COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/* 57 */           map.put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/*    */           
/* 59 */           map.put(Blocks.CHISELED_STONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE);
/* 60 */           map.put(Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*    */           
/* 62 */           map.put(Blocks.IRON_BARS, Blocks.IRON_CHAIN);
/*    */         });
/*    */   }
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 67 */     Block newBlock = (Block)this.replacements.get(processedBlockInfo.state().getBlock());
/* 68 */     if (newBlock == null) {
/* 69 */       return processedBlockInfo;
/*    */     }
/* 71 */     BlockState oldState = processedBlockInfo.state();
/* 72 */     BlockState newState = newBlock.defaultBlockState();
/* 73 */     if (oldState.hasProperty(StairBlock.FACING)) {
/* 74 */       newState = (BlockState)newState.setValue(StairBlock.FACING, (Direction)oldState.getValue(StairBlock.FACING));
/*    */     }
/* 76 */     if (oldState.hasProperty(StairBlock.HALF)) {
/* 77 */       newState = (BlockState)newState.setValue(StairBlock.HALF, (Half)oldState.getValue(StairBlock.HALF));
/*    */     }
/* 79 */     if (oldState.hasProperty(SlabBlock.TYPE)) {
/* 80 */       newState = (BlockState)newState.setValue(SlabBlock.TYPE, (SlabType)oldState.getValue(SlabBlock.TYPE));
/*    */     }
/* 82 */     return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), newState, processedBlockInfo.nbt());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 87 */   protected StructureProcessorType<?> getType() { return StructureProcessorType.BLACKSTONE_REPLACE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlackstoneReplaceProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */