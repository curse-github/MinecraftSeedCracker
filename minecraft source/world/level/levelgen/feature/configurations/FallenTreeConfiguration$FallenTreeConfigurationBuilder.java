/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FallenTreeConfigurationBuilder
/*    */ {
/*    */   private final BlockStateProvider trunkProvider;
/*    */   private final IntProvider logLength;
/*    */   private List<TreeDecorator> stumpDecorators;
/*    */   private List<TreeDecorator> logDecorators;
/*    */   
/*    */   public FallenTreeConfigurationBuilder(BlockStateProvider trunkProvider, IntProvider logLength) {
/* 35 */     this.stumpDecorators = new ArrayList();
/* 36 */     this.logDecorators = new ArrayList();
/*    */ 
/*    */     
/* 39 */     this.trunkProvider = trunkProvider;
/* 40 */     this.logLength = logLength;
/*    */   }
/*    */   
/*    */   public FallenTreeConfigurationBuilder stumpDecorators(List<TreeDecorator> stumpDecorators) {
/* 44 */     this.stumpDecorators = stumpDecorators;
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public FallenTreeConfigurationBuilder logDecorators(List<TreeDecorator> logDecorators) {
/* 49 */     this.logDecorators = logDecorators;
/* 50 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 54 */   public FallenTreeConfiguration build() { return new FallenTreeConfiguration(this.trunkProvider, this.logLength, this.stumpDecorators, this.logDecorators); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\FallenTreeConfiguration$FallenTreeConfigurationBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */