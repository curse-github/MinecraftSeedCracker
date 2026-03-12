/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ 
/*    */ public class CappedProcessor extends StructureProcessor {
/* 17 */   public static final MapCodec<CappedProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(StructureProcessorType.SINGLE_CODEC
/* 18 */         .fieldOf("delegate").forGetter(()), IntProvider.POSITIVE_CODEC
/* 19 */         .fieldOf("limit").forGetter(()))
/* 20 */       .apply(i, CappedProcessor::new));
/*    */   
/*    */   private final StructureProcessor delegate;
/*    */   private final IntProvider limit;
/*    */   
/*    */   public CappedProcessor(StructureProcessor delegate, IntProvider limit) {
/* 26 */     this.delegate = delegate;
/* 27 */     this.limit = limit;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected StructureProcessorType<?> getType() { return StructureProcessorType.CAPPED; }
/*    */ 
/*    */ 
/*    */   
/*    */   public final List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor level, BlockPos position, BlockPos referencePos, List<StructureTemplate.StructureBlockInfo> originalBlockInfoList, List<StructureTemplate.StructureBlockInfo> processedBlockInfoList, StructurePlaceSettings settings) {
/* 37 */     if (this.limit.getMaxValue() == 0 || processedBlockInfoList.isEmpty()) {
/* 38 */       return processedBlockInfoList;
/*    */     }
/*    */     
/* 41 */     if (originalBlockInfoList.size() != processedBlockInfoList.size()) {
/* 42 */       Util.logAndPauseIfInIde("Original block info list not in sync with processed list, skipping processing. Original size: " + originalBlockInfoList.size() + ", Processed size: " + processedBlockInfoList.size());
/* 43 */       return processedBlockInfoList;
/*    */     } 
/*    */     
/* 46 */     RandomSource random = RandomSource.create(level.getLevel().getSeed()).forkPositional().at(position);
/*    */     
/* 48 */     int maxToReplace = Math.min(this.limit.sample(random), processedBlockInfoList.size());
/*    */     
/* 50 */     if (maxToReplace < 1) {
/* 51 */       return processedBlockInfoList;
/*    */     }
/*    */     
/* 54 */     IntArrayList indices = Util.toShuffledList(IntStream.range(0, processedBlockInfoList.size()), random);
/*    */     
/* 56 */     IntIterator indexIterator = indices.intIterator();
/* 57 */     int replaced = 0;
/*    */     
/* 59 */     while (indexIterator.hasNext() && replaced < maxToReplace) {
/* 60 */       int index = indexIterator.nextInt();
/* 61 */       StructureTemplate.StructureBlockInfo originalBlockInfo = (StructureTemplate.StructureBlockInfo)originalBlockInfoList.get(index);
/* 62 */       StructureTemplate.StructureBlockInfo processedBlockInfo = (StructureTemplate.StructureBlockInfo)processedBlockInfoList.get(index);
/*    */       
/* 64 */       StructureTemplate.StructureBlockInfo maybeAltered = this.delegate.processBlock(level, position, referencePos, originalBlockInfo, processedBlockInfo, settings);
/*    */       
/* 66 */       if (maybeAltered != null && !processedBlockInfo.equals(maybeAltered)) {
/* 67 */         replaced++;
/* 68 */         processedBlockInfoList.set(index, maybeAltered);
/*    */       } 
/*    */     } 
/*    */     
/* 72 */     return processedBlockInfoList;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\CappedProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */