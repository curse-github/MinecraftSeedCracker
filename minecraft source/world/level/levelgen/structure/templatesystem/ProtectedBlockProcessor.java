/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProtectedBlockProcessor
/*    */   extends StructureProcessor
/*    */ {
/*    */   public final TagKey<Block> cannotReplace;
/* 20 */   public static final MapCodec<ProtectedBlockProcessor> CODEC = TagKey.hashedCodec(Registries.BLOCK).xmap(ProtectedBlockProcessor::new, e -> e.cannotReplace).fieldOf("value");
/*    */ 
/*    */   
/* 23 */   public ProtectedBlockProcessor(TagKey<Block> cannotReplace) { this.cannotReplace = cannotReplace; }
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 28 */     if (Feature.isReplaceable(this.cannotReplace).test(level.getBlockState(processedBlockInfo.pos()))) {
/* 29 */       return processedBlockInfo;
/*    */     }
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   protected StructureProcessorType<?> getType() { return StructureProcessorType.PROTECTED_BLOCKS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\ProtectedBlockProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */