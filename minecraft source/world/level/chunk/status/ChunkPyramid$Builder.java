/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*  93 */   private final List<ChunkStep> steps = new ArrayList();
/*     */ 
/*     */   
/*  96 */   public ChunkPyramid build() { return new ChunkPyramid(ImmutableList.copyOf(this.steps)); }
/*     */ 
/*     */   
/*     */   public Builder step(ChunkStatus status, UnaryOperator<ChunkStep.Builder> operator) {
/*     */     ChunkStep.Builder stepBuilder;
/* 101 */     if (this.steps.isEmpty()) {
/* 102 */       stepBuilder = new ChunkStep.Builder(status);
/*     */     } else {
/* 104 */       stepBuilder = new ChunkStep.Builder(status, (ChunkStep)this.steps.getLast());
/*     */     } 
/* 106 */     this.steps.add(((ChunkStep.Builder)operator.apply(stepBuilder)).build());
/* 107 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\status\ChunkPyramid$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */