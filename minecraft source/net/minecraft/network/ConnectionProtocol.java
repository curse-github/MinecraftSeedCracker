/*    */ package net.minecraft.network;
/*    */ 
/*    */ public static enum ConnectionProtocol {
/*  4 */   HANDSHAKING("handshake"),
/*  5 */   PLAY("play"),
/*  6 */   STATUS("status"),
/*  7 */   LOGIN("login"),
/*  8 */   CONFIGURATION("configuration");
/*    */ 
/*    */   
/*    */   private final String id;
/*    */ 
/*    */   
/* 14 */   ConnectionProtocol(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public String id() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\ConnectionProtocol.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */