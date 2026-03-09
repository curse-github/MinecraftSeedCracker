/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ public static enum PacketFlow {
/*  4 */   SERVERBOUND("serverbound"),
/*  5 */   CLIENTBOUND("clientbound");
/*    */   
/*    */   private final String id;
/*    */   
/*  9 */   PacketFlow(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public PacketFlow getOpposite() { return (this == CLIENTBOUND) ? SERVERBOUND : CLIENTBOUND; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public String id() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\PacketFlow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */