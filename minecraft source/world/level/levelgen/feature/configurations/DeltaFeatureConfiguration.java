/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DeltaFeatureConfiguration implements FeatureConfiguration {
/*  9 */   public static final Codec<DeltaFeatureConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockState.CODEC
/* 10 */         .fieldOf("contents").forGetter(()), BlockState.CODEC
/* 11 */         .fieldOf("rim").forGetter(()), 
/* 12 */         IntProvider.codec(0, 16).fieldOf("size").forGetter(()), 
/* 13 */         IntProvider.codec(0, 16).fieldOf("rim_size").forGetter(()))
/* 14 */       .apply(i, DeltaFeatureConfiguration::new));
/*    */   
/*    */   private final BlockState contents;
/*    */   private final BlockState rim;
/*    */   private final IntProvider size;
/*    */   private final IntProvider rimSize;
/*    */   
/*    */   public DeltaFeatureConfiguration(BlockState contents, BlockState rim, IntProvider size, IntProvider rimSize) {
/* 22 */     this.contents = contents;
/* 23 */     this.rim = rim;
/* 24 */     this.size = size;
/* 25 */     this.rimSize = rimSize;
/*    */   }
/*    */ 
/*    */   
/* 29 */   public BlockState contents() { return this.contents; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public BlockState rim() { return this.rim; }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public IntProvider size() { return this.size; }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public IntProvider rimSize() { return this.rimSize; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\DeltaFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */