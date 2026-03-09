/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.world.level.Level;
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
/*    */ public interface WeatherAccess
/*    */ {
/*    */   static WeatherAccess from(final Level level) {
/* 67 */     return new WeatherAccess()
/*    */       {
/*    */         public float rainLevel() {
/* 70 */           return level.getRainLevel(1.0F);
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 75 */         public float thunderLevel() { return level.getThunderLevel(1.0F); }
/*    */       };
/*    */   }
/*    */   
/*    */   float rainLevel();
/*    */   
/*    */   float thunderLevel();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\WeatherAttributes$WeatherAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */