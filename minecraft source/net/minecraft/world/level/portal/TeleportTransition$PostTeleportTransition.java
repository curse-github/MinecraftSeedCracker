/*    */ package net.minecraft.world.level.portal;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
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
/*    */ @FunctionalInterface
/*    */ public interface PostTeleportTransition
/*    */ {
/*    */   void onTransition(Entity paramEntity);
/*    */   
/*    */   default PostTeleportTransition then(PostTeleportTransition postTeleportTransition) {
/* 22 */     return entity -> {
/* 23 */         onTransition(entity);
/* 24 */         postTeleportTransition.onTransition(entity);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\portal\TeleportTransition$PostTeleportTransition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */