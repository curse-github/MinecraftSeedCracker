/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.portal.TeleportTransition;
/*    */ 
/*    */ public interface Portal
/*    */ {
/*    */   public enum Transition {
/* 11 */     CONFUSION,
/* 12 */     NONE;
/*    */   }
/*    */ 
/*    */   
/* 16 */   default int getPortalTransitionTime(ServerLevel level, Entity entity) { return 0; }
/*    */ 
/*    */   
/*    */   TeleportTransition getPortalDestination(ServerLevel paramServerLevel, Entity paramEntity, BlockPos paramBlockPos);
/*    */ 
/*    */   
/* 22 */   default Transition getLocalTransition() { return Transition.NONE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\Portal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */