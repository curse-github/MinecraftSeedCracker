/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
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
/*    */ public final class SystemMessage
/*    */   extends Record
/*    */ {
/*    */   private final Message message;
/*    */   private final boolean overlay;
/*    */   private final Optional<List<PlayerDto>> receivingPlayers;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 45 */   public SystemMessage(Message message, boolean overlay, Optional<List<PlayerDto>> receivingPlayers) { this.message = message; this.overlay = overlay; this.receivingPlayers = receivingPlayers; } public Message message() { return this.message; } public boolean overlay() { return this.overlay; } public Optional<List<PlayerDto>> receivingPlayers() { return this.receivingPlayers; }
/* 46 */   public static final Codec<SystemMessage> CODEC = RecordCodecBuilder.create(i -> i.group(Message.CODEC
/* 47 */         .fieldOf("message").forGetter(SystemMessage::message), Codec.BOOL
/* 48 */         .fieldOf("overlay").forGetter(SystemMessage::overlay), PlayerDto.CODEC
/* 49 */         .codec().listOf().lenientOptionalFieldOf("receivingPlayers").forGetter(SystemMessage::receivingPlayers))
/* 50 */       .apply(i, SystemMessage::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\ServerStateService$SystemMessage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */