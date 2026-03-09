/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ 
/*    */ public interface ConfigurationTask
/*    */ {
/*    */   void start(Consumer<Packet<?>> paramConsumer);
/*    */   
/* 11 */   default boolean tick() { return false; }
/*    */   Type type();
/*    */   
/*    */   public static final class Type extends Record { private final String id;
/*    */     
/* 16 */     public Type(String id) { this.id = id; } public String id() { return this.id; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/ConfigurationTask$Type;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/network/ConfigurationTask$Type; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/ConfigurationTask$Type;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/network/ConfigurationTask$Type;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/* 19 */     public String toString() { return this.id; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ConfigurationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */