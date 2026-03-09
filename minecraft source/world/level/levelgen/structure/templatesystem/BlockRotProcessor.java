/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class BlockRotProcessor extends StructureProcessor {
/* 18 */   public static final MapCodec<BlockRotProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 19 */         RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("rottable_blocks").forGetter(()), 
/* 20 */         Codec.floatRange(0.0F, 1.0F).fieldOf("integrity").forGetter(()))
/* 21 */       .apply(i, BlockRotProcessor::new));
/*    */   
/*    */   private final Optional<HolderSet<Block>> rottableBlocks;
/*    */   private final float integrity;
/*    */   
/* 26 */   public BlockRotProcessor(HolderSet<Block> tag, float integrity) { this(Optional.of(tag), integrity); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public BlockRotProcessor(float integrity) { this(Optional.empty(), integrity); }
/*    */ 
/*    */   
/*    */   private BlockRotProcessor(Optional<HolderSet<Block>> blockTagKey, float integrity) {
/* 34 */     this.integrity = integrity;
/* 35 */     this.rottableBlocks = blockTagKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 40 */     RandomSource random = settings.getRandom(processedBlockInfo.pos());
/*    */     
/* 42 */     if ((this.rottableBlocks.isPresent() && !originalBlockInfo.state().is((HolderSet)this.rottableBlocks.get())) || random.nextFloat() <= this.integrity) {
/* 43 */       return processedBlockInfo;
/*    */     }
/* 45 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   protected StructureProcessorType<?> getType() { return StructureProcessorType.BLOCK_ROT; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockRotProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */