/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RuleProcessor extends StructureProcessor {
/* 15 */   public static final MapCodec<RuleProcessor> CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules").xmap(RuleProcessor::new, p -> p.rules);
/*    */   
/*    */   private final ImmutableList<ProcessorRule> rules;
/*    */ 
/*    */   
/* 20 */   public RuleProcessor(List<? extends ProcessorRule> rules) { this.rules = ImmutableList.copyOf(rules); }
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 25 */     RandomSource random = RandomSource.create(Mth.getSeed(processedBlockInfo.pos()));
/* 26 */     BlockState locState = level.getBlockState(processedBlockInfo.pos());
/* 27 */     for (UnmodifiableIterator unmodifiableIterator = this.rules.iterator(); unmodifiableIterator.hasNext(); ) { ProcessorRule rule = (ProcessorRule)unmodifiableIterator.next();
/* 28 */       if (rule.test(processedBlockInfo.state(), locState, originalBlockInfo.pos(), processedBlockInfo.pos(), referencePos, random)) {
/* 29 */         return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), rule.getOutputState(), rule.getOutputTag(random, processedBlockInfo.nbt()));
/*    */       } }
/*    */     
/* 32 */     return processedBlockInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected StructureProcessorType<?> getType() { return StructureProcessorType.RULE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RuleProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */