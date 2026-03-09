/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*    */ 
/*    */ public class FallenTreeConfiguration implements FeatureConfiguration {
/* 13 */   public static final Codec<FallenTreeConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockStateProvider.CODEC
/* 14 */         .fieldOf("trunk_provider").forGetter(()), 
/* 15 */         IntProvider.codec(0, 16).fieldOf("log_length").forGetter(()), TreeDecorator.CODEC
/* 16 */         .listOf().fieldOf("stump_decorators").forGetter(()), TreeDecorator.CODEC
/* 17 */         .listOf().fieldOf("log_decorators").forGetter(()))
/* 18 */       .apply(i, FallenTreeConfiguration::new));
/*    */   
/*    */   public final BlockStateProvider trunkProvider;
/*    */   public final IntProvider logLength;
/*    */   public final List<TreeDecorator> stumpDecorators;
/*    */   public final List<TreeDecorator> logDecorators;
/*    */   
/*    */   protected FallenTreeConfiguration(BlockStateProvider trunkProvider, IntProvider logLength, List<TreeDecorator> stumpDecorators, List<TreeDecorator> logDecorators) {
/* 26 */     this.trunkProvider = trunkProvider;
/* 27 */     this.logLength = logLength;
/* 28 */     this.stumpDecorators = stumpDecorators;
/* 29 */     this.logDecorators = logDecorators;
/*    */   }
/*    */   public static class FallenTreeConfigurationBuilder { private final BlockStateProvider trunkProvider;
/*    */     private final IntProvider logLength;
/*    */     
/*    */     public FallenTreeConfigurationBuilder(BlockStateProvider trunkProvider, IntProvider logLength) {
/* 35 */       this.stumpDecorators = new ArrayList();
/* 36 */       this.logDecorators = new ArrayList();
/*    */ 
/*    */       
/* 39 */       this.trunkProvider = trunkProvider;
/* 40 */       this.logLength = logLength;
/*    */     }
/*    */     private List<TreeDecorator> stumpDecorators; private List<TreeDecorator> logDecorators;
/*    */     public FallenTreeConfigurationBuilder stumpDecorators(List<TreeDecorator> stumpDecorators) {
/* 44 */       this.stumpDecorators = stumpDecorators;
/* 45 */       return this;
/*    */     }
/*    */     
/*    */     public FallenTreeConfigurationBuilder logDecorators(List<TreeDecorator> logDecorators) {
/* 49 */       this.logDecorators = logDecorators;
/* 50 */       return this;
/*    */     }
/*    */ 
/*    */     
/* 54 */     public FallenTreeConfiguration build() { return new FallenTreeConfiguration(this.trunkProvider, this.logLength, this.stumpDecorators, this.logDecorators); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\FallenTreeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */