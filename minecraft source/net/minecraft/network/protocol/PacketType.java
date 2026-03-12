/*    */ package net.minecraft.network.protocol;public final class PacketType<T extends Packet<?>> extends Record {
/*    */   private final PacketFlow flow;
/*    */   private final Identifier id;
/*    */   
/*  5 */   public PacketType(PacketFlow flow, Identifier id) { this.flow = flow; this.id = id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/PacketType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/PacketType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  5 */     //   0	7	0	this	Lnet/minecraft/network/protocol/PacketType<TT;>; } public PacketFlow flow() { return this.flow; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/PacketType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/PacketType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  5 */     //   0	8	0	this	Lnet/minecraft/network/protocol/PacketType<TT;>; } public Identifier id() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 11 */   public String toString() { return this.flow.id() + "/" + this.flow.id(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\PacketType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */