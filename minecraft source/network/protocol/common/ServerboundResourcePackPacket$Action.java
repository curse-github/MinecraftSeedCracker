/*    */ package net.minecraft.network.protocol.common;
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
/*    */ public static enum Action
/*    */ {
/* 39 */   SUCCESSFULLY_LOADED,
/* 40 */   DECLINED,
/* 41 */   FAILED_DOWNLOAD,
/* 42 */   ACCEPTED,
/* 43 */   DOWNLOADED,
/* 44 */   INVALID_URL,
/* 45 */   FAILED_RELOAD,
/* 46 */   DISCARDED;
/*    */ 
/*    */ 
/*    */   
/* 50 */   public boolean isTerminal() { return (this != ACCEPTED && this != DOWNLOADED); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ServerboundResourcePackPacket$Action.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */