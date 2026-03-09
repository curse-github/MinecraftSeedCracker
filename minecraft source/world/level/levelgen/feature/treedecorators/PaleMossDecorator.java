/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.HangingMossBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class PaleMossDecorator extends TreeDecorator {
/* 22 */   protected TreeDecoratorType<?> type() { return TreeDecoratorType.PALE_MOSS; }
/*    */ 
/*    */   
/* 25 */   public static final MapCodec<PaleMossDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 26 */         Codec.floatRange(0.0F, 1.0F).fieldOf("leaves_probability").forGetter(()), 
/* 27 */         Codec.floatRange(0.0F, 1.0F).fieldOf("trunk_probability").forGetter(()), 
/* 28 */         Codec.floatRange(0.0F, 1.0F).fieldOf("ground_probability").forGetter(()))
/* 29 */       .apply(i, PaleMossDecorator::new));
/*    */   
/*    */   private final float leavesProbability;
/*    */   private final float trunkProbability;
/*    */   private final float groundProbability;
/*    */   
/*    */   public PaleMossDecorator(float leavesProbability, float trunkProbability, float groundProbability) {
/* 36 */     this.leavesProbability = leavesProbability;
/* 37 */     this.trunkProbability = trunkProbability;
/* 38 */     this.groundProbability = groundProbability;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 43 */     RandomSource random = context.random();
/*    */     
/* 45 */     WorldGenLevel level = (WorldGenLevel)context.level();
/*    */     
/* 47 */     List<BlockPos> logs = Util.shuffledCopy(context.logs(), random);
/* 48 */     if (logs.isEmpty()) {
/*    */       return;
/*    */     }
/* 51 */     BlockPos origin = (BlockPos)Collections.min(logs, Comparator.comparingInt(Vec3i::getY));
/*    */     
/* 53 */     if (random.nextFloat() < this.groundProbability) {
/* 54 */       level.registryAccess()
/* 55 */         .lookup(Registries.CONFIGURED_FEATURE)
/* 56 */         .flatMap(registry -> registry.get(VegetationFeatures.PALE_MOSS_PATCH))
/* 57 */         .ifPresent(mossPatch -> ((ConfiguredFeature)mossPatch.value()).place(level, level.getLevel().getChunkSource().getGenerator(), random, origin.above()));
/*    */     }
/* 59 */     context.logs().forEach(pos -> {
/* 60 */           if (random.nextFloat() < this.trunkProbability) {
/* 61 */             BlockPos down = pos.below();
/* 62 */             if (context.isAir(down)) {
/* 63 */               addMossHanger(down, context);
/*    */             }
/*    */           } 
/*    */         });
/* 67 */     context.leaves().forEach(pos -> {
/* 68 */           if (random.nextFloat() < this.leavesProbability) {
/* 69 */             BlockPos down = pos.below();
/* 70 */             if (context.isAir(down)) {
/* 71 */               addMossHanger(down, context);
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void addMossHanger(BlockPos pos, TreeDecorator.Context context) {
/* 78 */     while (context.isAir(pos.below()) && 
/* 79 */       context.random().nextFloat() >= 0.5D) {
/*    */ 
/*    */       
/* 82 */       context.setBlock(pos, (BlockState)Blocks.PALE_HANGING_MOSS.defaultBlockState().setValue(HangingMossBlock.TIP, Boolean.valueOf(false)));
/* 83 */       pos = pos.below();
/*    */     } 
/* 85 */     context.setBlock(pos, (BlockState)Blocks.PALE_HANGING_MOSS.defaultBlockState().setValue(HangingMossBlock.TIP, Boolean.valueOf(true)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\PaleMossDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */