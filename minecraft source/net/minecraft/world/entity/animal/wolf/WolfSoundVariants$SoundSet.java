/*    */ package net.minecraft.world.entity.animal.wolf;
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
/*    */ public static enum SoundSet
/*    */ {
/* 14 */   CLASSIC("classic", ""),
/* 15 */   PUGLIN("puglin", "_puglin"),
/* 16 */   SAD("sad", "_sad"),
/* 17 */   ANGRY("angry", "_angry"),
/* 18 */   GRUMPY("grumpy", "_grumpy"),
/* 19 */   BIG("big", "_big"),
/* 20 */   CUTE("cute", "_cute");
/*    */   
/*    */   private final String identifier;
/*    */   private final String soundEventSuffix;
/*    */   
/*    */   SoundSet(String identifier, String suffix) {
/* 26 */     this.identifier = identifier;
/* 27 */     this.soundEventSuffix = suffix;
/*    */   }
/*    */ 
/*    */   
/* 31 */   public String getIdentifier() { return this.identifier; }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public String getSoundEventSuffix() { return this.soundEventSuffix; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\wolf\WolfSoundVariants$SoundSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */