/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Ticker
/*    */ {
/*    */   private final UniformInt interval;
/*    */   private int ticksUntilNextStart;
/*    */   
/*    */   public Ticker(UniformInt interval) {
/* 56 */     if (interval.getMinValue() <= 1) {
/* 57 */       throw new IllegalArgumentException();
/*    */     }
/* 59 */     this.interval = interval;
/*    */   }
/*    */   
/*    */   public boolean tickDownAndCheck(RandomSource random) {
/* 63 */     if (this.ticksUntilNextStart == 0) {
/* 64 */       this.ticksUntilNextStart = this.interval.sample(random) - 1;
/* 65 */       return false;
/*    */     } 
/*    */     
/* 68 */     return (--this.ticksUntilNextStart == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetEntityLookTargetSometimes$Ticker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */