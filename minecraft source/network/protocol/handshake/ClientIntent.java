/*    */ package net.minecraft.network.protocol.handshake;
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum ClientIntent
/*    */ {
/*  7 */   STATUS,
/*  8 */   LOGIN,
/*  9 */   TRANSFER;
/*    */   
/*    */   private static final int STATUS_ID = 1;
/*    */   
/*    */   private static final int LOGIN_ID = 2;
/*    */   
/*    */   private static final int TRANSFER_ID = 3;
/*    */   
/*    */   public static ClientIntent byId(int id) {
/* 18 */     switch (id) { case 1: 
/*    */       case 2:
/*    */       
/*    */       case 3:
/* 22 */        }  throw new IllegalArgumentException("Unknown connection intent: " + id);
/*    */   }
/*    */ 
/*    */   
/*    */   public int id() {
/* 27 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return 
/*    */ 
/*    */       
/* 30 */       3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\handshake\ClientIntent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */