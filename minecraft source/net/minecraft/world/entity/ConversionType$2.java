/*    */ package net.minecraft.world.entity;
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
/*    */ static enum null
/*    */ {
/*    */   void convert(Mob from, Mob to, ConversionParams params) {
/* 79 */     Entity rootPassenger = from.getFirstPassenger();
/*    */     
/* 81 */     if (rootPassenger != null) {
/* 82 */       rootPassenger.stopRiding();
/*    */     }
/*    */ 
/*    */     
/* 86 */     Entity leashHolder = from.getLeashHolder();
/* 87 */     if (leashHolder != null) {
/* 88 */       from.dropLeash();
/*    */     }
/*    */     
/* 91 */     convertCommon(from, to, params);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ConversionType$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */