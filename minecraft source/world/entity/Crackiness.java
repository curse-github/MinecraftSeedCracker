/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class Crackiness {
/*    */   public enum Level {
/*  7 */     NONE,
/*  8 */     LOW,
/*  9 */     MEDIUM,
/* 10 */     HIGH;
/*    */   }
/* 12 */   public static final Crackiness GOLEM = new Crackiness(0.75F, 0.5F, 0.25F);
/* 13 */   public static final Crackiness WOLF_ARMOR = new Crackiness(0.95F, 0.69F, 0.32F);
/*    */   
/*    */   private final float fractionLow;
/*    */   private final float fractionMedium;
/*    */   private final float fractionHigh;
/*    */   
/*    */   private Crackiness(float fractionLow, float fractionMedium, float fractionHigh) {
/* 20 */     this.fractionLow = fractionLow;
/* 21 */     this.fractionMedium = fractionMedium;
/* 22 */     this.fractionHigh = fractionHigh;
/*    */   }
/*    */   
/*    */   public Level byFraction(float fraction) {
/* 26 */     if (fraction < this.fractionHigh) {
/* 27 */       return Level.HIGH;
/*    */     }
/* 29 */     if (fraction < this.fractionMedium) {
/* 30 */       return Level.MEDIUM;
/*    */     }
/* 32 */     if (fraction < this.fractionLow) {
/* 33 */       return Level.LOW;
/*    */     }
/* 35 */     return Level.NONE;
/*    */   }
/*    */   
/*    */   public Level byDamage(ItemStack item) {
/* 39 */     if (!item.isDamageableItem()) {
/* 40 */       return Level.NONE;
/*    */     }
/* 42 */     return byDamage(item.getDamageValue(), item.getMaxDamage());
/*    */   }
/*    */ 
/*    */   
/* 46 */   public Level byDamage(int damage, int maxDamage) { return byFraction((maxDamage - damage) / maxDamage); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Crackiness.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */